package com.example.eventhandlingintentsdebugging;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class InputActivity extends AppCompatActivity {

    private static final String TAG = "InputActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Log.d(TAG, "onCreate started");

        EditText editMessage = findViewById(R.id.editMessage);
        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String message = editMessage.getText().toString();
            Log.d(TAG, "Send button clicked, message: " + message);
            Intent intent = new Intent(InputActivity.this, EchoActivity.class);
            intent.putExtra("MESSAGE", message);
            startActivity(intent);
        });
    }
}
