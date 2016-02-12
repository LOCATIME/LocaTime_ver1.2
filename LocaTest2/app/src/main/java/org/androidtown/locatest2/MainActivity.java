package org.androidtown.locatest2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ContentAdapter adapter;
    Button btnReceive;
    List<ContentVO> list;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        btnReceive=(Button) findViewById(R.id.btnReceive);
        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1001);

                dialog.dismiss();

                String requestURL = getBaseContext().getText(R.string.site_url) + "member/selectContent";
                LocaHttp locaHttp = new LocaHttp();
                list=locaHttp.receiveContentAll(requestURL);

                adapter = new ContentAdapter(getApplicationContext(), list, R.layout.item_list);
                listView.setAdapter(adapter);

            }
        });






    }

    public Dialog onCreateDialog(int id) {
        switch (id){
            case(1001):
            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("데이터 확인 중입니다.");
            return dialog;
        }
        return null;

    }



}
