package com.twadeclark.brainswiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
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
    private int deckLength = 0;

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
            int delay = (int) (200 - Math.sqrt(Math.abs(velocityY)) );

            if (Math.abs(diffX) > Math.abs(diffY)) { // horizontal swipe
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) { // fast enough swipe
                    if (isFrontOfCardShown) {
                        // do nothing
                    } else {
                        if (diffX > 0) { // swipe right
                            // we are all good
                            tossCardRight( delay);
                        } else { // swipe left
                            // got it wrong, add it to the end
                            Flashcard f;
                            f = new Flashcard(flashcardList.get(currentCardIndex).getFront(),flashcardList.get(currentCardIndex).getBack());
                            flashcardList.add(f);
                            tossCardLeft(delay);
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
                            flipCardOver (delay);
                        }
                    }
                }
            }

            return true;
        }
    }

    private void tossCardLeft (int delay) {
        View cardView = binding.constraintLayoutFlipper;
        float screenWidth = binding.constraintLayoutFlipper.getWidth();

        cardView.animate()
                .translationX(-screenWidth)
                .setDuration(delay)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cardView.setTranslationX(0); // Reset position after animation
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .start();
    }

    private void tossCardRight (int delay) {
        View cardView = binding.constraintLayoutFlipper;
        float screenWidth = binding.constraintLayoutFlipper.getWidth();

        cardView.animate()
                .translationX(screenWidth)
                .setDuration(delay)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cardView.setTranslationX(0); // Reset position after animation
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .start();
    }


    private void flipCardOver (int delay) {
        // delay = bigger is slower
        ObjectAnimator firstHalf = ObjectAnimator.ofFloat(binding.constraintLayoutFlipper, "rotationX", 0f, 90f);
        firstHalf.setDuration(delay); // duration in milliseconds

        ObjectAnimator secondHalf = ObjectAnimator.ofFloat(binding.constraintLayoutFlipper, "rotationX", 270f, 360f);
        secondHalf.setDuration(delay);

        firstHalf.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                showCurrentFlashcardBack();
            }
        });

        secondHalf.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.constraintLayoutFlipper.setRotationX(0);
            }
        });

        AnimatorSet cardFlip = new AnimatorSet();
        cardFlip.playSequentially(firstHalf, secondHalf);
        cardFlip.start();
    }

    private void showCurrentFlashcardFront() {
        binding.constraintLayoutInterior.setBackgroundColor((ContextCompat.getColor(this, R.color.colorCardFront)));
        binding.flashcardTextView.setTextColor((ContextCompat.getColor(this, R.color.colorCardFrontText)));
        if (currentCardIndex >= deckLength) {
            binding.constraintLayoutFlipper.setBackgroundColor((ContextCompat.getColor(this, R.color.colorCardRed)));
        } else {
            binding.constraintLayoutFlipper.setBackgroundColor((ContextCompat.getColor(this, R.color.colorCardGreen)));
        }

        isFrontOfCardShown = true;
        String s = flashcardList.get(currentCardIndex).getFront();
        binding.flashcardTextView.setText(s);
        binding.statusQA.setText("Q:");
        binding.countDisplay.setText("" + (currentCardIndex + 1) + "/" + flashcardList.size());
    }

    private void showCurrentFlashcardBack() {
        binding.constraintLayoutInterior.setBackgroundColor((ContextCompat.getColor(this, R.color.colorCardBack)));
        binding.flashcardTextView.setTextColor((ContextCompat.getColor(this, R.color.colorCardBackText)));
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

        deckLength = flashcardList.size();
        Collections.shuffle(flashcardList);
    }

}
