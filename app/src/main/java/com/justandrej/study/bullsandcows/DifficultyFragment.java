package com.justandrej.study.bullsandcows;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class DifficultyFragment extends Fragment {
    private ImageButton mStartButton;
    private SeekBar mSeekBar;
    private int mSeekProgress = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_difficulty, container, false);

        mStartButton = v.findViewById(R.id.start_button);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((Animatable) startButton.getDrawable()).start();
//                startButton.setVisibility(View.INVISIBLE);

//                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.open_animator);
//                set.setTarget(startButton);
//                set.start();

                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.open_animator);
                mStartButton.startAnimation(anim);
                Intent intent = GameActivity.newIntent(getContext(), mSeekProgress);
                startActivity(intent);

            }
        });

        mSeekBar = v.findViewById(R.id.seekBar);
        mSeekBar.setProgress(1);
//        mSeekBar.incrementProgressBy(1);
        mSeekBar.setMax(3);
        final TextView seekBarText = v.findViewById(R.id.seekbar_text);

        seekBarText.setText(R.string.easy);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekProgress = progress;
                seekBarText.setText(String.valueOf(progress));

                switch(progress){
                    case 0: seekBarText.setText(R.string.free);
                    break;
                    case 1: seekBarText.setText(R.string.easy);
                    break;
                    case 2: seekBarText.setText(R.string.medium);
                    break;
                    case 3: seekBarText.setText(R.string.hard);
                    break;
                    default: seekBarText.setText(R.string.default_difficulty);
                    break;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return v;
    }
}
