package com.uestc.lyreg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/6/16.
 *
 * @Author lyreg
 */
public class LYArrowLoadingView extends View {

    private final String TAG = getClass().getSimpleName();

    private int mWidth;
    private int mHeight;

    private int mDefaultWidth = DisplayUtil.dip2px(getContext(), 100);

    private int mCircleStrokeWidth = 10;

    private int mDefaultColor = Color.parseColor("#2EA4F2");

    private Paint mPaint;

    // 标记是否可以开始动画
    private boolean isStartAnimation = false;

    // 判断是不是正在draw动画
    private boolean isDrawing = false;

    private boolean isArrowToLine = false;

    //标记上升是否完成
    private boolean isRiseDone = false;

    //竖线缩短的百分比
    private int mLineShrinkPercent = 0;

    // 箭头变形的百分比
    private int mArrowPercent = 0;

    private int mRisePercent = 0;

    //横线变对勾的百分比
    private int mLinePercent;

    //圆形进度百分比
    private int mCirclePercent = 0;


    public LYArrowLoadingView(Context context) {
        this(context, null);
    }


    public LYArrowLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LYArrowLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        setBackgroundColor(Color.LTGRAY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            Log.e("onMeasure", "EXACTLY");
            mWidth = widthSize;
        } else {
            Log.e("onMeasure", "at most");
            mWidth = mDefaultWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = mDefaultWidth;
        }

        if (mWidth > mHeight) {
            mWidth = mHeight;
        } else {
            mHeight = mWidth;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int strokeWidth = mCircleStrokeWidth * mWidth / mDefaultWidth;
        mPaint.setColor(mDefaultColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);

        //百分比弧的矩形
        RectF rectF = new RectF(10, 10, mWidth - 10, mHeight - 10);

        canvas.drawCircle(mWidth* 0.5f, mHeight* 0.5f, mWidth* 0.5f - 10, mPaint);

        Path path = new Path();
        if (!isStartAnimation) {
            // 1. 画外圈 以及 静止的箭头
            mPaint.setColor(Color.WHITE);
            canvas.drawLine(mWidth* 0.5f, mHeight* 0.25f, mWidth* 0.5f, mWidth* 0.75f, mPaint);

            path.moveTo(mWidth* 0.25f, mHeight* 0.5f);
            path.lineTo(mWidth* 0.5f, mWidth* 0.75f);
            path.lineTo(mWidth* 0.75f, mHeight* 0.5f);
            canvas.drawPath(path, mPaint);
        } else {
            isDrawing = true;

            // 线段 缩短
            mPaint.setColor(Color.WHITE);
            if (mLineShrinkPercent < 95) {
                // 线段逐渐缩短（终点为mWidth/2,mHeight/2）
                float tmp = (mHeight*0.5f - mHeight * 0.25f) * mLineShrinkPercent / 100f;
                canvas.drawLine(mWidth *0.5f, mHeight * 0.25f + tmp, mWidth *0.5f, mHeight * 0.75f - tmp, mPaint);
                mLineShrinkPercent += 5;

                path.moveTo(mWidth* 0.25f, mHeight* 0.5f);
                path.lineTo(mWidth* 0.5f, mWidth* 0.75f);
                path.lineTo(mWidth* 0.75f, mHeight* 0.5f);
                canvas.drawPath(path, mPaint);
            } else {
                // 箭头变成直线
                isArrowToLine = true;
                if (mArrowPercent < 100) {
                    path.moveTo(mWidth* 0.25f, mHeight*0.5f);
                    path.lineTo(mWidth *0.5f, mHeight * 0.75f - mArrowPercent / 100f * 0.25f * mHeight);
                    path.lineTo(mWidth * 0.75f, mHeight * 0.5f);
                    canvas.drawPath(path, mPaint);
                    mArrowPercent+=5;

                    //在变成直线的过程中这个点一直存在
                    canvas.drawCircle(mWidth / 2, mHeight / 2,2.5f, mPaint);

                } else {
                    // 绘制把点上弹的直线
                    if (mRisePercent < 100) {
                        //在点移动到圆弧上的时候 线是一直存在的
                        canvas.drawLine(mWidth / 4, mHeight * 0.5f, mWidth * 0.75f, mHeight * 0.5f, mPaint);
                        canvas.drawCircle(mWidth / 2, mHeight / 2 - mHeight / 2 * mRisePercent / 100 + 5,2.5f, mPaint);
                        mRisePercent += 5;
                    } else {
                        //上升的点最终的位置
                        canvas.drawPoint(mWidth / 2, 10, mPaint);
                        isRiseDone = true;

                        //改变对勾形状
                        if (mLinePercent < 100) {

                            path.moveTo(mWidth / 4, mHeight * 0.5f);
                            path.lineTo(mWidth / 2, mHeight * 0.5f + mLinePercent / 100f * mHeight * 0.25f);
                            path.lineTo(mWidth * 0.75f, mHeight * 0.5f - mLinePercent / 100f * mHeight * 0.3f);
                            canvas.drawPath(path, mPaint);
                            mLinePercent += 5;

                            //动态绘制圆形百分比
                            if (mCirclePercent < 100) {
                                canvas.drawArc(rectF, 270, -mCirclePercent / 100.0f * 360, false, mPaint);
                                mCirclePercent += 5;
                            }
                        } else {
                            //绘制最终的path
                            path.moveTo(mWidth / 4, mHeight * 0.5f);
                            path.lineTo(mWidth / 2, mHeight * 0.75f);
                            path.lineTo(mWidth * 0.75f, mHeight * 0.3f);
                            canvas.drawPath(path, mPaint);
//                            绘制最终的圆
                            canvas.drawArc(rectF, 270, -360, false, mPaint);

                            isDrawing = false;
                        }
                    }
                }
            }
            if (!isArrowToLine) {
                path.moveTo(mWidth / 4, mHeight * 0.5f);
                path.lineTo(mWidth / 2, mHeight * 0.75f);
                path.lineTo(mWidth * 0.75f, mHeight * 0.5f);
                canvas.drawPath(path, mPaint);
            }
        }

        postInvalidateDelayed(10);
        super.onDraw(canvas);
    }


    public void start() {
        if (isDrawing == false) {
            isStartAnimation = true;
            isRiseDone = false;
            mRisePercent = 0;
            mLineShrinkPercent = 0;
            mCirclePercent = 0;
            mArrowPercent = 0;
            mLinePercent = 0;
            invalidate();
        }
    }
}
