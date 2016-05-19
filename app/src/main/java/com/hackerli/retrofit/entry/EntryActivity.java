package com.hackerli.retrofit.entry;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.hackerli.retrofit.R;
import com.hackerli.retrofit.main.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EntryActivity extends AppCompatActivity {

    @Bind(R.id.text_label)
    TextView textLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);
        startAnim();
    }

    private void startAnim() {
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(textLabel, "translationY", 150f, 0f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(textLabel, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(moveUp).with(fadeInOut);
        animatorSet.setDuration(2000);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(EntryActivity.this, MainActivity.class);
                startActivity(intent);
                EntryActivity.this.finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }
}
