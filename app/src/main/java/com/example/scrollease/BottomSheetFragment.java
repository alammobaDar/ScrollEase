package com.example.scrollease;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public interface SpeechRecognitionInterface{
        void startSpeechRecognition();
    }

    private SpeechRecognitionInterface listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (SpeechRecognitionInterface) context;
        }catch (ClassCastException e ){
            throw new ClassCastException(context.toString() +
                    "must implement SpeechRecognitionInterface");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        Button bottomSheetButton = view.findViewById(R.id.BottomFragment);
        bottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Scroll Ease", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        Button speechButton = view.findViewById(R.id.SpeechRecognition);
        speechButton.setOnClickListener(v -> {
            if (listener != null){
                listener.startSpeechRecognition();
                Log.d("Speech","Clicked");
            }
        });


        return view;
    }

}
