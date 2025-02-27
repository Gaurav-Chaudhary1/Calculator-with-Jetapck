package com.example.basiccalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicCalculator()
        }
    }
}

@Composable
fun BasicCalculator() {
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(12.dp), // Reduced padding
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Display Screen
        CalculatorDisplay(input)

        // Buttons Grid
        CalculatorButtons(input) { input = it }
    }
}

@Composable
fun CalculatorDisplay(value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = value.ifEmpty { "0" },
            color = Color.White,
            fontSize = 60.sp // Reduced text size
        )
    }
}

@Composable
fun CalculatorButtons(input: String, onInputChange: (String) -> Unit) {
    val buttons = listOf(
        listOf("AC", "±", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Reduced spacing
    ) {
        for (row in buttons) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Reduced spacing
            ) {
                for (button in row) {
                    CalculatorButton(button, onButtonClick = {
                        onInputChange(handleInput(input, it))
                    })
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(button: String, onButtonClick: (String) -> Unit) {
    val buttonColor = when (button) {
        "÷", "×", "-", "+", "=" -> Color(0xFFFF9800) // Orange
        "AC", "±", "%" -> Color(0xFFBDBDBD) // Light Gray
        else -> Color(0xFF424242) // Dark Gray
    }

    val buttonModifier = if (button == "0") {
        Modifier
            .width(140.dp) // Reduced width
            .height(70.dp) // Reduced height
    } else {
        Modifier
            .size(70.dp) // Reduced button size
    }

    Button(
        onClick = { onButtonClick(button) },
        modifier = buttonModifier.padding(4.dp), // Reduced padding
        colors = ButtonDefaults.buttonColors(buttonColor),
        shape = CircleShape
    ) {
        Text(
            text = button,
            fontSize = if (button == "AC") 13.sp else 20.sp, // Smaller font for "AC"
            color = Color.White,
            maxLines = 1, // Prevents wrapping
            softWrap = false // Ensures text stays in one line
        )
    }
}

fun handleInput(currentInput: String, buttonValue: String): String {
    return when (buttonValue) {
        "AC" -> ""
        "±" -> if (currentInput.startsWith("-")) currentInput.drop(1) else "-$currentInput"
        "%" -> (currentInput.toDoubleOrNull()?.div(100))?.toString() ?: currentInput
        "=" -> evaluateExpression(currentInput)
        else -> currentInput + buttonValue
    }
}

fun evaluateExpression(expression: String): String {
    return try {
        val formattedExpression = expression.replace("×", "*").replace("÷", "/")
        val result = ExpressionBuilder(formattedExpression).build().evaluate()
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalculator() {
    BasicCalculator()
}
