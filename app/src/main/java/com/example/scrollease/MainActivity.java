package com.example.scrollease;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements BottomSheetFragment.SpeechRecognitionInterface{

    SpeechRecognitionFeature speechRecognitionFeature;
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

        Button toOpenBottomSheet = findViewById(R.id.ToButtonFragment);
        toOpenBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment bsf = new BottomSheetFragment();
                bsf.show(getSupportFragmentManager(), "Bottom Sheet Fragment");
            }
        });

        NotificationFeature nf = new NotificationFeature();
        nf.SRFPermission(this);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button notifButton = findViewById(R.id.notification_button);
        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nf.persistentNotification(MainActivity.this, v);
            }
        });
        speechRecognitionFeature = new SpeechRecognitionFeature(this);

    }

    @Override
    public void startSpeechRecognition() {
        if(speechRecognitionFeature != null){
            speechRecognitionFeature.startSpeechRecognition();
        }
    }



}