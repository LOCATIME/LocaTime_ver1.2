package com.example.student.locatime;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   EditText editText,editText2;
   CheckBox checkBox;
   Button button,button2,button3;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);

        checkBox=(CheckBox)findViewById(R.id.checkBox);

        button=(Button)findViewById(R.id.button);
        button2=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:


            break;

            case R.id.button2:

                if(editText.getText().equals("") || editText2.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"아뒤나 비번 입력하세요!!!",Toast.LENGTH_LONG).show();
                }
                vibrator.vibrate(500);//진동500은 5초간
               /* if(cnt>5){
                    vibrator.vibrate(500);//진동500은 5초간
                }*/
              /*  Toast.makeText(getApplicationContext(),"회원가입 되었습니다",Toast.LENGTH_LONG).show();*/
            break;

            case R.id.button3:
                Log.d("clear", "지움 버튼이 클릭");
                editText.setText("");
                editText2.setText("");
                checkBox.setChecked(false);
            break;
        }

    }
}
