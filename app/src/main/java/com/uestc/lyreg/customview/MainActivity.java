package com.uestc.lyreg.customview;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_slideback)
    Button mButtonSlideBack;
    @BindView(R.id.text)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupViews();
    }

    private void setupViews() {
        ButterKnife.bind(this);

//        mButtonSlideBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, OneActivity.class);
//
//                startActivity(intent,
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this).toBundle());
//            }
//        });

    }

    public void onButtonClicked(View v) {
        switch (v.getId()) {
            case R.id.button_slideback:
                Intent intent = new Intent(this, OneActivity.class);

                startActivity(intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                break;
            case R.id.button_recycler:
                intent = new Intent(this, RecyclerviewActivity.class);

                startActivity(intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                break;

            case R.id.button_map:
                intent = new Intent(this, MapActivity.class);
                startActivity(intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
            default:
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPx = metrics.widthPixels;
        int heightPx = metrics.heightPixels;

        Log.e("Main", "widthPx => " + widthPx + " heightPx => " + heightPx);

        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        Log.e("Main", "widthPx => " + rect.width() + " heightPx => " + rect.height());

        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        Log.e("Main", "top => " + rect.top);

        getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(rect);

        Log.e("Main", "widthPx => " + rect.width() + " heightPx => " + rect.height());
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity", "onStop");

    }
}
