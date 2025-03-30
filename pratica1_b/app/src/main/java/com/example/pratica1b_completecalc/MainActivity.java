package com.example.pratica1b_completecalc;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText display;
    boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initDisplay();
        initButtons();
    }

    protected void initDisplay() {
        display = (EditText) findViewById(R.id.display);
    }

    protected void initButtons() {
        // Operators and digits.
        ViewGroup buttons = (ViewGroup) findViewById(R.id.buttons);
        for (int i = 0; i < buttons.getChildCount(); i++) {
            Button button = (Button) buttons.getChildAt(i);
            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear if an exception text was displayed.
                    if(answered)
                        display.setText("");
                    answered = false;

                    display.getText().append(button.getText());
                }
            });
        }

        // Clear button.
        Button undo = (Button) findViewById(R.id.bttnUndo);
        undo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(display.getText().length() == 0)
                    return;

                // Clear if an exception text was displayed.
                if(answered)
                    display.setText("");
                answered = false;

                display.setText(display.getText().subSequence(0, display.getText().length()-1));
            }
        });

        // Clear button.
        Button clear = (Button) findViewById(R.id.bttnC);
        clear.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
                answered = false;
            }
        });

        // Equals button.
        Button eq = (Button) findViewById(R.id.bttnEq);
        eq.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                answered = true;

                Pattern regexPattern = Pattern.compile("(-?\\d+(?:\\.\\d+)?)\\s*([-+/*])\\s*(-?\\d+(?:\\.\\d+)?)");
                Matcher regexMatcher = regexPattern.matcher(display.getText().toString());

                String[] math = new String[3];
                if (regexMatcher.matches()) {
                    math[0] = regexMatcher.group(1);
                    math[1] = regexMatcher.group(2);
                    math[2] = regexMatcher.group(3);
                } else {
                    display.setText("Operação Inválida!");
                    return;
                }

                switch(math[1]) {
                    case "+":
                        display.setText("" + (Double.parseDouble(math[0]) + Double.parseDouble(math[2])));
                        break;
                    case "-":
                        display.setText("" + (Double.parseDouble(math[0]) - Double.parseDouble(math[2])));
                        break;
                    case "*":
                        display.setText("" + (Double.parseDouble(math[0]) * Double.parseDouble(math[2])));
                        break;
                    case "/":
                        if(Double.parseDouble(math[2]) == 0)
                            display.setText("Operação Impossível: divisão por zero.");
                        else
                            display.setText("" + (Double.parseDouble(math[0]) / Double.parseDouble(math[2])));
                        break;
                }
            }
        });
    }
}