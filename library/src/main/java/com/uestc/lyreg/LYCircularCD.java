package com.uestc.lyreg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by Administrator on 2016/6/30.
 *
 * @Author lyreg
 */
public class LYCircularCD extends View {

    private Paint mPaint;
    private int mColor;

    private float mWidth = 0f;
    private float mHeight = 0f;
    private float mPadding = 0f;

    private boolean isAnimStarted = false;
    private RotateAnimation mRotateAnim;
    private RectF rectF = new RectF();
    private RectF rectF2 = new RectF();

    public LYCircularCD(Context context) {
        this(context, null);
    }


    public LYCircularCD(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LYCircularCD(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs);
    }

    private void initViews(AttributeSet attrs) {

        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.LYCircularCD);
        mColor = arr.getColor(R.styleable.LYCircularCD_cdColor, Color.parseColor("#0277BD"));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);

        mRotateAnim = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnim.setRepeatCount(-1);
        mRotateAnim.setInterpolator(new LinearInterpolator());
        mRotateAnim.setFillAfter(true);//停在最后
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

        mPadding = 5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        mPaint.setStrokeWidth(2);
        canvas.drawCircle(mWidth/2, mWidth/2, mWidth/2 - mPadding, mPaint);
        mPaint.setStrokeWidth(3);
        canvas.drawCircle(mWidth/2, mWidth/2, mPadding, mPaint);

        mPaint.setStrokeWidth(2);
        rectF = new RectF(mWidth/2 - mWidth/3, mWidth/2 - mWidth/3,
                mWidth/2 + mWidth/3, mWidth/2 + mWidth/3);
        canvas.drawArc(rectF, 0, 80, false, mPaint);
        canvas.drawArc(rectF, 180, 80, false, mPaint);

        rectF2 = new RectF(mWidth/2 - mWidth/4, mWidth/2 - mWidth/4,
                mWidth/2 + mWidth/4, mWidth/2 + mWidth/4);
        canvas.drawArc(rectF2, 0, 80, false, mPaint);
        canvas.drawArc(rectF2, 180, 80, false, mPaint);

        canvas.restore();
    }

    public void startAnim() {

        stopAnim();
        isAnimStarted = true;
        mRotateAnim.setDuration(1200);
        startAnimation(mRotateAnim);
    }

    public void stopAnim() {
        clearAnimation();

        isAnimStarted = false;
    }

    public boolean isAnimStarted() {
        return isAnimStarted;
    }
}
