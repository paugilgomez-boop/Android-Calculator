package com.paugil.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
import android.widget.Button;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {
    TextView textDisplay;
    RadioButton radioDeg, radioRad;
    Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9, btnClear, btnPlus, btnSolve, btnMinus, btnDivision, btnMultiply, btnSin, btnCos,btnTan;

    double storedValue = 0;
    String pendingOperation = "";
    boolean isNewNumber = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textDisplay = findViewById(R.id.textDisplay);
        radioDeg = findViewById(R.id.radioDeg);
        radioRad = findViewById(R.id.radioRad);

        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btnSin = findViewById(R.id.btnSin);
        btnCos = findViewById(R.id.btnCos);
        btnTan = findViewById(R.id.btnTan);

        btnClear = findViewById(R.id.btnClear);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivision = findViewById(R.id.btnDivision);
        btnSolve = findViewById(R.id.btnSolve);

        btn0.setOnClickListener(v -> appendNumber("0"));
        btn1.setOnClickListener(v -> appendNumber("1"));
        btn2.setOnClickListener(v -> appendNumber("2"));
        btn3.setOnClickListener(v -> appendNumber("3"));
        btn4.setOnClickListener(v -> appendNumber("4"));
        btn5.setOnClickListener(v -> appendNumber("5"));
        btn6.setOnClickListener(v -> appendNumber("6"));
        btn7.setOnClickListener(v -> appendNumber("7"));
        btn8.setOnClickListener(v -> appendNumber("8"));
        btn9.setOnClickListener(v -> appendNumber("9"));

        btnPlus.setOnClickListener(v -> handleOperator("+"));
        btnMinus.setOnClickListener(v -> handleOperator("-"));
        btnMultiply.setOnClickListener(v -> handleOperator("*"));
        btnDivision.setOnClickListener(v -> handleOperator("/"));

        btnSolve.setOnClickListener(v -> handleEqual());
        btnClear.setOnClickListener(v -> clearCalculator());

        btnSin.setOnClickListener(v -> applyTrigFunction("sin"));
        btnCos.setOnClickListener(v -> applyTrigFunction("cos"));
        btnTan.setOnClickListener(v -> applyTrigFunction("tan"));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void appendNumber(String number) {
        String currentText = textDisplay.getText().toString();

        if (currentText.equals("Error")) {
            textDisplay.setText(number);
            isNewNumber = false;
            return;
        }

        if (isNewNumber || currentText.equals("0")) {
            textDisplay.setText(number);
            isNewNumber = false;
        } else {
            textDisplay.setText(currentText + number);
        }
    }
    private void handleOperator(String operator) {
        if (textDisplay.getText().toString().equals("Error")) {
            return;
        }

        double currentValue = Double.parseDouble(textDisplay.getText().toString());

        if (pendingOperation.isEmpty()) {
            storedValue = currentValue;
        } else if (!isNewNumber) {
            storedValue = calculate(storedValue, currentValue, pendingOperation);

            if (Double.isNaN(storedValue)) {
                textDisplay.setText("Error");
                resetStateAfterError();
                return;
            }

            textDisplay.setText(formatNumber(storedValue));
        }

        pendingOperation = operator;
        isNewNumber = true;
    }
    private void handleEqual() {
        if (textDisplay.getText().toString().equals("Error")) {
            return;
        }

        if (pendingOperation.isEmpty()) {
            return;
        }

        double currentValue = Double.parseDouble(textDisplay.getText().toString());
        storedValue = calculate(storedValue, currentValue, pendingOperation);

        if (Double.isNaN(storedValue)) {
            textDisplay.setText("Error");
            resetStateAfterError();
            return;
        }

        textDisplay.setText(formatNumber(storedValue));
        pendingOperation = "";
        isNewNumber = true;
    }

    private double calculate(double left, double right, String operator) {
        if (operator.equals("+")) {
            return left + right;
        } else if (operator.equals("-")) {
            return left - right;
        } else if (operator.equals("*")) {
            return left * right;
        } else if (operator.equals("/")) {
            if (right == 0) {
                return Double.NaN;
            }
            return left / right;
        }

        return right;
    }

    private void applyTrigFunction(String functionName) {
        String currentText = textDisplay.getText().toString();

        if (currentText.equals("Error")) {
            return;
        }

        double value = Double.parseDouble(currentText);

        if (radioDeg.isChecked()) {
            value = Math.toRadians(value);
        }

        double result = 0;

        if (functionName.equals("sin")) {
            result = Math.sin(value);
        } else if (functionName.equals("cos")) {
            result = Math.cos(value);
        } else if (functionName.equals("tan")) {
            result = Math.tan(value);
        }

        textDisplay.setText(formatNumber(result));
        isNewNumber = true;
        pendingOperation = "";
        storedValue = result;
    }

    private void clearCalculator() {
        textDisplay.setText("0");
        storedValue = 0;
        pendingOperation = "";
        isNewNumber = true;
    }

    private void resetStateAfterError() {
        storedValue = 0;
        pendingOperation = "";
        isNewNumber = true;
    }

    private String formatNumber(double number) {
        if (number == (long) number) {
            return String.valueOf((long) number);
        }
        return String.valueOf(number);
    }

}