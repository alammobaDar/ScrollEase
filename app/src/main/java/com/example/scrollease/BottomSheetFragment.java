package com.example.scrollease;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.scrollease.databinding.FragmentBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {


    private final Handler handler = new Handler(Looper.getMainLooper());

    private FragmentBottomSheetBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false); // Necessary for fragments, inflates the fragment so it can be display
        String command = getArguments() != null ? getArguments().getString("command"): null ; // get the data from Bundle
        if (command != null){
            binding.textDisplay.setText(command);
        }
        // this dismiss the fragment after 5 seconds
        handler.postDelayed(() -> {
            if(isAdded()) {
                dismiss();
            }
        }, 5000);

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
        handler.removeCallbacksAndMessages(null); // Prevent memory leaks

    }
}
