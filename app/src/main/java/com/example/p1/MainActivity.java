package com.example.p1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Declare variables
    TextView resultText;
    Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnClear, btnPlusMinus, btnPercent, btnDivide;
    Button btnMultiply, btnMinus, btnPlus, btnEqual, btnDot;

    // Scientific calculator buttons
    Button btnPower, btnSquare, btnCube, btnLog, btnFactorial;
    Button btnSqrt, btnCubeRoot, btnOpenBracket, btnCloseBracket;
    Button btnSin, btnCos, btnTan, btnE, btnPi, btnRad;
    Button btnLn, btnInverse, btnYRoot, btnExp, btnTenPow;
    Button btnRand, btnSinh, btnCosh, btnTanh, btnEE;

    // Memory buttons
    Button btnMC, btnMPlus, btnMMinus, btnMR;

    // Other buttons
    Button btnCalculator;

    // For calculations
    double firstNum = 0;
    double secondNum = 0;
    String operation = "";
    boolean isNewOperation = true;
    boolean isRadMode = true; // Default to radian mode

    // For memory function
    double memoryValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restore state if available
        if (savedInstanceState != null) {
            firstNum = savedInstanceState.getDouble("firstNum", 0);
            secondNum = savedInstanceState.getDouble("secondNum", 0);
            operation = savedInstanceState.getString("operation", "");
            isNewOperation = savedInstanceState.getBoolean("isNewOperation", true);
            isRadMode = savedInstanceState.getBoolean("isRadMode", true);
            memoryValue = savedInstanceState.getDouble("memoryValue", 0);
        }

        // Connect variables to UI elements
        resultText = findViewById(R.id.resultText);

        // Initialize basic calculator buttons
        initializeBasicButtons();

        // Initialize scientific calculator buttons if available
        initializeScientificButtons();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("firstNum", firstNum);
        outState.putDouble("secondNum", secondNum);
        outState.putString("operation", operation);
        outState.putBoolean("isNewOperation", isNewOperation);
        outState.putBoolean("isRadMode", isRadMode);
        outState.putDouble("memoryValue", memoryValue);
    }

    private void initializeBasicButtons() {
        // Basic calculator buttons
        btn0 = findViewById(R.id.zero);
        btn1 = findViewById(R.id.one);
        btn2 = findViewById(R.id.two);
        btn3 = findViewById(R.id.three);
        btn4 = findViewById(R.id.four);
        btn5 = findViewById(R.id.five);
        btn6 = findViewById(R.id.six);
        btn7 = findViewById(R.id.seven);
        btn8 = findViewById(R.id.eight);
        btn9 = findViewById(R.id.nine);

        btnDot = findViewById(R.id.dot);
        btnClear = findViewById(R.id.clear);
        btnPlusMinus = findViewById(R.id.Pm);
        btnPercent = findViewById(R.id.per);

        btnDivide = findViewById(R.id.div);
        btnMultiply = findViewById(R.id.multiply);

        // Fix for landscape/portrait mode difference - try both IDs
        try {
            btnMinus = findViewById(R.id.min);
        } catch (Exception e) {
            try {
                btnMinus = findViewById(R.id.minus);
            } catch (Exception ex) {
                // Both failed, handle appropriately
            }
        }

        btnPlus = findViewById(R.id.plus);
        btnEqual = findViewById(R.id.equal);

        try {
            // Calculator button may not be present in all layouts
            btnCalculator = findViewById(R.id.calculator);
            btnCalculator.setOnClickListener(v -> {
                // Logic for calculator button if needed
            });
        } catch (Exception e) {
            // Calculator button not found
        }

        // Set up number button click listeners
        if (btn0 != null) btn0.setOnClickListener(v -> numberClick("0"));
        if (btn1 != null) btn1.setOnClickListener(v -> numberClick("1"));
        if (btn2 != null) btn2.setOnClickListener(v -> numberClick("2"));
        if (btn3 != null) btn3.setOnClickListener(v -> numberClick("3"));
        if (btn4 != null) btn4.setOnClickListener(v -> numberClick("4"));
        if (btn5 != null) btn5.setOnClickListener(v -> numberClick("5"));
        if (btn6 != null) btn6.setOnClickListener(v -> numberClick("6"));
        if (btn7 != null) btn7.setOnClickListener(v -> numberClick("7"));
        if (btn8 != null) btn8.setOnClickListener(v -> numberClick("8"));
        if (btn9 != null) btn9.setOnClickListener(v -> numberClick("9"));

        if (btnDot != null) {
            btnDot.setOnClickListener(v -> {
                String currentText = resultText.getText().toString();
                // Only add decimal if there isn't one already
                if (!currentText.contains(".")) {
                    numberClick(".");
                }
            });
        }

        // Set up operation button click listeners
        if (btnPlus != null) btnPlus.setOnClickListener(v -> operationClick("+"));
        if (btnMinus != null) btnMinus.setOnClickListener(v -> operationClick("-"));
        if (btnMultiply != null) btnMultiply.setOnClickListener(v -> operationClick("×"));
        if (btnDivide != null) btnDivide.setOnClickListener(v -> operationClick("÷"));

        if (btnPercent != null) {
            btnPercent.setOnClickListener(v -> {
                try {
                    double number = Double.parseDouble(resultText.getText().toString());
                    double result = number / 100;
                    resultText.setText(formatResult(result));
                    isNewOperation = true;
                } catch (Exception e) {
                    resultText.setText("Error");
                }
            });
        }

        if (btnPlusMinus != null) {
            btnPlusMinus.setOnClickListener(v -> {
                try {
                    double number = Double.parseDouble(resultText.getText().toString());
                    number = number * -1;
                    resultText.setText(formatResult(number));
                } catch (Exception e) {
                    resultText.setText("Error");
                }
            });
        }

        if (btnClear != null) {
            btnClear.setOnClickListener(v -> {
                resultText.setText("0");
                firstNum = 0;
                secondNum = 0;
                operation = "";
                isNewOperation = true;
            });
        }

        if (btnEqual != null) {
            btnEqual.setOnClickListener(v -> calculateResult());
        }
    }

    private void initializeScientificButtons() {
        // Try to set up scientific calculator functions (wrapped in try-catch to handle layout differences)
        try {
            // Memory buttons
            btnMC = findViewById(R.id.mc);
            btnMPlus = findViewById(R.id.mPlus);
            btnMMinus = findViewById(R.id.mMinus);
            btnMR = findViewById(R.id.mr);

            if (btnMC != null) btnMC.setOnClickListener(v -> memoryValue = 0);
            if (btnMPlus != null) btnMPlus.setOnClickListener(v -> {
                try {
                    memoryValue += Double.parseDouble(resultText.getText().toString());
                } catch (Exception e) {
                    // Handle error
                }
            });
            if (btnMMinus != null) btnMMinus.setOnClickListener(v -> {
                try {
                    memoryValue -= Double.parseDouble(resultText.getText().toString());
                } catch (Exception e) {
                    // Handle error
                }
            });
            if (btnMR != null) btnMR.setOnClickListener(v -> {
                resultText.setText(formatResult(memoryValue));
                isNewOperation = true;
            });

            // Power, square, cube
            btnPower = findViewById(R.id.power);
            btnSquare = findViewById(R.id.sqr);
            btnCube = findViewById(R.id.cube);

            // Parentheses
            btnOpenBracket = findViewById(R.id.parenthesisOpen);
            btnCloseBracket = findViewById(R.id.parenthesisClose);

            // Log, factorial, sqrt
            btnLog = findViewById(R.id.log);
            btnLn = findViewById(R.id.ln);
            btnFactorial = findViewById(R.id.factorial);
            btnSqrt = findViewById(R.id.sqrt);

            // Other scientific buttons
            btnCubeRoot = findViewById(R.id.cubeRoot);
            btnYRoot = findViewById(R.id.yRoot);
            btnInverse = findViewById(R.id.inverse);
            btnExp = findViewById(R.id.exp);
            btnTenPow = findViewById(R.id.tenPow);

            // Trig functions
            btnSin = findViewById(R.id.sin);
            btnCos = findViewById(R.id.cos);
            btnTan = findViewById(R.id.tan);
            btnSinh = findViewById(R.id.sinh);
            btnCosh = findViewById(R.id.cosh);
            btnTanh = findViewById(R.id.tanh);

            // Constants and mode
            btnE = findViewById(R.id.e);
            btnPi = findViewById(R.id.pi);
            btnRad = findViewById(R.id.rad);
            btnEE = findViewById(R.id.EE);
            btnRand = findViewById(R.id.rand);

            // Set up scientific button click listeners
            if (btnPower != null) {
                btnPower.setOnClickListener(v -> operationClick("^"));
            }

            if (btnSquare != null) {
                btnSquare.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result = number * number;
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnCube != null) {
                btnCube.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result = number * number * number;
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnInverse != null) {
                btnInverse.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        if (number == 0) {
                            resultText.setText("Error");
                            return;
                        }
                        double result = 1 / number;
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnLog != null) {
                btnLog.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        if (number <= 0) {
                            resultText.setText("Error");
                            return;
                        }
                        double result = Math.log10(number);
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnLn != null) {
                btnLn.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        if (number <= 0) {
                            resultText.setText("Error");
                            return;
                        }
                        double result = Math.log(number);
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnFactorial != null) {
                btnFactorial.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        int n = (int) number;
                        if (n != number || n < 0) {
                            resultText.setText("Error");
                            return;
                        }
                        long factorial = 1;
                        for (int i = 1; i <= n; i++) {
                            factorial *= i;
                            if (factorial < 0) { // Overflow check
                                resultText.setText("Error");
                                return;
                            }
                        }
                        resultText.setText(formatResult((double) factorial));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnSqrt != null) {
                btnSqrt.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        if (number < 0) {
                            resultText.setText("Error");
                            return;
                        }
                        double result = Math.sqrt(number);
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnCubeRoot != null) {
                btnCubeRoot.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result = Math.cbrt(number);
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnYRoot != null) {
                btnYRoot.setOnClickListener(v -> {
                    operationClick("yroot");
                });
            }

            if (btnOpenBracket != null) {
                btnOpenBracket.setOnClickListener(v -> {
                    // For a real calculator app, we'd need to implement expression parsing
                    // This is a simplified version
                    resultText.setText("(");
                    isNewOperation = false;
                });
            }

            if (btnCloseBracket != null) {
                btnCloseBracket.setOnClickListener(v -> {
                    // For a real calculator app, we'd need to implement expression parsing
                    // This is a simplified version
                    String currentText = resultText.getText().toString();
                    if (!currentText.equals("0") && !currentText.equals("(")) {
                        resultText.setText(currentText + ")");
                    }
                });
            }

            if (btnSin != null) {
                btnSin.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result;
                        if (isRadMode) {
                            result = Math.sin(number);
                        } else {
                            result = Math.sin(Math.toRadians(number));
                        }
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnCos != null) {
                btnCos.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result;
                        if (isRadMode) {
                            result = Math.cos(number);
                        } else {
                            result = Math.cos(Math.toRadians(number));
                        }
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnTan != null) {
                btnTan.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result;
                        if (isRadMode) {
                            result = Math.tan(number);
                        } else {
                            result = Math.tan(Math.toRadians(number));
                        }
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnSinh != null) {
                btnSinh.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result = Math.sinh(number);
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnCosh != null) {
                btnCosh.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result = Math.cosh(number);
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnTanh != null) {
                btnTanh.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result = Math.tanh(number);
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnE != null) {
                btnE.setOnClickListener(v -> {
                    resultText.setText(formatResult(Math.E));
                    isNewOperation = true;
                });
            }

            if (btnPi != null) {
                btnPi.setOnClickListener(v -> {
                    resultText.setText(formatResult(Math.PI));
                    isNewOperation = true;
                });
            }

            if (btnExp != null) {
                btnExp.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result = Math.exp(number);
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnTenPow != null) {
                btnTenPow.setOnClickListener(v -> {
                    try {
                        double number = Double.parseDouble(resultText.getText().toString());
                        double result = Math.pow(10, number);
                        resultText.setText(formatResult(result));
                        isNewOperation = true;
                    } catch (Exception e) {
                        resultText.setText("Error");
                    }
                });
            }

            if (btnRand != null) {
                btnRand.setOnClickListener(v -> {
                    double result = Math.random();
                    resultText.setText(formatResult(result));
                    isNewOperation = true;
                });
            }

            if (btnEE != null) {
                btnEE.setOnClickListener(v -> {
                    String currentText = resultText.getText().toString();
                    if (!currentText.contains("E")) {
                        resultText.setText(currentText + "E+0");
                    }
                });
            }

            if (btnRad != null) {
                btnRad.setOnClickListener(v -> {
                    isRadMode = !isRadMode;
                    if (isRadMode) {
                        btnRad.setText("Rad");
                    } else {
                        btnRad.setText("Deg");
                    }
                });

                // Set initial button text based on mode
                btnRad.setText(isRadMode ? "Rad" : "Deg");
            }

        } catch (Exception e) {
            // If any button is not found (i.e., using the basic layout), this catch block will prevent a crash
        }
    }

    // Method to handle number button clicks
    private void numberClick(String digit) {
        String currentText = resultText.getText().toString();

        if (isNewOperation) {
            resultText.setText(digit);
            isNewOperation = false;
        } else {
            // Check if current display is just "0" and the new digit isn't a decimal
            if (currentText.equals("0") && !digit.equals(".")) {
                resultText.setText(digit);
            } else {
                resultText.setText(currentText + digit);
            }
        }
    }

    // Method to handle operation button clicks
    private void operationClick(String op) {
        try {
            firstNum = Double.parseDouble(resultText.getText().toString());
            operation = op;
            isNewOperation = true;
        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    // Method to perform calculation
    private void calculateResult() {
        try {
            secondNum = Double.parseDouble(resultText.getText().toString());
            double result = 0;

            switch (operation) {
                case "+":
                    result = firstNum + secondNum;
                    break;
                case "-":
                    result = firstNum - secondNum;
                    break;
                case "×":
                    result = firstNum * secondNum;
                    break;
                case "÷":
                    // Check for division by zero
                    if (secondNum == 0) {
                        resultText.setText("Error");
                        return;
                    }
                    result = firstNum / secondNum;
                    break;
                case "^":
                    result = Math.pow(firstNum, secondNum);
                    break;
                case "yroot":
                    // Check for invalid operations
                    if (secondNum == 0) {
                        resultText.setText("Error");
                        return;
                    }
                    result = Math.pow(firstNum, 1.0 / secondNum);
                    break;
                default:
                    // If no operation selected, just keep the current number
                    result = secondNum;
                    break;
            }

            resultText.setText(formatResult(result));
            firstNum = result;
            isNewOperation = true;

        } catch (Exception e) {
            resultText.setText("Error");
        }
    }

    // Helper method to format results nicely
    private String formatResult(double result) {
        // If result is a whole number, don't show decimal part
        if (result == (int) result) {
            return String.valueOf((int) result);
        } else {
            // For very small numbers close to zero that result from floating point errors
            if (Math.abs(result) < 1e-10) {
                return "0";
            }

            // For scientific notation
            if (Math.abs(result) > 1e9 || Math.abs(result) < 1e-9 && result != 0) {
                return String.format("%e", result);
            }

            return String.valueOf(result);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Save current values
        String currentDisplay = resultText.getText().toString();

        // Recreate the activity when configuration changes
        setContentView(R.layout.activity_main);

        // Reinitialize buttons
        resultText = findViewById(R.id.resultText);
        initializeBasicButtons();
        initializeScientificButtons();

        // Restore the display value
        if (resultText != null) {
            resultText.setText(currentDisplay);
        }
    }
}