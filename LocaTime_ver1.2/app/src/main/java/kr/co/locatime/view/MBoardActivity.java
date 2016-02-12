package kr.co.locatime.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.locatime.Network.LocaHttp;
import kr.co.locatime.R;
import kr.co.locatime.adapter.ContentAdapter;
import kr.co.locatime.domain.ContentVO;

/**
 * Created by Joonha on 2015-11-26.
 */
public class MBoardActivity extends Activity {
    public static final int PROGRESS_DIALOG = 1001;
    public static Context context;
    ListView listView;
    ContentAdapter adapter;
    ImageButton btnHome, btnMypage;
    ImageButton btnPriceRefresh;
    ImageButton btnDeadLine;
    TextView titleBuy;
    ArrayList<ContentVO> list;
    ProgressDialog progressDialog;
    String requestURL;
    ProgressDialog dialog;
    MboardAsyncTask mboardAsysTask;
    MboardAsyncTaskPriceRefresh mboardAsyncTaskPriceRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mboard);

        context = this;

        titleBuy = (TextView) findViewById(R.id.titleBuy);
        Typeface font = Typeface.createFromAsset(
                getApplicationContext().getAssets(), "fonts/font_ssanai_thin.ttf");
        titleBuy.setTypeface(font);


        mboardAsysTask = new MboardAsyncTask();
        mboardAsysTask.execute();


        btnDeadLine = (ImageButton) findViewById(R.id.btnDeadLine);
        btnDeadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mboardAsysTask = new MboardAsyncTask();
                mboardAsysTask.execute();
            }
        });


        btnPriceRefresh = (ImageButton) findViewById(R.id.btnPriceRefresh);
        btnPriceRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mboardAsyncTaskPriceRefresh = new MboardAsyncTaskPriceRefresh();
                mboardAsyncTaskPriceRefresh.execute();
            }
        });

        //최하단 3인방.

        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

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

    class MboardAsyncTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "데이터 로딩중", "잠시만 기다려주세요.", false);
            dialog.setCanceledOnTouchOutside(true);
            listView = (ListView) findViewById(R.id.listView);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
            requestURL = getBaseContext().getText(R.string.server_url) + "member/selectContent";
        }

        @Override
        protected ArrayList<ContentVO> doInBackground(Object[] params) {
            LocaHttp locaHttp = new LocaHttp();
            list = locaHttp.receiveContentAll(requestURL);
            Log.d("list", list.toString());
            return list;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            adapter = new ContentAdapter(context, list, R.layout.item_list);
            listView.setAdapter(adapter);
            dialog.dismiss();
        }

    }

    class MboardAsyncTaskPriceRefresh extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(context, "데이터 로딩중", "잠시만 기다려주세요.", false);
            dialog.setCanceledOnTouchOutside(true);
            requestURL = getBaseContext().getText(R.string.server_url) + "member/selectContentPrice";
        }

        @Override
        protected ArrayList<ContentVO> doInBackground(Object[] params) {

            LocaHttp locaHttp = new LocaHttp();
            list = locaHttp.receiveContentAllPrice(requestURL);
            Log.d("list_price", list.toString());


            return list;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            adapter = new ContentAdapter(context, list, R.layout.item_list);
            listView.setAdapter(adapter);
            dialog.dismiss();
        }

    }

}
