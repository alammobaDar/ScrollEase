package com.example.scrollease;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.scrollease.databinding.FragmentBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment implements SpeechRecognitionFeature.SpeechResultListener {

    @Override
    public void onResults(String results) {
    }

    // TODO: The partial text is not being past on overrided onPartialResults
    @Override
    public void onPartialResults(String partialResults) {
        Log.d("Speech", "partial "+ partialResults);
        if (getActivity() != null){

            getActivity().runOnUiThread(() ->
                    binding.textDisplay.setText(partialResults));
        }
    }


    private SpeechRecognitionFeature.SpeechRecognitionInterface listener;

    private FragmentBottomSheetBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SpeechRecognitionFeature.SpeechRecognitionInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement SpeechRecognitionInterface");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false);
        setupClickListeners();
        return binding.getRoot();
    }

    private void setupClickListeners() {
        binding.backButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Scroll Ease", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        binding.SpeechRecognitionButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.startSpeechRecognition();
                Log.d("Speech", "Clicked");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
