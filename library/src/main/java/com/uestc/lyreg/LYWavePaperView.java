package com.uestc.lyreg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/6/13.
 *
 * @Author lyreg
 */
public class LYWavePaperView extends View {

    private static int MODE_TRIANGLE = -2;
    private static int MODE_CIRLE    = -1;

    private int mWaveCount = 20;

    private int mWaveWidth = 20;

    private int mWaveHeight;

    private int mWaveMode = -2;

    private int mColor = Color.parseColor("#2C97DE");

    private Paint mPaint;

    private int mWidth;
    private int mHeight;
    private int mRectWidth;
    private int mRectHeight;


    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public LYWavePaperView(Context context) {
        this(context, null);
    }


    public LYWavePaperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LYWavePaperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        if(attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LYWavePaperView);
            mWaveCount = array.getInteger(R.styleable.LYWavePaperView_waweCount, 20);
            mWaveWidth = array.getInteger(R.styleable.LYWavePaperView_waweWidth, 20);
            mWaveMode = array.getInteger(R.styleable.LYWavePaperView_mode, -2);
            mColor = array.getColor(R.styleable.LYWavePaperView_backgroundColor, Color.parseColor("#2C97DE"));
            array.recycle();
        }

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);

        // 防止边缘锯齿
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
            mRectWidth = (int) (mWidth * 0.8);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = DisplayUtil.dip2px(getContext(), 120);
            mRectWidth = (int) (mWidth * 0.8);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
            mRectHeight = (int) (mHeight * 0.8);

        } else if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = DisplayUtil.dip2px(getContext(), 80);
            mRectHeight = (int) (mHeight * 0.8);
        }

        mWaveHeight = mRectHeight/mWaveCount;

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float paddingX = (mWidth - mRectWidth)/2;
        float paddingY = (mHeight - mRectHeight)/2;

        canvas.drawRect(paddingX, paddingY, mRectWidth+paddingX, mRectHeight+paddingY, mPaint);


        if(mWaveMode == MODE_TRIANGLE) {
            Path path = new Path();
            // 绘制左边的波浪
            float startX = paddingX;
            float startY = paddingY;
            path.moveTo(startX, startY);
            for(int i = 0; i < mWaveCount; i++) {
                path.lineTo(startX - mWaveWidth, startY + i * mWaveHeight + (mWaveHeight / 2));
                path.lineTo(startX, startY + (i+1) * mWaveHeight);
            }
            path.close();
            canvas.drawPath(path, mPaint);

            // 绘制右边的波浪
            startX = mRectWidth + paddingX;
            startY = paddingY;
            path.moveTo(startX, startY);
            for(int i = 0; i < mWaveCount; i++) {
                path.lineTo(startX + mWaveWidth, startY + i * mWaveHeight + (mWaveHeight / 2));
                path.lineTo(startX, startY + (i+1) * mWaveHeight);
            }
            path.close();
            canvas.drawPath(path, mPaint);
        } else {
            Path path = new Path();
            // 绘制左边的波浪
            float startX = paddingX;
            float startY = paddingY + mWaveHeight/2;
            for (int i = 0; i < mWaveCount; i++) {
                canvas.drawCircle(startX, startY + i * mWaveHeight, mWaveHeight/2, mPaint);
            }

            // 绘制右边的波浪
            startX = paddingX + mRectWidth;
            startY = paddingY + mWaveHeight/2;
            for (int i = 0; i < mWaveCount; i++) {
                canvas.drawCircle(startX, startY + i * mWaveHeight, mWaveHeight/2, mPaint);
            }
        }

        super.onDraw(canvas);
    }
}
