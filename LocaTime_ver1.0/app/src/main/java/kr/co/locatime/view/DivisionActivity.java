package kr.co.locatime.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kr.co.locatime.R;

/**
 * Created by Joonha on 2015-11-26.
 */
public class DivisionActivity extends Activity implements View.OnClickListener{
    Button btnBuy,btnSell,btnMypage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division);

        Typeface font = Typeface.createFromAsset(
                getApplicationContext().getAssets(), "fonts/font_ssanai_thin.ttf");

        btnBuy=(Button)findViewById(R.id.btnBuy);
        btnBuy.setTypeface(font);
        btnSell=(Button)findViewById(R.id.btnSell);
        btnSell.setTypeface(font);
        btnMypage=(Button)findViewById(R.id.btnMypage);
        btnMypage.setTypeface(font);

        btnBuy.setOnClickListener(this);
        btnSell.setOnClickListener(this);
        btnMypage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
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
}
