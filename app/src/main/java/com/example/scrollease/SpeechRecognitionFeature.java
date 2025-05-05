package com.example.scrollease;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechRecognitionFeature implements BottomSheetFragment.SpeechRecognitionInterface{

    private Context context;
    private SpeechRecognizer speechRecognizer;
    private Intent intent;
    private final String TRIGGER_WORD = "config";
    private boolean isListening = false;
    private final TextView textView;

    public SpeechRecognitionFeature(Context context, TextView textView){
        this.context = context;
        this.textView = textView;
    }
    @Override
    public void startSpeechRecognition() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.context);
        intent =  new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d("Speech", "Speech Ready!");
                isListening = true;
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
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {
                Log.d("Speech", "Speech Finished");
            }

            @Override
            public void onError(int error) {
                SpeechRecognizerErrorHandler speechErrorHandler = new SpeechRecognizerErrorHandler();
                String errorMessage = speechErrorHandler.getErrorText(error);
                Log.e("Speech", "Error:" + errorMessage);
                Toast.makeText(context.getApplicationContext(), "Error:" + errorMessage, Toast.LENGTH_SHORT).show();

                isListening = false;
                new Handler().postDelayed(() -> startListening(), 300);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()){
                    String spokenText = matches.get(0);
                    Log.d("Speech", "Result:" + spokenText);

                    if (spokenText != null){
                        if(spokenText.toLowerCase().contains(TRIGGER_WORD.toLowerCase())){
                           String command = extractCommand(spokenText);
                           Toast.makeText(context.getApplicationContext(), command, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("Speech", "No trigger word found in: " + spokenText);
                        }
                    }
                }

                startListening();

                isListening = false;
                new Handler().postDelayed(() -> startListening(), 300);

            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                ArrayList<String> match = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (match != null && !match.isEmpty()){
                    String partialSpokenText = match.get(0).toLowerCase();

                    if(partialSpokenText.toLowerCase().contains(TRIGGER_WORD.toLowerCase())){
                        textView.post(() -> textView.setText(partialSpokenText));
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
        if (speechRecognizer != null && !isListening) {
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

//    public String getText(){
//        Toast.makeText(context.getApplicationContext(), command, Toast.LENGTH_SHORT).show();
//        return command;
//    }
}
