package com.twadeclark.brainswiper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class InstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        if (getSupportActionBar() != null) { getSupportActionBar().setTitle("BrainSwiper Instructions"); }
    }

    public void closeButton(View view) {
        finish();
    }
}