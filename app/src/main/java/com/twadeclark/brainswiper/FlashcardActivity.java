package com.twadeclark.brainswiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
    private @NonNull ActivityFlashcardBinding binding;
    private GestureDetector gestureDetector;
    private DeckViewModel deckViewModel;


    private int deckIdFromIntent;
    private Deck thisDeck;
    private int deckLength = 0;
//    private List<Flashcard> flashcardList = new ArrayList<>();
//    private int currentCardIndex = 0;
//    private boolean isFrontOfCardShown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) { getSupportActionBar().hide(); }
        super.onCreate(savedInstanceState);
        binding = ActivityFlashcardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        deckIdFromIntent = getIntent().getIntExtra("deckId", -1);

        long currentTime = System.currentTimeMillis();
        deckViewModel.updateLastAccessed(deckIdFromIntent, currentTime);

        deckViewModel.getDeckById(deckIdFromIntent).observe(this, deck -> {
            if (deck != null) {
                thisDeck = deck;

                setupGestureDetector();

                if (savedInstanceState == null) {
                    loadFlashCards();
                }


                showCurrentFlashcardFront();
                setupClickableDeckName();

                binding.titleTextView.setText(thisDeck.getDeckName());
            } else {
                finish();
            }
        });

    }

    private void setupClickableDeckName() {
        TextView tvDeckName = findViewById(R.id.titleTextView);
        SpannableString content = new SpannableString(thisDeck.getDeckName());
        content.setSpan(new UnderlineSpan(), 0, thisDeck.getDeckName().length(), 0);
        tvDeckName.setText(content);

        tvDeckName.setClickable(true);
        tvDeckName.setFocusable(true);

        tvDeckName.setOnClickListener(v -> {
            Intent intent = new Intent(FlashcardActivity.this, DeckEditor.class);

            intent.putExtra("deckId", deckIdFromIntent);
            intent.putExtra("deckName", thisDeck.getDeckName());
            intent.putExtra("deckContents", thisDeck.getDeckContents());

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
                    if (deckViewModel.getFrontOfCardShown()) {
                        // do nothing
                    } else {
                        if (diffX > 0) { // swipe right
                            // we are all good
                            tossCardRight( delay);
                        } else { // swipe left
                            // got it wrong, add it to the end
                            Flashcard f;
                            f = new Flashcard(deckViewModel.getFlashcardList().get(deckViewModel.getCurrentCardIndex()).getFront(),deckViewModel.getFlashcardList().get(deckViewModel.getCurrentCardIndex()).getBack());
                            deckViewModel.getFlashcardList().add(f);
                            tossCardLeft(delay);
                        }

                        deckViewModel.incrementCurrentCardIndex();

                        if (deckViewModel.getCurrentCardIndex() >= deckViewModel.getFlashcardList().size()) {
                            deckViewModel.setCurrentCardIndex(0); // currentCardIndex = 0;
                            deckViewModel.getFlashcardList().clear();
                            loadFlashCards();
                        }

                        showCurrentFlashcardFront();
                    }

                    return true;
                }
            } else { // vertical swipe
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) { // fast enough swipe
                    if (diffY < 0) { // swipe up
                        if (deckViewModel.getFrontOfCardShown()) {
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
        deckViewModel.setFrontOfCardShown(true);

        if (deckViewModel.getCurrentCardIndex() >= deckLength) {
            binding.constraintLayoutFlipper.setBackgroundColor((ContextCompat.getColor(this, R.color.colorCardRed)));
        } else {
            binding.constraintLayoutFlipper.setBackgroundColor((ContextCompat.getColor(this, R.color.colorCardGreen)));
        }

        binding.statusQA.setText("Q:");
        String countDisplayText = "" + (deckViewModel.getCurrentCardIndex() + 1) + "/" + deckViewModel.getFlashcardList().size();
        binding.countDisplay.setText(countDisplayText);

        //
        binding.flashcardTextView.setText(cardFrontReversedIfNeeded());
    }

    private String cardFrontReversedIfNeeded() {
        return binding.checkBoxReverse.isChecked() ? deckViewModel.getFlashcardList().get(deckViewModel.getCurrentCardIndex()).getBack() : deckViewModel.getFlashcardList().get(deckViewModel.getCurrentCardIndex()).getFront();
    }

    private String cardBackReversedIfNeeded() {
        return binding.checkBoxReverse.isChecked() ? deckViewModel.getFlashcardList().get(deckViewModel.getCurrentCardIndex()).getFront() : deckViewModel.getFlashcardList().get(deckViewModel.getCurrentCardIndex()).getBack();
    }

    private void showCurrentFlashcardBack() {
        binding.constraintLayoutInterior.setBackgroundColor((ContextCompat.getColor(this, R.color.colorCardBack)));
        binding.flashcardTextView.setTextColor((ContextCompat.getColor(this, R.color.colorCardBackText)));
        deckViewModel.setFrontOfCardShown(false);

        String statusQAText = "Q: " + cardFrontReversedIfNeeded() + "\n" + "A:";
        binding.statusQA.setText(statusQAText);
        binding.flashcardTextView.setText(cardBackReversedIfNeeded());
    }

    private void loadFlashCards() {
        for (String line: thisDeck.getDeckContents().split("\n")) {
            String[] parsed = Arrays.stream(line.split(",", 2))
                    .map(String::trim)
                    .toArray(String[]::new);

            String front = parsed[0].length() > 0 ? parsed[0] : blankCardText;
            String back = parsed.length > 1 && parsed[1].length() > 0 ? parsed[1] : blankCardText;

            if (front != blankCardText || back != blankCardText)
            {
                deckViewModel.getFlashcardList().add(new Flashcard(front, back));
            }
        }

        if (deckViewModel.getFlashcardList().isEmpty()) {
            Flashcard f = new Flashcard("(empty deck)","(empty deck)");
            deckViewModel.getFlashcardList().add(f);
        }

        deckLength = deckViewModel.getFlashcardList().size();
        Collections.shuffle(deckViewModel.getFlashcardList());
    }

}
