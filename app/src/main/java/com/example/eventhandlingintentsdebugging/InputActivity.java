package com.example.eventhandlingintentsdebugging;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        EditText editMessage = findViewById(R.id.editMessage);
        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String message = editMessage.getText().toString();
            Intent intent = new Intent(InputActivity.this, EchoActivity.class);
            intent.putExtra("MESSAGE", message);
            startActivity(intent);
        });
    }
}
