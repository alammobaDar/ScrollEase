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
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpeechRecognitionFeature extends Service{

    Context context;
    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private final String TRIGGER_WORD = "config";
    private boolean isListening = false;
    NotificationFeature notificationFeature = new NotificationFeature();

    // TODO: Make a state machine flow

    private final int STATE_IDLE = 0;
    private final int STATE_LISTENING = 1;
    private final int STATE_TRIGGERED = 2;

    private int currentState = STATE_IDLE;

    // TODO: add condition to OnResult that if the word is not yet Triggered, return Nothing
    // TODO: add condition on OnPartialResult if the TW has been said, then update current state
    // TODO: after that, add the extract commands on OnResult to activate commands

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
//                Toast.makeText(context.getApplicationContext(), "Error:" + errorMessage, Toast.LENGTH_SHORT).show();

                currentState = STATE_IDLE;
                new Handler().postDelayed(() -> startListening(), 500);
            }

            @Override
            public void onResults(Bundle results) {
                if (currentState != STATE_TRIGGERED){
                    currentState = STATE_IDLE;
                    startListening();
                    return;
                }

                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()){
                    String spokenText = matches.get(0);
                    Log.d("Speech", "Result:" + spokenText);

                    if (spokenText.toLowerCase().contains(TRIGGER_WORD)){
                        String command = extractCommand(spokenText);

                        Log.d("Speech", "Command: "+ command);
                    }

                    currentState = STATE_IDLE;
                    new Handler().postDelayed(() -> startListening(), 500);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> match = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (match != null && !match.isEmpty()){
                    String partialSpokenText = match.get(0).toLowerCase();

                    Log.d("Speech", partialSpokenText);
                    if (partialSpokenText.toLowerCase().contains(TRIGGER_WORD) && currentState == STATE_LISTENING){
                        currentState = STATE_TRIGGERED;
                        Log.d("Speech", "Trigger word");

//                        stopListening();
                    }
                }

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });


        startListening();

    }

    private void startListening(){
        if (speechRecognizer != null && currentState == STATE_IDLE) {
            currentState = STATE_LISTENING;
            speechRecognizer.startListening(intent);
        }
    }

    private void stopListening(){
        if(isListening){
            speechRecognizer.stopListening();
        }
    }

    private String extractCommand(String text) {
        // after the word config, it will only process 2 next word
        String[] arr_text = text.split("\\s+");
//        boolean isTW = Arrays.asList(arr_text).contains(TRIGGER_WORD);

        int index = -1;
        StringBuilder command = new StringBuilder();
        for (int i = 0; i < arr_text.length; i++){
            if (Objects.equals(arr_text[i], TRIGGER_WORD)){
                index = i;
                break;

            }
        }
        for (int i = index+1; i < arr_text.length; i++){
            command.append(" ").append(arr_text[i]);
        }



        return command.toString().strip();
    }
}
