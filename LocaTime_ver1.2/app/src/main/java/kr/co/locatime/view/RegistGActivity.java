package kr.co.locatime.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;

import kr.co.locatime.Network.LocaHttp;
import kr.co.locatime.R;

/**
 * Created by Joonha on 2015-11-26.
 */
public class RegistGActivity extends Activity {

    EditText content_title, content_OriPrice, content_SellingPrice, edDeadLine;
    String content_deadline;
    Button btnSubmit;
    ImageButton btnHome, btnMypage;
    String user_id;
    TextView titleSell;
    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String month_imp = String.valueOf(selectedMonth + 1);
            String day_imp = String.valueOf(selectedDay);
            if (selectedMonth + 1 < 10) {
                month_imp = "0" + String.valueOf(selectedMonth + 1);
            }

            if (selectedDay < 10) {
                day_imp = "0" + String.valueOf(selectedDay);
            }
            edDeadLine.setText(selectedYear + month_imp + ""
                    + day_imp);
            content_deadline = edDeadLine.getText().toString();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_g);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());


        Typeface font = Typeface.createFromAsset(
                getApplicationContext().getAssets(), "fonts/font_ssanai_thin.ttf");

        titleSell = (TextView) findViewById(R.id.titleSell);
        titleSell.setTypeface(font);

        content_title = (EditText) findViewById(R.id.content_title);
        content_OriPrice = (EditText) findViewById(R.id.content_OriPrice);
        content_SellingPrice = (EditText) findViewById(R.id.content_SellingPrice);

        SharedPreferences prefs = getSharedPreferences("LocaTime", MODE_PRIVATE);
        user_id = prefs.getString("user_id", "");

        Log.d("Saved user_id", user_id);

        edDeadLine = (EditText) findViewById(R.id.edDeadLine);

        edDeadLine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true)
                    showDialog(0);
            }
        });

        /*edDeadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });*/

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);


        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(font);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (content_title.getText().toString().equals("") ||
                        content_SellingPrice.getText().toString().equals("") ||
                        content_deadline.equals("") ||
                        content_OriPrice.getText().toString().equals("")
                        ) {
                    Toast.makeText(getApplicationContext(), "빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (Integer.parseInt(content_OriPrice.getText().toString())
                            >= Integer.parseInt(content_SellingPrice.getText().toString())) {

                        content_deadline = edDeadLine.getText().toString();

                        Log.d("기한", content_deadline);

                        ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
                        String requestUrl = getBaseContext().getText(R.string.server_url) +
                                "member/insertContent";
                        httpParams.add(new BasicNameValuePair("content_title", content_title.getText().toString()));
                        httpParams.add(new BasicNameValuePair("content_SellingPrice", content_SellingPrice.getText().toString()));
                        httpParams.add(new BasicNameValuePair("content_deadline", content_deadline));
                        httpParams.add(new BasicNameValuePair("content_OriPrice", content_OriPrice.getText().toString()));
                        httpParams.add(new BasicNameValuePair("user_id", user_id));

                        LocaHttp locaHttp = new LocaHttp();
                        locaHttp.encoding = "UTF-8";
                        locaHttp.registContent(requestUrl, httpParams);
                        if (locaHttp.result == 1) {
                            Toast.makeText(getApplicationContext(), "정상적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MBoardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "등록실패.네트워크 환경을 확인하세요.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "판매가격이 원가보다 높을 수 없습니다.", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });


        //Home 버튼 처리.(메인으로 이동)
        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        //하단 마이페이지.
        btnMypage = (ImageButton) findViewById(R.id.btnMypage);
        btnMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }
}











