package com.twadeclark.brainswiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.twadeclark.brainswiper.database.Flashcard;
import com.twadeclark.brainswiper.databinding.ActivityFlashcardBinding;

import java.util.ArrayList;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {

    private List<Flashcard> flashcardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private boolean isFrontOfCardShown = true;
    private @NonNull ActivityFlashcardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlashcardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupGestureDetector();
        loadFlashCards();
        showCurrentFlashcardFront();
    }

    private void loadFlashCards() {
        String deckName = getIntent().getStringExtra("deckName");
        binding.titleTextView.setText(deckName);

        String deckContents = getIntent().getStringExtra("deckContents");
        for (String line:deckContents.split("\n")) {
            String[] parsed = line.split(",", 2);
            Flashcard f;

            if (parsed.length == 2) {
                f = new Flashcard(parsed[0].trim(),parsed[1].trim());
            } else {
                f = new Flashcard(line,"(blank)");
            }

            flashcardList.add(f);
        }

        if (flashcardList.isEmpty()) {
            Flashcard f = new Flashcard("empty","deck");
            flashcardList.add(f);
        }
    }

    private void showCurrentFlashcardFront() {
        if (currentCardIndex >= flashcardList.size()) {
            currentCardIndex = 0;
        }

        String s = flashcardList.get(currentCardIndex).getFront();
        binding.flashcardTextView.setText(s);
    }

    private void showCurrentFlashcardBack() {
        if (currentCardIndex >= flashcardList.size()) {
            currentCardIndex = 0;
        }

        binding.flashcardTextView.setText(flashcardList.get(currentCardIndex).getBack());
    }

    private void setupGestureDetector() {
        GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // Determine swipe direction and toggle card or move to next card
                handleSwipe(velocityX, velocityY);
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        // Attach the gesture detector to your view component
    }

    private void handleSwipe(float velocityX, float velocityY) {
        if (isFrontOfCardShown) {
            // Show back of the card
            showCurrentFlashcardBack();
        } else {
            // Record the user's response based on swipe direction
            // Move to the next card
            currentCardIndex++;
            showCurrentFlashcardFront();
        }
    }
}
