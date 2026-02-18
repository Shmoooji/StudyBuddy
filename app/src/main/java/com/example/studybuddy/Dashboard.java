package com.example.studybuddy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

public class Dashboard extends AppCompatActivity {



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        Button btnLogout = findViewById(R.id.btn_logout);
        Button btnSubject =findViewById(R.id.btn_subjects);
        Button btnProfile = findViewById(R.id.btn_profile);

        btnLogout.setOnClickListener(v->{
            Intent intent = new Intent(Dashboard.this, Login.class);
            startActivity(intent);
        });

        btnSubject.setOnClickListener( v->{
            Intent intent = new Intent(Dashboard.this, Subject.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener( v->{
            Intent intent = new Intent(Dashboard.this, Profile.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.StudyBuddy), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}