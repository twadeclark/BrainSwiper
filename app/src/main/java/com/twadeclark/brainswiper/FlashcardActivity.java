package com.twadeclark.brainswiper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

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
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {

    private List<Flashcard> flashcardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private boolean isFrontOfCardShown = true;
    private @NonNull ActivityFlashcardBinding binding;
    private GestureDetector gestureDetector;

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

//        TextView textView = binding.flashcardTextView;
        ConstraintLayout constraintLayoutFlashcard = binding.constraintLayoutFlashcard;

        constraintLayoutFlashcard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d("FlashcardActivity", "+ onTouch v.toString(): " + v.toString());
                Log.d("FlashcardActivity", "+ onTouch event.toString(): " + event.toString());


                boolean retVal = gestureDetector.onTouchEvent(event);
                Log.d("FlashcardActivity", "+ onTouch retVal: " + retVal);
                binding.titleTextView.setText("retVal:" + retVal);

//                return retVal;
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


            Log.d("FlashcardActivity", "+ onFling diffY: " + diffY);
            Log.d("FlashcardActivity", "+ onFling diffX: " + diffX);
            binding.titleTextView.setText("diffX:" + diffX + " diffY:" + diffY);



            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    return true;
                }
            }
            return false;
        }

        public void onSwipeRight() {
            // Handle right swipe
//            currentCardIndex++;
//            showCurrentFlashcardBack();

            Log.d("FlashcardActivity", "+ onSwipeRight ");

            handleSwipe(0,0);

        }

        public void onSwipeLeft() {
            // Handle left swipe
//            currentCardIndex++;
//            showCurrentFlashcardFront();


            Log.d("FlashcardActivity", "+ onSwipeLeft ");

            handleSwipe(0,0);


        }
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

    private void handleSwipe(float velocityX, float velocityY) {
        if (isFrontOfCardShown) {
            // Show back of the card
            isFrontOfCardShown = false;
            showCurrentFlashcardBack();
        } else {
            // Record the user's response based on swipe direction
            // Move to the next card
            isFrontOfCardShown = true;
            currentCardIndex++;
            showCurrentFlashcardFront();
        }
    }
}
