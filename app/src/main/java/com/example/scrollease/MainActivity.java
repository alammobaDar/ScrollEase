package com.example.scrollease;


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

    }



    public void SpeechRecognitionFeature(View v){
        startSpeechRecognition();

    }

    @Override
    public void startSpeechRecognition() {
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent intent =  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d("Speech", "Speech Ready!");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d("Speech", "Speech Started");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                Log.d("Speech", "Sound level: " + rmsdB);
            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                Log.d("Speech", "Speech Finished");
            }

            @Override
            public void onError(int error) {
                SpeechRecognizerErrorHandler speechErrorHandler = new SpeechRecognizerErrorHandler();
                String errorMessage = speechErrorHandler.getErrorText(error);
                Log.e("Speech", "Error:" + errorMessage);
                Toast.makeText(getApplicationContext(), "Error:" + errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()){
                    String spokenText = matches.get(0);
                    Log.d("Speech", "Result:" + spokenText);
                    Toast.makeText(getApplicationContext(), spokenText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        speechRecognizer.startListening(intent);
    }
}