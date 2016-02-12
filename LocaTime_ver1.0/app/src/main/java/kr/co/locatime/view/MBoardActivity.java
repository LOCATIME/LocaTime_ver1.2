package kr.co.locatime.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import kr.co.locatime.Network.LocaHttp;
import kr.co.locatime.R;
import kr.co.locatime.adapter.ContentAdapter;
import kr.co.locatime.domain.ContentVO;

/**
 * Created by Joonha on 2015-11-26.
 */
public class MBoardActivity extends Activity {
    ListView listView;
    ContentAdapter adapter;

    ImageButton btnHome,btnBack,btnMypage;

    ImageButton btnPriceRefresh;
    ImageButton btnDeadLine;
    TextView titleBuy;


    List<ContentVO> list;

    public static final int PROGRESS_DIALOG=1001;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mboard);

        listView = (ListView) findViewById(R.id.listView);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        String requestURL = getBaseContext().getText(R.string.server_url) + "member/selectContent";

        titleBuy=(TextView) findViewById(R.id.titleBuy);
        Typeface font = Typeface.createFromAsset(
                getApplicationContext().getAssets(), "fonts/font_ssanai_thin.ttf");
        titleBuy.setTypeface(font);

        LocaHttp locaHttp = new LocaHttp();

        list = locaHttp.receiveContentAll(requestURL);
        adapter = new ContentAdapter(getApplicationContext(), list, R.layout.item_list);
        listView.setAdapter(adapter);

        btnDeadLine=(ImageButton) findViewById(R.id.btnDeadLine);
        btnDeadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requestURL = getBaseContext().getText(R.string.server_url) + "member/selectContent";
                LocaHttp locaHttp = new LocaHttp();
                list = locaHttp.receiveContentAll(requestURL);
                Log.d("list_", list.toString());
                adapter = new ContentAdapter(getApplicationContext(), list, R.layout.item_list);
                listView.setAdapter(adapter);

            }
        });

        btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        btnPriceRefresh=(ImageButton)findViewById(R.id.btnPriceRefresh);
        btnPriceRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requestURL = getBaseContext().getText(R.string.server_url) + "member/selectContentPrice";
                LocaHttp locaHttp = new LocaHttp();
                list = locaHttp.receiveContentAllPrice(requestURL);
                Log.d("list_price",list.toString());
                adapter = new ContentAdapter(getApplicationContext(), list, R.layout.item_list);
                listView.setAdapter(adapter);

            }
        });

    }

}
