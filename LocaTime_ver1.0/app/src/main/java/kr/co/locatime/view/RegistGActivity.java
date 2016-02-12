package kr.co.locatime.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import kr.co.locatime.Network.LocaHttp;
import kr.co.locatime.R;

/**
 * Created by Joonha on 2015-11-26.
 */
public class RegistGActivity extends Activity {

    EditText content_title,content_OriPrice,content_SellingPrice;
    String content_deadline;
    Button btnSubmit;
    ImageButton btnHome,btnBack,btnMypage;
    Spinner year_spinner,month_spinner,day_spinner;

    String user_id;

    TextView titleSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_g);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());


        Typeface font = Typeface.createFromAsset(
                getApplicationContext().getAssets(), "fonts/font_ssanai_thin.ttf");

        titleSell=(TextView)findViewById(R.id.titleSell);
        titleSell.setTypeface(font);

        content_title=(EditText)findViewById(R.id.content_title);
        content_OriPrice=(EditText)findViewById(R.id.content_OriPrice);
        content_SellingPrice=(EditText)findViewById(R.id.content_SellingPrice);

        SharedPreferences prefs=getSharedPreferences("LocaTime",MODE_PRIVATE);
        user_id=prefs.getString("user_id","");

        Log.d("Saved user_id",user_id);

        year_spinner=(Spinner)findViewById(R.id.year_spinner);
        month_spinner=(Spinner)findViewById(R.id.month_spinner);
        day_spinner=(Spinner)findViewById(R.id.day_spinner);

        String[] year=new String[11];
        year[0]="";
        for(int i=1;i<11;i++){
            year[i]=String.valueOf(i+2014);
        }

        String[] month=new String[13];
        month[0]="";
        for(int i=1;i<13;i++){
            if(i<10) {
                month[i] = "0"+String.valueOf(i);
            }else {
                month[i] = String.valueOf(i);
            }
        }

        ArrayAdapter<String> adapterYear
                =new ArrayAdapter<String>(this,R.layout.spinner_year,year);
        ArrayAdapter<String> adapterMonth
                =new ArrayAdapter<String>(this,R.layout.spinner_month,month);

        year_spinner.setAdapter(adapterYear);

        month_spinner.setAdapter(adapterMonth);
        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("test01", month_spinner.getSelectedItem().toString());
                String tempDay = month_spinner.getSelectedItem().toString();
                if (tempDay.equals("01") ||
                        tempDay.equals("03") ||
                        tempDay.equals("05") ||
                        tempDay.equals("07") ||
                        tempDay.equals("08") ||
                        tempDay.equals("10") ||
                        tempDay.equals("12")) {
                    String[] day2 = new String[32];
                    day2[0] = "";
                    for (int i = 1; i < 32; i++) {
                        if (i < 10) {
                            day2[i] = "0" + String.valueOf(i);
                        } else {
                            day2[i] = String.valueOf(i);
                        }
                    }
                    ArrayAdapter<String> tempAdapter
                            = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_month, day2);
                    day_spinner.setAdapter(tempAdapter);
                } else {
                    String[] day1 = new String[31];
                    day1[0] = "";
                    for (int i = 1; i < 31; i++) {
                        if (i < 10) {
                            day1[i] = "0" + String.valueOf(i);
                        } else {
                            day1[i] = String.valueOf(i);
                        }
                    }
                    ArrayAdapter<String> tempAdapter
                            = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_month, day1);
                    day_spinner.setAdapter(tempAdapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnSubmit.setTypeface(font);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(content_OriPrice.getText().toString())
                        >= Integer.parseInt(content_SellingPrice.getText().toString())) {

                    content_deadline = year_spinner.getSelectedItem().toString()
                            + month_spinner.getSelectedItem().toString()
                            + day_spinner.getSelectedItem().toString();

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
                    } else {
                        Toast.makeText(getApplicationContext(), "등록실패.네트워크 환경을 확인하세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "판매가격이 원가보다 높을 수 없습니다.", Toast.LENGTH_LONG).show();
                }

            }
        });

        //뒤로가기
        btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Home 버튼 처리.(메인으로 이동)
        btnHome=(ImageButton)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), DivisionActivity.class);
                startActivity(intent);
            }
        });

        //하단 마이페이지.
        btnMypage=(ImageButton)findViewById(R.id.btnMypage);
        btnMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });

    }
}
