package com.example.eventhandlingintentsdebugging;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EchoActivity extends AppCompatActivity {

    private static final String TAG = "EchoActivityLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo);

        Intent intent = getIntent();
        String message = intent.getStringExtra("MESSAGE");
        Log.d(TAG, "onCreate started, received: " + message);

        TextView txtEcho = findViewById(R.id.txtEcho);
        txtEcho.setText(message);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });
    }
}
