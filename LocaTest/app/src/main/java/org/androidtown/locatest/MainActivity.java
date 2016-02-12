package org.androidtown.locatest;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText user_id ,user_name, user_pw, user_kakao;
    Button btnSend;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_id=(EditText) findViewById(R.id.user_id);
        user_name=(EditText)findViewById(R.id.user_name);
        user_pw=(EditText)findViewById(R.id.user_pw);
        user_kakao=(EditText)findViewById(R.id.user_kakao);
        btnSend=(Button) findViewById(R.id.btnSend);
        result=(TextView) findViewById(R.id.result);




        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memberinsertUrl=getBaseContext().getText(R.string.server_url)+
                        "member/insertMember";
                ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();

                httpParams.add(new BasicNameValuePair("user_id", user_id.getText().toString()));
                httpParams.add(new BasicNameValuePair("user_pw", user_pw.getText().toString()));
                httpParams.add(new BasicNameValuePair("user_name", user_name.getText().toString()));
                httpParams.add(new BasicNameValuePair("user_kakao", user_kakao.getText().toString()));

                LocaHttp locaHttp= new LocaHttp();
                locaHttp.encoding="UTF-8";
                locaHttp.sendPost(memberinsertUrl,httpParams);
            }
        });
    }
}
