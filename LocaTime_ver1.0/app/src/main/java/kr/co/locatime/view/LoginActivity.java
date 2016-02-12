package kr.co.locatime.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
public class LoginActivity extends Activity implements View.OnClickListener {
    EditText user_id,user_pw;
    CheckBox checkBox;
    Button button,button2;
    Vibrator vibrator;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_id = (EditText)findViewById(R.id.user_id);
        user_id.setPrivateImeOptions("defaultInputmode=english;");
        user_pw = (EditText)findViewById(R.id.user_pw);
        user_pw.setPrivateImeOptions("defaultInputmode=english;");


        preferences=getSharedPreferences("LocaTime", MODE_PRIVATE);
        Boolean checked=preferences.getBoolean("ID/PW_SAVE",false);
        checkBox=(CheckBox) findViewById(R.id.checkBox);
        checkBox.setChecked(checked);
        if(checkBox.isChecked()==true){
            user_id.setText(preferences.getString("user_id_login",""));
            user_pw.setText(preferences.getString("user_pw",""));
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = preferences.edit();
                if (isChecked == true) {
                    Toast.makeText(getApplicationContext(), "ID/PW 저장 되었습니다.", Toast.LENGTH_LONG).show();
                    editor.putBoolean("ID/PW_SAVE", true);
                    editor.commit();
                } else if (isChecked == false) {
                    editor.putBoolean("ID/PW_SAVE", false);
                    editor.commit();
                }
            }
        });

        button=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);


        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        button.setOnClickListener(this);
        button2.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.button:

                intent = new Intent(
                        getApplicationContext(), RegistUActivity.class);
                startActivity(intent);
                break;

            case R.id.button2:

                if(user_id.getText().equals("") || user_pw.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력하세요!!!", Toast.LENGTH_LONG).show();
                    vibrator.vibrate(500);
                }

                ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
                httpParams.add(new BasicNameValuePair("user_id", user_id.getText().toString()));
                httpParams.add(new BasicNameValuePair("user_pw", user_pw.getText().toString()));

                LocaHttp locaHttp=new LocaHttp();
                String url=getBaseContext().getText(R.string.server_url)
                        +"member/login";
                Log.d("url", url);

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

                int result=locaHttp.LoginAction(url, httpParams);
                Log.d("result_test",""+result);
                if(result==1){
                    Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                    intent = new Intent(
                            getApplicationContext(), DivisionActivity.class);

                    SharedPreferences prefs=getSharedPreferences("LocaTime",MODE_PRIVATE);
                    SharedPreferences.Editor editor=prefs.edit();

                    editor.putString("user_id",user_id.getText().toString());
                    editor.putBoolean("login",true);
                    if(checkBox.isChecked()==true){
                        editor.putString("user_id_login",user_id.getText().toString());
                        editor.putString("user_pw",user_pw.getText().toString());
                    }
                    editor.commit();



                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }


}