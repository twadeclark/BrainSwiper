package com.twadeclark.brainswiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.twadeclark.brainswiper.database.Deck;
import com.twadeclark.brainswiper.database.DeckViewModel;
import com.twadeclark.brainswiper.database.Flashcard;
import com.twadeclark.brainswiper.databinding.ActivityFlashcardBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {

    private static final String blankCardText = "(blank)";
    private List<Flashcard> flashcardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private boolean isFrontOfCardShown = true;
    private @NonNull ActivityFlashcardBinding binding;
    private GestureDetector gestureDetector;
    private float flipDelay = 100.0f;
    private float prevVel = -1.0f;
    private int deckLength = 0;

    private int deckIdFromIntent;
    private String deckNameFromIntent;
    private String deckContentsFromIntent;
    private DeckViewModel deckViewModel;
    private Deck thisDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlashcardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        deckIdFromIntent = getIntent().getIntExtra("deckId", -1);

        deckViewModel.getDeckById(deckIdFromIntent).observe(this, deck -> {
            if (deck != null) {
                thisDeck = deck;
                deckNameFromIntent = thisDeck.deckName;
                deckContentsFromIntent = thisDeck.deckContents;

                setupGestureDetector();
                loadFlashCards();
                showCurrentFlashcardFront();
                setupClickableDeckName();
            } else {
                finish();
            }
        });

    }

    private void setupClickableDeckName() {
        TextView tvDeckName = findViewById(R.id.titleTextView);
        SpannableString content = new SpannableString(deckNameFromIntent);
        content.setSpan(new UnderlineSpan(), 0, deckNameFromIntent.length(), 0);
        tvDeckName.setText(content);

        tvDeckName.setClickable(true);
        tvDeckName.setFocusable(true);

        tvDeckName.setOnClickListener(v -> {
            Intent intent = new Intent(FlashcardActivity.this, DeckEditor.class);

            intent.putExtra("deckId", deckIdFromIntent);
            intent.putExtra("deckName", deckNameFromIntent);
            intent.putExtra("deckContents", deckContentsFromIntent);

            startActivity(intent);
        });

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
        binding.flashcardTextView.setText(flashcardList.get(currentCardIndex).getBack());
        binding.statusQA.setText("Q: "+ flashcardList.get(currentCardIndex).getFront() + "\n" + "A:");

    }

    private void loadFlashCards() {
        binding.titleTextView.setText(deckNameFromIntent);

        for (String line: deckContentsFromIntent.split("\n")) {
            String[] parsed = Arrays.stream(line.split(",", 2))
                    .map(String::trim)
                    .toArray(String[]::new);

            String front = parsed[0].length() > 0 ? parsed[0] : blankCardText;
            String back = parsed.length > 1 && parsed[1].length() > 0 ? parsed[1] : blankCardText;

            if (front != blankCardText || back != blankCardText)
            {
                flashcardList.add(new Flashcard(front, back));
            }
        }

        if (flashcardList.isEmpty()) {
            Flashcard f = new Flashcard("(empty deck)","(empty deck)");
            flashcardList.add(f);
        }

        deckLength = flashcardList.size();
        Collections.shuffle(flashcardList);
    }

}
