package com.example.calculator_midtermsmock;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;

    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private Button btnAdd, btnSubtract, btnMultiply, btnDivide, btnEquals, btnClear, btnDot;

    private String currentInput = "";
    private double firstOperand = 0;
    private String operator = "";
    private boolean isNewInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);

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

        btnAdd = findViewById(R.id.btnAdd);
        btnSubtract = findViewById(R.id.btnSubtract);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);
        btnEquals = findViewById(R.id.btnEquals);
        btnClear = findViewById(R.id.btnClear);
        btnDot = findViewById(R.id.btnDot);

        tvDisplay.setText(getString(R.string.startup_message));

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                tvDisplay.setText(getString(R.string.default_display));
            }
        }, getResources().getInteger(R.integer.startup_delay));

        setNumberClickListeners();
        setOperatorClickListeners();
        setSpecialClickListeners();
    }

    private void setNumberClickListeners() {
        View.OnClickListener numberListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button clickedButton = (Button) view;
                String digit = clickedButton.getText().toString();

                if (isNewInput) {
                    currentInput = "";
                    isNewInput = false;
                }

                currentInput += digit;
                tvDisplay.setText(currentInput);
            }
        };

        btn0.setOnClickListener(numberListener);
        btn1.setOnClickListener(numberListener);
        btn2.setOnClickListener(numberListener);
        btn3.setOnClickListener(numberListener);
        btn4.setOnClickListener(numberListener);
        btn5.setOnClickListener(numberListener);
        btn6.setOnClickListener(numberListener);
        btn7.setOnClickListener(numberListener);
        btn8.setOnClickListener(numberListener);
        btn9.setOnClickListener(numberListener);
    }

    private void setOperatorClickListeners() {
        View.OnClickListener operatorListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button clickedButton = (Button) view;

                if (!currentInput.isEmpty()) {
                    firstOperand = Double.parseDouble(currentInput);
                    operator = clickedButton.getText().toString();
                    isNewInput = true;
                }
            }
        };

        btnAdd.setOnClickListener(operatorListener);
        btnSubtract.setOnClickListener(operatorListener);
        btnMultiply.setOnClickListener(operatorListener);
        btnDivide.setOnClickListener(operatorListener);
    }

    private void setSpecialClickListeners() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentInput = "";
                firstOperand = 0;
                operator = "";
                isNewInput = false;
                tvDisplay.setText(getString(R.string.default_display));
            }
        });

        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNewInput) {
                    currentInput = "";
                    isNewInput = false;
                }

                if (!currentInput.contains(getString(R.string.btn_dot))) {
                    if (currentInput.isEmpty()) {
                        currentInput = getString(R.string.default_display);
                    }
                    currentInput += getString(R.string.btn_dot);
                    tvDisplay.setText(currentInput);
                }
            }
        });

        btnEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentInput.isEmpty() || operator.isEmpty()) {
                    return;
                }

                double secondOperand = Double.parseDouble(currentInput);
                double result = 0;

                switch (operator) {
                    case "+":
                        result = firstOperand + secondOperand;
                        tvDisplay.setText(String.valueOf(result));
                        break;

                    case "-":
                        result = firstOperand - secondOperand;
                        tvDisplay.setText(String.valueOf(result));
                        break;

                    case "*":
                        result = firstOperand * secondOperand;
                        tvDisplay.setText(String.valueOf(result));
                        break;

                    case "/":
                        if (secondOperand == 0) {
                            tvDisplay.setText(getString(R.string.default_display));
                            currentInput = "";
                            operator = "";
                            isNewInput = true;
                            return;
                        } else {
                            result = firstOperand / secondOperand;
                            tvDisplay.setText(String.valueOf(result));
                        }
                        break;
                }

                currentInput = String.valueOf(result);
                operator = "";
                isNewInput = true;
            }
        });
    }
}