package com.uestc.lyreg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/6/13.
 *
 * @Author lyreg
 */
public class LYPopupView extends View {

    private Paint mPaint;

    private int mWidth;
    private int mHeight;

    private int mRectWidth;
    private int mRectHeight;
    private float mRectPercent = (float) 0.8;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public LYPopupView(Context context) {
        this(context, null);
    }


    public LYPopupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LYPopupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        mPaint = new Paint();

        mPaint.setColor(Color.parseColor("#2C97DE"));
        mPaint.setStyle(Paint.Style.FILL);
        // 防止边缘锯齿
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode== MeasureSpec.EXACTLY){
            mWidth = widthSize; //获取到当前view的宽度
            mRectWidth = (int) (mWidth * mRectPercent);//计算矩形的大小 这里是直接给的初值为0.8 也就是说矩形是view大小的0.8倍 下同
        } else if(widthMode == MeasureSpec.AT_MOST) {
            mWidth = widthSize/2;
            mRectWidth = (int) (mWidth * mRectPercent);
        }

        if(heightMode == MeasureSpec.EXACTLY){
            mHeight = heightSize;//获取当前view的高度
            mRectHeight = (int) (mHeight * mRectPercent+0.1);
        } else if(heightMode == MeasureSpec.AT_MOST) {
            mHeight = heightSize/2;
            mRectHeight = (int) (mHeight * mRectPercent+0.1);
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawRoundRect(new RectF(0, 0, mRectWidth, mRectHeight), 10, 10, mPaint);

        Path path = new Path();
        path.moveTo(mRectWidth/2-30,mRectHeight);
        path.lineTo(mRectWidth/2,mRectHeight+20);
        path.lineTo(mRectWidth/2+30,mRectHeight);
        path.close();

        canvas.drawPath(path, mPaint);

        super.onDraw(canvas);

    }
}
