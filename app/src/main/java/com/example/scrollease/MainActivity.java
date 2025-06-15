package com.example.scrollease;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.scrollease.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SpeechRecognitionFeature.SpeechRecognitionInterface {

    private ActivityMainBinding binding;
    private SpeechRecognitionFeature speechRecognitionFeature;

    private BottomSheetFragment bottomSheetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Replace findViewById with binding
        binding.ToButtonFragment.setOnClickListener(v -> {
            BottomSheetFragment bsf = new BottomSheetFragment();
            bsf.show(getSupportFragmentManager(), "Bottom Sheet Fragment");
        });

        NotificationFeature nf = new NotificationFeature();
        nf.SRFPermission(this);

        binding.notificationButton.setOnClickListener(v ->
                nf.persistentNotification(MainActivity.this, v)
        );

        bottomSheetFragment = new BottomSheetFragment();
        speechRecognitionFeature = new SpeechRecognitionFeature(this, bottomSheetFragment);

    }

    @Override
    public void startSpeechRecognition() {
        if (speechRecognitionFeature != null) {
            speechRecognitionFeature.startSpeechRecognition();
        }
    }

    public void setSpeechRecognitionListener(SpeechRecognitionFeature.SpeechResultListener listener){
        speechRecognitionFeature.resultListener = listener;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null; // Cleanup
    }

}