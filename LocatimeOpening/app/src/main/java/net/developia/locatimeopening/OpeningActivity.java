package net.developia.locatimeopening;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class OpeningActivity extends Activity {
    TextView textView;
    TextView[] textCh = new TextView[8];
    Animation transparencyAnimation, translationAnimation;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //statusbar 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_opening);

        textCh[0] = (TextView) findViewById(R.id.textL);
        textCh[1] = (TextView) findViewById(R.id.textO);
        textCh[2] = (TextView) findViewById(R.id.textC);
        textCh[3] = (TextView) findViewById(R.id.textA);
        textCh[4] = (TextView) findViewById(R.id.textT);
        textCh[5] = (TextView) findViewById(R.id.textI);
        textCh[6] = (TextView) findViewById(R.id.textM);
        textCh[7] = (TextView) findViewById(R.id.textE);

        //textCh 폰트설정
        Typeface font = Typeface.createFromAsset(
                getApplicationContext().getAssets(), "fonts/font_ssanai_thin.ttf");
        for(int i=0; i < textCh.length; i++) {
            textCh[i].setTypeface(font);
        }
        //애니메이션설정
        transparencyAnimation = AnimationUtils.loadAnimation(this, R.anim.transparency);
        translationAnimation = AnimationUtils.loadAnimation(this, R.anim.moving_locatime);

        //액티비티가 화면에 보이면 새로 정의한 스레드 시작
        thread = new textThread();
        thread.start();

        textView = (TextView) findViewById(R.id.textView);
        //textView 폰트설정
        textView.setTypeface(font);
        textView.setAnimation(transparencyAnimation);
    }

    //thread 클래스 상속하여 새로운 스레드 정의
    class textThread extends Thread {
        Handler handler = new Handler();
        int i=0;
        public void run() {
            while (true) {
                try {
                    thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textCh[i].startAnimation(translationAnimation);
                        i++;
                        i%=textCh.length;
                    }
                });
            }

        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {//포커스 받았을 때 수행
            translationAnimation.start();
            transparencyAnimation.start();
        } else { //포커스 없을 때 수행
            translationAnimation.reset();
            transparencyAnimation.reset();
        }
    }

    //onclick 메서드 수행
    public void onImageClicked(View v) {
        Intent intent = new Intent(
                getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
