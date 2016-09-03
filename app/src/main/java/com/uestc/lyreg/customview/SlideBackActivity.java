package com.uestc.lyreg.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * 支持右滑返回
 * 1. 滑动关闭页面基类，使用时继承此类并相应activity的style里加入以下内容即可
 *      <item name="android:windowBackground">@android:color/transparent</item>
 *      <item name="android:windowIsTranslucent">true</item>
 * 2. 同时需要手动给需要滑动返回的activity的layout设置背景色，要不然是透明的。
 * 3. 同时由于主题设为透明，会导致相应启动该activity的前一个activity的生命周期受到影响，
 *      使前一个activity启动支持滑动返回的activity之后只调用onPause而不调用onStop了。
 * created at 2016/6/13 15:34
 */
public class SlideBackActivity extends AppCompatActivity {

    private static final String TAG = "SlideBackActivity";

    // 手指在执行右滑返回时 起始位置的最大范围
    private static final int XSTART_MAX = 100;

    // 手指上下滑动时的最小速度
    private static final int YSPEED_MIN = 1000;

    // 手指向右滑动时的最小距离
    private static int XDISTANCE_MIN;

    // 手指右滑时，允许上下滑动的最大范围
    private static final int YDISTANCE_MAX = 200;

    // 记录手指按下时的横坐标
    private float xDown;

    // 记录手指按下时的纵坐标
    private float yDown;

    // 记录手指滑动时的横坐标
    private float xMove;

    // 记录手指滑动时的纵坐标
    private float yMove;

    private float lastX;

    // 用于计算手指滑动的速度
    private VelocityTracker mVelocityTracker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupWindowAnimations();

        XDISTANCE_MIN = getScreenWidth(this) / 3;

        Log.e(TAG, "screenwidth => " + getScreenWidth(this));
    }

    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.RIGHT);
        slide.setDuration(300);
        slide.setInterpolator(new LinearInterpolator());

        getWindow().setEnterTransition(slide);
        getWindow().setReturnTransition(slide);
    }

    boolean hasIgnoreFirstMove;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        createVelocityTracker(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "dispatchTouchEvent Down");
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                lastX = xDown;
                Log.e(TAG, "xDown = " + xDown + " yDown = " + yDown);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "dispatchTouchEvent Move");
                xMove = ev.getRawX();
                yMove = ev.getRawY();

                float dx = xMove - lastX;

                if(xDown <= XSTART_MAX ) {
                    if (dx != 0f && !hasIgnoreFirstMove ) {
                        hasIgnoreFirstMove = true;
                        dx = dx / dx;
                    }
                    if (getContentX() + dx < 0) {
                        setContentX(0);
                    } else {
                        setContentX(getContentX() + dx);
                    }
                    lastX = xMove;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "dispatchTouchEvent Up");

                xMove = ev.getRawX();
                yMove = ev.getRawY();
                // 滑动的距离
                int distanceX = (int) (xMove - xDown);
                int distanceY = (int) (yMove - yDown);

                // 获取顺时速度
                int speedY = getScrollVelocity();
                // 关闭Activity需满足以下条件：
                // 1. x轴的起始位置<= XSTART_MAX
                // 2. x轴滑动的距离>XDISTANCE_MIN
                // 3. y轴滑动的距离在YDISTANCE_MIN范围内
                // 4. y轴上（即上下滑动的速度）<YSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
                Log.e(TAG, "distanceX => " + distanceX + " distanceY => " + distanceY);
                if(xDown <= XSTART_MAX && distanceX > XDISTANCE_MIN
                        && Math.abs(distanceY) < YDISTANCE_MAX
                        && speedY < YSPEED_MIN ) {
                        finishAfterTransition();
                } else {
                    //回弹
                    animateBack(false);
                }

                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
    
    /**
     * 创建VelocityTracker对象，并将需要计算速度的触摸事件加入到VelocityTracker当中。
     * @param event 被追踪其速度的触摸事件
     * created at 2016/6/13 15:40
     */
    private void createVelocityTracker(MotionEvent event) {
        if(mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     *
     * created at 2016/6/13 15:42
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 计算相应滑动速度
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     * created at 2016/6/13 15:42
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getYVelocity();
        return Math.abs(velocity);
    }

    public void setContentX(float x) {
        int ix = (int) x;
        final ViewGroup root = (ViewGroup) getWindow().getDecorView();
        root.getChildAt(0).setX(ix);
        root.getChildAt(0).invalidate();
    }

    public float getContentX() {
        final ViewGroup root = (ViewGroup) getWindow().getDecorView();
        return root.getChildAt(0).getX();
    }


    private int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    ObjectAnimator animator;

    public void cancelPotentialAnimation() {
        if (animator != null) {
            animator.removeAllListeners();
            animator.cancel();
        }
    }
    /**
     * 弹回，不关闭，因为left是0，所以setX和setTranslationX效果是一样的
     *
     * @param withVel 使用计算出来的时间
     */
    private void animateBack(boolean withVel) {
        cancelPotentialAnimation();
        animator = ObjectAnimator.ofFloat(this, "contentX", getContentX(), 0);

        animator.setDuration(100);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
