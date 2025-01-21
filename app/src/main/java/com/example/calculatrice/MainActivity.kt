package com.example.calculatrice

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

class MainActivity : ComponentActivity() {
    private lateinit var resultTextView: TextView
    private var currentNumber: String = ""
    private var operator: String = ""
    private var firstNumber: Double = 0.0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultTextView = findViewById(R.id.textViewResult)
        val buttons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button7,
            R.id.button8, R.id.button9, R.id.buttonModulo
        )
        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { numberClicked(it as Button) }
        }
        val operatorButtons = listOf(
            R.id.buttonAdd, R.id.buttonSubtract,
            R.id.buttonMultiply, R.id.buttonDivide, R.id.buttonModulo
        )
        operatorButtons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { operatorClicked(it as Button) }
        }
        findViewById<Button>(R.id.buttonEqual).setOnClickListener { calculateResult() }
    }

    private fun numberClicked(button: Button) {
        currentNumber += button.text
        resultTextView.text = currentNumber
    }

    private fun operatorClicked(button: Button) {
        if (currentNumber.isNotEmpty()) {
            firstNumber = currentNumber.toDouble()
            currentNumber = ""
            operator = button.text.toString()
        }
    }

    private fun calculateResult() {
        val secondNumber = currentNumber.toDouble()
        val result = when (operator) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> firstNumber / secondNumber
            "%" -> firstNumber % secondNumber
            else -> 0.0
        }
        resultTextView.text = result.toString()
        currentNumber = result.toString()
    }
}
