package com.uestc.lyreg.customview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.uestc.lyreg.LYArrowLoadingView;
import com.uestc.lyreg.LYCircularCD;
import com.uestc.lyreg.LYCircularRing;
import com.uestc.lyreg.LYDashBoardView;
import com.uestc.lyreg.LYWaveLoadingView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OneActivity extends SlideBackActivity {

    @BindView(R.id.dashboard)
    LYDashBoardView mDashBoard;
    @BindView(R.id.seekbar)
    SeekBar mSeekBar;
    @BindView(R.id.arrowloading)
    LYArrowLoadingView mArrowLoading;
    @BindView(R.id.arrowloadbutton)
    Button mArrowButton;
    @BindView(R.id.waweloading)
    LYWaveLoadingView mWaveLoading;
    @BindView(R.id.circularcd)
    LYCircularCD circularcd;
    @BindView(R.id.circularring)
    LYCircularRing circularring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
//        setSwipeAnyWhere(false);

        ButterKnife.bind(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDashBoard.setPercent(progress);
                mWaveLoading.setPercent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mArrowLoading.start();
            }
        });

        circularcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularcd.isAnimStarted()) {
                    circularcd.stopAnim();
                } else {
                    circularcd.startAnim();
                }
            }
        });

        circularring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(circularring.isAnimRunning()) {
                    circularring.stopAnim();
                } else {
                    circularring.startAnim();
                }
            }
        });
    }
}
