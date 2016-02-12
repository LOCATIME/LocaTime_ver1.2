package kr.co.locatime.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kr.co.locatime.R;

/**
 * Created by Joonha on 2015-11-26.
 */
public class DivisionActivity extends Activity implements View.OnClickListener {
    private final long FINISH_INTERVAL_TIME = 2000;
    Button btnBuy, btnSell, btnMypage;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division);

        Typeface font = Typeface.createFromAsset(
                getApplicationContext().getAssets(), "fonts/font_ssanai_thin.ttf");

        btnBuy = (Button) findViewById(R.id.btnBuy);
        btnBuy.setTypeface(font);
        btnSell = (Button) findViewById(R.id.btnSell);
        btnSell.setTypeface(font);
        btnMypage = (Button) findViewById(R.id.btnMypage);
        btnMypage.setTypeface(font);

        btnBuy.setOnClickListener(this);
        btnSell.setOnClickListener(this);
        btnMypage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnBuy:
                intent = new Intent(
                        getApplicationContext(), MBoardActivity.class);
                startActivity(intent);

                break;

            case R.id.btnSell:
                intent = new Intent(
                        getApplicationContext(), RegistGActivity.class);
                startActivity(intent);

                break;

            case R.id.btnMypage:
                intent = new Intent(
                        getApplicationContext(), MyPageActivity.class);
                startActivity(intent);

                break;


        }
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "'뒤로 버튼을 한번 더 누르시면 종료됩니다.' ", Toast.LENGTH_SHORT).show();
        }
    }


}
