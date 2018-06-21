package com.justandrej.study.bullsandcows;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String IS_WIN = "Won?", USER_SCORE = "User score";
    private TextView mWinLose, mUserScore;
    private Button mHighButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWinLose = findViewById(R.id.win_lose);
        mUserScore = findViewById(R.id.score_is);
        mHighButton = findViewById(R.id.high_scores);

        mHighButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = HighScoresActivity.newIntent(MainActivity.this);
                startActivity(intent);
            }
        });

        if(getIntent().getIntExtra(USER_SCORE, -2) != -2){
            if(getIntent().getBooleanExtra(IS_WIN, false)){
                mWinLose.setText(R.string.you_win);
                mUserScore.setText((getString(R.string.your_score) + " " + String.valueOf(getIntent().getIntExtra(USER_SCORE, 0))));
            } else {
                mWinLose.setText(R.string.you_lose);
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.main_fragment_container);
//
        if(fragment == null){
            fragment = new StartFragment();
            fm.beginTransaction().add(R.id.main_fragment_container, fragment).commit();
        }
    }

    public static Intent newIntent(Context packageContext, boolean iswin, int score){
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(IS_WIN, iswin);
        intent.putExtra(USER_SCORE, score);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

//    public void showDifficult(){
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.main_fragment_container);
////
//        if(fragment == null){
//            fragment = new DifficultyFragment();
//            fm.beginTransaction().add(R.id.main_fragment_container, fragment).commit();
//        }
//    }
}
