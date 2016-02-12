package kr.co.locatime.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import kr.co.locatime.Network.LocaHttp;
import kr.co.locatime.R;

/**
 * Created by Joonha on 2015-11-26.
 */
public class RegistUActivity extends Activity {
    EditText user_id, user_pw, user_pwconfrim, user_phone, user_kakao, user_name;
    Button btnConfirm, btnCancel, btnCheck, btnPwCheck;
    ArrayList<NameValuePair> httpParams;
    boolean idcheck, pwcheck;
    LocaHttp locaHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_u);
        user_id = (EditText) findViewById(R.id.user_id);
        user_id.setPrivateImeOptions("defaultInputmode=english");
        user_phone = (EditText) findViewById(R.id.user_phone);
        user_name = (EditText) findViewById(R.id.user_name);
        user_pw = (EditText) findViewById(R.id.user_pw);
        user_pwconfrim = (EditText) findViewById(R.id.user_pwconfrim);
        user_kakao = (EditText) findViewById(R.id.user_kakao);
        user_kakao.setPrivateImeOptions("defaultInputmode=english");

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());


        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idcheck == true && pwcheck == true) {
                    httpParams = new ArrayList<NameValuePair>();
                    String requestUrl = getBaseContext().getText(R.string.server_url) +
                            "member/insertMember";
                    httpParams.add(new BasicNameValuePair("user_id", user_id.getText().toString()));
                    httpParams.add(new BasicNameValuePair("user_phone", user_phone.getText().toString()));
                    httpParams.add(new BasicNameValuePair("user_pw", user_pw.getText().toString()));
                    httpParams.add(new BasicNameValuePair("user_name", user_name.getText().toString()));
                    httpParams.add(new BasicNameValuePair("user_kakao", user_kakao.getText().toString()));

                    locaHttp = new LocaHttp();
                    locaHttp.encoding = "UTF-8";

                    locaHttp.registMember(requestUrl, httpParams);
                    if (locaHttp.result == 1) {
                        Intent intent = new Intent(getApplicationContext(), DivisionActivity.class);
                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                        SharedPreferences preferences = getSharedPreferences("LocaTime", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user_id", user_id.getText().toString());
                        editor.commit();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.\n네트워크 상태를 점검하세요.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID 중복확인 및 PW 일치확인을 해주세요.", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpParams = new ArrayList<NameValuePair>();
                String url = getBaseContext().getText(R.string.server_url) +
                        "member/idCheck";
                httpParams.add(new BasicNameValuePair("user_id", user_id.getText().toString()));

                locaHttp = new LocaHttp();
                locaHttp.encoding = "UTF-8";

                int loginCheck = locaHttp.idCheck(url, httpParams);
                if (loginCheck == 1) {
                    Toast.makeText(getApplicationContext(), "입력하신 ID가 존재합니다. 다른 ID를 입력해주세요.", Toast.LENGTH_LONG).show();
                    idcheck = false;
                } else if (loginCheck == 3) {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 확인해주세요. ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "사용 가능한 ID입니다.", Toast.LENGTH_LONG).show();
                    idcheck = true;
                }
            }
        });

        btnPwCheck = (Button) findViewById(R.id.btnPwCheck);
        btnPwCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user_pw.getText().toString().equals(user_pwconfrim.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "패스워드가 일치합니다.", Toast.LENGTH_LONG).show();
                    pwcheck = true;

                } else {
                    Toast.makeText(getApplicationContext(), "패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    pwcheck = false;
                }

            }
        });

    }


}
