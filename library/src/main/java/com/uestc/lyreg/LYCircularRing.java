package com.uestc.lyreg;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2016/7/12.
 *
 * @Author lyreg
 */
public class LYCircularRing extends View {

    private int mRingColor;
    private Paint mPaint;

    private float mWidth = 0f;
    private float mHeight = 0f;
    private float mPadding = 0f;
    private float mStartAngle = 0f;

    public LYCircularRing(Context context) {
        this(context, null);
    }

    public LYCircularRing(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LYCircularRing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.LYCircularRing);
        mRingColor = arr.getColor(R.styleable.LYCircularRing_RingColor, Color.parseColor("#0277BD"));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(mRingColor);

        mPadding = 5;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = widthSize;
        mHeight = heightSize;

        if(mWidth > mHeight) {
            mWidth = mHeight;
        } else {
            mHeight = mWidth;
        }
        setMeasuredDimension((int)mWidth, (int)mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(mPadding);
        canvas.drawCircle(mWidth/2, mHeight/2, mWidth/2 - mPadding, mPaint);

        mPaint.setColor(mRingColor);
        canvas.drawArc(new RectF(mPadding, mPadding, mWidth-mPadding, mHeight-mPadding)
                , mStartAngle, 100, false, mPaint);

        canvas.restore();
    }

    public void startAnim() {
        stopAnim();
        startViewAnim(0f, 1f, 1200   );
    }

    public void stopAnim() {
        if(mValueAnimator != null) {
            clearAnimation();
            mValueAnimator.setRepeatCount(1);
            mValueAnimator.cancel();
            mValueAnimator.end();
        }
    }

    public boolean isAnimRunning() {
        if(mValueAnimator != null) {
            return mValueAnimator.isRunning();
        }
        return false;
    }

    ValueAnimator mValueAnimator;

    private ValueAnimator startViewAnim(float start, float end, long time) {
        mValueAnimator = ValueAnimator.ofFloat(start, end);

        mValueAnimator.setDuration(time);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);

        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mStartAngle = 360 * value;

                invalidate();
            }
        });

        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        if(!mValueAnimator.isRunning()) {
            mValueAnimator.start();
        }

        return mValueAnimator;
    }
}
