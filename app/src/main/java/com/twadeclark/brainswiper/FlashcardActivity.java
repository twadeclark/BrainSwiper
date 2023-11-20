package com.twadeclark.brainswiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twadeclark.brainswiper.database.Flashcard;
import com.twadeclark.brainswiper.databinding.ActivityFlashcardBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {

    private List<Flashcard> flashcardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private boolean isFrontOfCardShown = true;
    private @NonNull ActivityFlashcardBinding binding;
    private GestureDetector gestureDetector;
    private float flipDelay = 100.0f;
    private float prevVel = -1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlashcardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupGestureDetector();
        loadFlashCards();
        showCurrentFlashcardFront();
    }

    private void setupGestureDetector() {
        gestureDetector = new GestureDetector(this, new GestureListener());

        ConstraintLayout constraintLayoutFlashcard = binding.constraintLayoutFlashcard;

        constraintLayoutFlashcard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean retVal = gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();

            if (Math.abs(diffX) > Math.abs(diffY)) { // horizontal swipe
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) { // fast enough swipe
                    if (isFrontOfCardShown) {
                        // do nothing
                    } else {
                        if (diffX > 0) { // swipe right
                            // we are all good
                        } else { // swipe left
                            // got it wrong, add it to the end
                            Flashcard f;
                            f = new Flashcard(flashcardList.get(currentCardIndex).getFront(),flashcardList.get(currentCardIndex).getBack());
                            flashcardList.add(f);
                        }

                        currentCardIndex++;

                        if (currentCardIndex >= flashcardList.size()) {
                            currentCardIndex = 0;
                            flashcardList.clear();
                            loadFlashCards();
                        }

                        showCurrentFlashcardFront();
                    }

                    return true;
                }
            } else { // vertical swipe
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) { // fast enough swipe
                    if (diffY < 0) { // swipe up
                        if (isFrontOfCardShown) {
                            double delay = (200 - Math.sqrt(Math.abs(velocityY)) );
                            flipCardOver ((int)delay);
                        }
                    }
                }
            }

            return true;
        }
    }

    private void flipCardOver (int delay) {
        // delay = bigger is slower
        ObjectAnimator firstHalf = ObjectAnimator.ofFloat(binding.constraintLayoutFlipper, "rotationX", 0f, 90f);
        firstHalf.setDuration(delay); // duration in milliseconds

        ObjectAnimator secondHalf = ObjectAnimator.ofFloat(binding.constraintLayoutFlipper, "rotationX", 90f, 180f);
        secondHalf.setDuration(delay);

        firstHalf.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Change the content of the card to show the other side
                showCurrentFlashcardBack();

            }
        });

        secondHalf.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.constraintLayoutFlashcard.setRotationX(0);

            }
        });

        AnimatorSet cardFlip = new AnimatorSet();
        cardFlip.playSequentially(firstHalf, secondHalf);
        cardFlip.start();

    }

    private void showCurrentFlashcardFront() {
        isFrontOfCardShown = true;
        String s = flashcardList.get(currentCardIndex).getFront();
        binding.flashcardTextView.setText(s);
        binding.statusQA.setText("Q:");
    }

    private void showCurrentFlashcardBack() {
        isFrontOfCardShown = false;
        String s = flashcardList.get(currentCardIndex).getBack();
        binding.flashcardTextView.setText(s);
        binding.statusQA.setText("A:");
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

        Collections.shuffle(flashcardList);
    }

}
