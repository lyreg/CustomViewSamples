package com.uestc.lyreg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/6/14.
 *
 * @Author lyreg
 */
public class LYDashBoardView extends View {

    private static String TAG = "DashBoardView";

    private int mWidth;
    private int mHeight;

    private int mPercent = 0;

    // 刻度宽度
    private float mTikeWidth;

    //刻度的个数
    private int mTikeCount;

    // 第二个弧的宽度
    private int mSecondArcWidth;

    // 最小圆的半径
    private int mMinCircleRadius;

    // 文字矩形的宽
    private int mRectWidth;

    // 文字矩形的高
    private int mRectHeight;

    // 文字内容
    private String mText = "";

    // 文字的大小
    private int mTextSize;

    // 设置文字颜色
    private int mTextColor;

    // 最外面弧的颜色
    private int mArcColor;

    // 小圆和指针颜色
    private int mMinCircleColor;

    private Paint mPaint;

    public LYDashBoardView(Context context) {
        this(context, null);
    }


    public LYDashBoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LYDashBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LYDashBoardView);
        mArcColor = a.getColor(R.styleable.LYDashBoardView_arcColor, Color.parseColor("#5FB1ED"));
        mMinCircleColor = a.getColor(R.styleable.LYDashBoardView_pointerColor,Color.parseColor("#C9DEEE"));
        mTikeCount = a.getInt(R.styleable.LYDashBoardView_tikeCount,12);
        mTextSize = a.getDimensionPixelSize(R.styleable.LYDashBoardView_android_textSize,36);
        mText = a.getString(R.styleable.LYDashBoardView_android_text);
        mSecondArcWidth = 50;

        mPaint = new Paint();
        // 防止边缘锯齿
        mPaint.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = DisplayUtil.dip2px(getContext(), 160);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = DisplayUtil.dip2px(getContext(), 160);
        }

        if(mWidth > mHeight) {
            mWidth = mHeight;
        } else {
            mHeight = mWidth;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int strokeWidthOut = 5;
        int paddingOut = 10;

        mPaint.setStrokeWidth(strokeWidthOut);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mArcColor);

        // 1. 绘制最外面的弧线
        canvas.drawArc(new RectF(paddingOut, paddingOut, mWidth - paddingOut, mHeight - paddingOut),
                145, 250, false, mPaint);

        // 2. 绘制里边的粗弧线
        mSecondArcWidth = 60;
        int paddingIn = paddingOut + 60;

        RectF secondRectF = new RectF(paddingIn, paddingIn, mWidth - paddingIn, mHeight - paddingIn);
        float secondRectWidth = mWidth - paddingIn - paddingIn;
        float secondRectHeight = mHeight - paddingIn - paddingIn;
        float percent = mPercent / 100f;

        mPaint.setStrokeWidth(mSecondArcWidth);

        // 充满的圆弧的度数    -5是大小弧的偏差
        float fill = 250 * percent;
        // 空的圆弧的度数
        float empty = 250 - fill;

        if(percent == 0) {
            mPaint.setColor(Color.WHITE);
        }

        // 画粗弧突出部分左端
        canvas.drawArc(secondRectF, 135, 11, false, mPaint);
        if(percent != 0) {
            // 绘制2弧 也就是fill充满的弧，
            canvas.drawArc(secondRectF, 145, fill, false, mPaint);
        }

        mPaint.setColor(Color.WHITE);
        //画弧胡的未充满部分
        canvas.drawArc(secondRectF, 145 + fill, empty, false, mPaint);

        // 画出右边突出的4弧, 如果百分比为100 那么和充满的颜色一致，否则为白色
        if(percent == 1){
            mPaint.setColor(mArcColor);
        }
        canvas.drawArc(secondRectF, 144 + fill + empty, 10, false, mPaint);


        // 3. 绘制中心的圆

            // 绘制外圈
        mPaint.setColor(mArcColor);
        mPaint.setStrokeWidth(strokeWidthOut);
        canvas.drawCircle(mWidth/2, mHeight/2, 30, mPaint);
            // 绘制内圆
        mMinCircleRadius = 15;
        mPaint.setColor(mMinCircleColor);
        mPaint.setStrokeWidth(strokeWidthOut + 5);
        canvas.drawCircle(mWidth/2, mHeight/2, mMinCircleRadius, mPaint);

        // 4. 绘制最外面弧线上的刻度
        mPaint.setColor(mArcColor);
        // 绘制第一条最上面的刻度
        mTikeWidth = 20;
        mPaint.setStrokeWidth(strokeWidthOut);
        canvas.drawLine(mWidth / 2, paddingOut, mWidth / 2, mTikeWidth + paddingOut, mPaint);

        // 旋转的角度
        float rAngle = 250f / mTikeCount;
        // 通过旋转画布 绘制右面的刻度
        for (int i = 0; i < mTikeCount / 2; i++) {
            canvas.rotate(rAngle, mWidth / 2, mHeight / 2);
            canvas.drawLine(mWidth / 2, paddingOut, mWidth / 2, mTikeWidth + paddingOut, mPaint);
        }
        // 现在需要将将画布旋转回来
        canvas.rotate(-rAngle * mTikeCount / 2, mWidth / 2, mHeight / 2);
        // 通过旋转画布 绘制左面的刻度
        for (int i = 0; i < mTikeCount / 2; i++) {
            canvas.rotate(-rAngle, mWidth / 2, mHeight / 2);
            canvas.drawLine(mWidth / 2, paddingOut, mWidth / 2, mTikeWidth + paddingOut, mPaint);
        }
        // 现在需要将将画布旋转回来
        canvas.rotate(rAngle * mTikeCount / 2, mWidth / 2, mHeight / 2);

        // 5. 绘制指针
        mPaint.setColor(mMinCircleColor);
        mPaint.setStrokeWidth(strokeWidthOut);

        canvas.rotate((250 * percent - 250/2), mWidth/2, mHeight/2);
        canvas.drawLine(mWidth/2, (mHeight/2 - secondRectHeight/2 + mSecondArcWidth/2 + 10),
                mWidth/2, mHeight/2 - mMinCircleRadius, mPaint);

        canvas.rotate(-(250 * percent - 250/2), mWidth/2, mHeight/2);

        // 6. 绘制矩形和文字

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mArcColor);
        mRectWidth = 75;
        mRectHeight = 30;

        // 文字矩形的最底部坐标
        float rectBottomY = mHeight/2 + secondRectHeight/3 + mRectHeight;
        canvas.drawRect(mWidth/2 - mRectWidth/2, mHeight/2 + secondRectHeight/3, mWidth/2 + mRectWidth/2, rectBottomY, mPaint);

        // 绘制文字
        if(mText == null) {
            mText = "已完成";
        }

        mPaint.setTextSize(mTextSize);
        mTextColor = Color.WHITE;
        mPaint.setColor(mTextColor);
        float txtLength = mPaint.measureText(mText);
        canvas.drawText(mText, mWidth/2 - txtLength/2, rectBottomY+50, mPaint);

        super.onDraw(canvas);
    }

    /**
     * 设置百分比
     * @param percent
     */
    public void setPercent(int percent) {
        mPercent = percent;
        invalidate();
    }

    /**
     * 设置文字
     * @param text
     */
    public void setText(String text){
        mText = text;
        invalidate();
    }

    /**
     * 设置圆弧颜色
     * @param color
     */

    public void setArcColor(int color){
        mArcColor = color;
        invalidate();
    }


    /**
     * 设置指针颜色
     * @param color
     */
    public void setPointerColor(int color){
        mMinCircleColor = color;
        invalidate();
    }

    /**
     * 设置文字大小
     * @param size
     */
    public void setTextSize(int size){
        mTextSize = size;
        invalidate();
    }

    /**
     * 设置粗弧的宽度
     * @param width
     */
    public void setArcWidth(int width){
        mSecondArcWidth = width;
        invalidate();
    }
}
