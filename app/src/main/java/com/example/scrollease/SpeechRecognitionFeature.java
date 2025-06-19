package com.example.scrollease;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;
import android.app.Service;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechRecognitionFeature extends Service{

    Context context;
    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private final String TRIGGER_WORD = "config";
    private boolean isListening = false;
    NotificationFeature notificationFeature = new NotificationFeature();

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        startSpeechRecognition();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        startForeground(1, notificationFeature.persistentNotification(this));
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    public void startSpeechRecognition() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.context);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}
            @Override
            public void onBeginningOfSpeech() {}
            @Override
            public void onRmsChanged(float rmsdB) {}
            @Override
            public void onBufferReceived(byte[] buffer) {}
            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {
                SpeechRecognizerErrorHandler speechErrorHandler = new SpeechRecognizerErrorHandler();
                String errorMessage = speechErrorHandler.getErrorText(error);
                Log.e("Speech", "Error:" + errorMessage);
                Toast.makeText(context.getApplicationContext(), "Error:" + errorMessage, Toast.LENGTH_SHORT).show();

                isListening = false;
//                new Handler().postDelayed(() -> startListening(), 500);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()){
                    String spokenText = matches.get(0);
                    Log.d("Speech", "Result:" + spokenText);

                    isListening = false;
//                    startListening();
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> match = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (match != null && !match.isEmpty()){
                    String partialSpokenText = match.get(0).toLowerCase();

                    Log.d("Speech", partialSpokenText);
//                    if (resultListener != null){
//                        resultListener.onPartialResults(partialSpokenText);
//                    }
                }

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        startListening();
    }

    private void startListening(){
        if (speechRecognizer != null && isListening == false) {
            speechRecognizer.startListening(intent);
            isListening = true;
        }
    }

    private void stopListening(){
        if(isListening){
            speechRecognizer.stopListening();
            isListening = false;
        }
    }

    private String extractCommand(String text){

        return text;
    }
}
