package com.uestc.lyreg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/6/17.
 *
 * @Author lyreg
 */
public class LYWaveLoadingView extends View {

    private Paint mWavePaint;

    private PorterDuffXfermode mMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);//设置mode 为XOR

    private Paint mCirclePaint;

    private Canvas mCanvas;//我们自己的画布
    private Bitmap mBitmap;
    private Path   mPath;
    private int mWidth;
    private int mHeight;
    private int mPercent;

    private int x;
    private int y;
    private boolean isLeft;

    public LYWaveLoadingView(Context context) {
        this(context, null);
    }


    public LYWaveLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LYWaveLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {

        mWavePaint = new Paint();
        mWavePaint.setColor(Color.parseColor("#33b5e5"));
        mWavePaint.setAntiAlias(true);
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor("#99cc00"));
        mCirclePaint.setAntiAlias(true);

        mBitmap = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888); //生成一个bitmap
        mCanvas = new Canvas(mBitmap);//将bitmp放在我们自己的画布上，实际上mCanvas.draw的时候 改变的是这个bitmap对象

        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = DisplayUtil.dip2px(getContext(), 150);
        }


        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mHeight = DisplayUtil.dip2px(getContext(), 150);
        }

        if (mWidth > mHeight) {
            mWidth = mHeight;
        } else {
            mHeight = mWidth;
        }

        y = mHeight;
        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (x > 50) {
            isLeft = true;
        } else if (x < 0) {
            isLeft = false;
        }

        if(isLeft) {
            x = x-1;
        } else {
            x = x+1;
        }

        mPath.reset();
        y = (int) ((1 - mPercent / 100f) * mHeight);
        mPath.moveTo(0, y);
//        mPath.cubicTo(100 + x * 2, 50 + y, 100 + x * 2, y - 50, mWidth, y);
        mPath.cubicTo(100+mWidth * x /100f, 50 + y, 100+mWidth * x /100f, y - 50, mWidth, y);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();

        //清除掉图像 不然path会重叠
        mBitmap.eraseColor(Color.parseColor("#00000000"));

        mCanvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mCirclePaint);
//        int len = "lyreg".length();
//        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mCirclePaint.setStrokeWidth(3);
//        mCirclePaint.setTextSize(200);
//        mCanvas.drawText("lyreg", 0, mHeight*3/4, mCirclePaint);

        mWavePaint.setXfermode(mMode);
        //src
        mCanvas.drawPath(mPath, mWavePaint);
//        canvas.drawPath(mPath, mWavePaint);
        mWavePaint.setXfermode(null);


        canvas.drawBitmap(mBitmap, 0, 0, null);

        postInvalidateDelayed(10);

        super.onDraw(canvas);
    }


    public void setPercent(int percent) {
        this.mPercent = percent;

        invalidate();
    }
}
