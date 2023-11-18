package com.twadeclark.brainswiper;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.twadeclark.brainswiper.databinding.ActivityDeckEditorBinding;

public class DeckEditor extends AppCompatActivity {

    private ActivityDeckEditorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDeckEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();
    }

    public void onCancelClicked(View view) {
        // Finish the activity and return to the previous screen
        finish();
    }

    private void initUI() {
    }

}