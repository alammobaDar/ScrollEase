package com.example.scrollease;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class OverlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.overlay_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomSheetFragment bottomSheetFragment =  new BottomSheetFragment();

        // pass the data from getCommandText to BSF
        Bundle args = new Bundle();
        args.putString("command", getIntent().getStringExtra("command"));
        bottomSheetFragment.setArguments(args);

        bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheet");


        // TODO: this is risky because the BSF is still null,
        //  this can be solve if i just put this into the bsf itself
        //  and then do this to bsf onDismiss(); to dismiss the activity itself (OverlayActivity).
        //  After that, we can remove this
        // ensure to dismiss the overlay activity as the bsf is finished, also ensure bsf is not null.
        Objects.requireNonNull(bottomSheetFragment.getDialog()).setOnDismissListener(dialog -> finish());
    }
}