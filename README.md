# Projet Calculatrice

### Enoncé:
Créer une application calculatrice android en kotlin \
De 0-10 (un seul digit) avec un des 5 opérateurs (+, -, *, /, %) (6 points) \
Bonus: Possible de faire une multiplication avec plusieurs digits (2 points) \
Bonus: Plusieurs opérateurs identiques (2 points) \
Bonus: Plusieurs opérateur identiques avec gestion de la priorité (2 points)    
Bonus: Historique sous forme de liste en dessous de la calculatrice (2 points)   
Bonus: Le rendus est agréable à regarder (2 points)

Consignes additionnelles: \
Le rendu doit se faire sur Github \
On considère que la saisie est parfaite: pas de gestion d’erreur 

Barème: \
Clarté et propreté du code (nommage, indentation) (4 points) \
Rendus avant 16h30 le 20/01/2025 (2 points) 

## Code

### Réflexions:

A la question: Quels imports obligatoires pour un code Kotlin avec interaction par clic de l'utilisateur sous  AndroidStudio? (via google et non Gemini ou autre AI) \
Plusieurs liens intéressants sont à noter (cf Sources)



```kotlin
// Importation des classes nécessaires depuis les packages Android
import android.view.View 
import android.widget.Button 
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity 
```

Le package `android.view` comprend les classes liées aux vues (éléments d'interface visuelle).
`View`: la classe de base pour tous les composants d'interface utilisateur en Android, comme les boutons, les textes...
Le package `android.widget` contient les classes de widgets d'interface utilisateur.
`Button` : widget qui représente un bouton à l'écran cliquable par l'utilisateur
`TextView `: widget pour afficher du texte à l'écran. Il peut être utilisé pour montrer des informations textuelles à l'utilisateur.
Le package `androidx.appcompat` contient des composants pour les activités modernes.
`AppCompatActivity` : une version améliorée de Activity qui est compatible avec les versions précédentes d'Android et fournit des fonctionnalités avancées.

⚠️ Suite à l'avancement du projet à la multiplication de conflits, Le package `androidx.appcompat` était une fausse bonne idée.  \
Du coup j'ai réorienté le projet sur le package `ComponentActivité `et `compose`.

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.calculatrice.ui.theme.CalculatriceTheme
import android.view.View
import android.widget.Button
import android.widget.TextView
```

### Mise en forme/page 

Dans le répertoire  app/src/main/res/drawable/:

Création du fichier `rounded_button.xml` qui permettra de définir la formes des boutons, en ajustant les arrondis des coins et la couleur du contour.

```javascript
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="10dp" /> 
    <solid android:color="@color/buttonBackgroundColor" /> 
</shape>
```
Création du fichier `rounded_equal_button.xml` qui permettra de définir la formes du bouton `"="`, en ajustant les arrondis des coins et la couleur du contour.
```javascript
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="10dp" /> 
    <solid android:color="@color/equalButtonBackgroundColor" /> 
</shape>
```

Dans le répertoire  app/src/main/res/values/:


Création du fichier `styles.xml` qui définira les autres aspects composants boutons, leur background, les textes, dans le but d'optimiser en permettant la réutilisation des styles, la cohérence et surtout une facilité à maintenir ou modifier le style en un seul fichier:
```javascript
<resources>

    <!-- Style pour les boutons numériques et les opérateurs -->
    <style name="CalculatorButton">
        <item name="android:background">@drawable/rounded_button</item>
        <item name="android:textColor">@color/buttonTextColor</item>
        <item name="android:padding">10dp</item>
        <item name="android:layout_margin">5dp</item>
        <item name="android:elevation">2dp</item>
    </style>

    <!-- Style spécifique pour les boutons des opérateurs -->
    <style name="OperatorButton" parent="CalculatorButton">
        <item name="android:textColor">@color/operatorTextColor</item>
    </style>

    <!-- Style pour le bouton égal -->
    <style name="EqualButton" parent="CalculatorButton">
        <item name="android:background">@drawable/rounded_equal_button</item> 
    </style>
</resources>
```

A titre informatif, les `dp` (Density_independent Pixels) sont des unités de mesures indépendantes de la densité, utilisées dans le développement Android, qui permettent de créer une interface utilisateur qui s'adaptent correctement aux différents et écrans et résolutions.

```javascript 
<item name="android:background">@color/buttonBackgroundColor</item>
``` 
android:background est l'attribut Android pour définir la couleur de fond de l'élement d'interface `button`.  \
`@color/buttonBackgroundColor` @color indique que la ressource est définie dans le fichier de ressources de couleur (`colors.xml`)
buttonBackgroundColor est le nom de la ressourec définie dans le fichier cité précédemment.

`android:elevation` permet de jouer sur la "profondeur" pour donner un meilleur rendu visuel.

Mise à jour du fichier `colors.xml`

```javascript
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="buttonBackgroundColor">#333333</color> <!-- Gris foncé -->
    <color name="equalButtonBackgroundColor">#FFA500</color> <!-- Orange -->
    <color name="buttonTextColor">#FFFFFF</color> <!-- Blanc -->
    <color name="operatorTextColor">#FFA500</color> <!-- Orange -->
    <color name="appBackgroundColor">#000000</color> <!-- Noir -->
</resources>
```
Pour donner un aspect se rapprochant de la calculatrice sous Android, j'ai choisi de mettre en noir le background de l'application, de donner un gris foncé pour les boutons hormis le bouton égal qui sera orange. Le blanc sera la couleur du texte des boutons sauf pour celui des opérateurs qui sera orange.


 ⚠️  Suite à une "alerte" AndroidStudio au niveau du contraste du bouton `"="`
 ```javascript
 <color name="equalButtonBackgroundColor">#ff7f00</color> <!-- Orange plus vif -->
```

Ainsi les couleurs des interfaces sont centralisées, avec tous les avantaques évoqués précédemment pour le fichier `styles.xml`

Mise à jour du fichier `activity_main.xml`
Ce fichier qui permettra de configuer la disposition de l'interface utilisateur et de définir les interfaces, qui seront gérées pour le style (couleurs etc..) par les deux fichiers précédents.

Arbitrairement vu la thématique, j'ai opté pour `GridLayout` plutôt que `ConstraintLayout`, plus puissant mais plus "technique". Pour le projet une grille suffit amplement.

```javascript
<?xml version="1.0" encoding="utf-8"?>
<GridLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp"
    android:rowCount="5"
    android:columnCount="4"
    android:orientation="horizontal"
    android:background="@color/appBackgroundColor">

    <!-- Affichage du résultat -->
    <TextView
        android:id="@+id/textViewResult"
        android:layout_width="0dp"
        android:layout_height="5dp"
        android:layout_rowWeight="1.5"
        android:layout_columnSpan="4"
        android:layout_gravity="fill"
        android:background="@android:color/black" <!-- Fond noir -->
        android:gravity="end|bottom"
        android:padding="16dp"
        android:textSize="24sp"
        android:textColor="@color/buttonTextColor"/> <!-- Texte blanc -->

    <!-- Ligne de boutons : 7, 8, 9, / -->
    <Button
        android:id="@+id/button7"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="7"/>
        
    <Button
        android:id="@+id/button8"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="8"/>

    <Button
        android:id="@+id/button9"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="9"/>
        
    <Button
        android:id="@+id/buttonDivide"
        style="@style/OperatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="/"/>

    <!-- Ligne de boutons : 4, 5, 6, * -->
    <Button
        android:id="@+id/button4"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="4"/>
        
    <Button
        android:id="@+id/button5"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="5"/>

    <Button
        android:id="@+id/button6"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="6"/>
        
    <Button
        android:id="@+id/buttonMultiply"
        style="@style/OperatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="*"/>

    <!-- Ligne de boutons : 1, 2, 3, - -->
    <Button
        android:id="@+id/button1"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="1"/>
        
    <Button
        android:id="@+id/button2"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="2"/>

    <Button
        android:id="@+id/button3"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="3"/>
        
    <Button
        android:id="@+id/buttonSubtract"
        style="@style/OperatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="-"/>

    <!-- Ligne de boutons : 0, =, + -->
    <Button
        android:id="@+id/button0"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="1"
        android:layout_columnWeight="1"
        android:text="0"/>

    <Button
        android:id="@+id/buttonEqual"
        style="@style/EqualButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="="/>

    <Button
        android:id="@+id/buttonAdd"
        style="@style/OperatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="+"/>

    
</GridLayout>
```
`android:id="@+id/`  `@` indique que c'est une ressource  \
grâce à `id` cela permet d'attribuer un identifiant unique.  \
le `+`  indique que l'identifiant doit être créé s'il n'existe pas  \
La partie après `id/` est le nom de l'identifiant, cela permet à ce qu'il soit utilisé ailleurs dans le code. (cf `findViewById(R.id."identifiant")` )

⚠️ Après vérification et tests: oubli du bouton "modulo"

```javascript
<!-- Suppression des lignes supérieures car identiques>

    <!-- Ligne de boutons : 0, %, =, + -->
    <Button
        android:id="@+id/button0"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="0"/>

    <Button
        android:id="@+id/buttonModulo"
        style="@style/OperatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="%"/>

    <Button
        android:id="@+id/buttonEqual"
        style="@style/EqualButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="0.5"
        android:text="="/>
    
    <Button
        android:id="@+id/buttonAdd"
        style="@style/OperatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="+"/>


</GridLayout>
```
Ajout du bouton "modulo" sur la ligne du `0` en modifiant les valeurs de ce dernier :  
```javascript
android:layout_columnWeight="1" 
android:layout_columnSpan="1"
```
par:
```javascript
android:layout_columnWeight="0.5" 
android:layout_columnSpan="0.5"
```

⚠️ Mauvais positionnement du bouton `"="`, il est plus intuitif qu'il soit en bas à droite:

```javascript
<!-- Ligne de boutons : 0, %, +, = -->
    <Button
        android:id="@+id/button0"
        style="@style/CalculatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="0"/>

    <Button
        android:id="@+id/buttonModulo"
        style="@style/OperatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="%"/>

    <Button
        android:id="@+id/buttonAdd"
        style="@style/OperatorButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="1"
        android:text="+"/>

    <Button
        android:id="@+id/buttonEqual"
        style="@style/EqualButton"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_rowWeight="0.5"
        android:layout_columnWeight="0.5"
        android:text="="/>

</GridLayout>
```
### Sources

- [Documentation Kotlin:](https://kotlinlang.org/docs/home.html)
- [Android Developers](https://developer.android.com/codelabs/basic-android-kotlin-compose-button-click-practice-problem?hl=fr#1) 
- [Premiers pas avec Jetpack Compose](https://developer.android.com/develop/ui/compose/documentation?hl=fr)
- [Tuto les boutons](https://www.youtube.com/watch?v=SWTddpV0l8Q&t=539s)
- [Créer des ombres et ajuster la taille des vues](https://developer.android.com/develop/ui/views/theming/shadows-clipping?hl=fr)
- [GridLayout](https://developer.android.com/reference/android/widget/GridLayout)
- [ConstraintLayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout)



