package kr.co.locatime.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import kr.co.locatime.Network.LocaHttp;
import kr.co.locatime.R;
import kr.co.locatime.adapter.MyContentAdapter;
import kr.co.locatime.domain.ContentVO;
import kr.co.locatime.domain.MemberVO;

/**
 * Created by Joonha on 2015-11-26.
 */
public class MyPageActivity extends Activity {
    List<ContentVO> list;
    ListView listView;
    String user_id;
    MyContentAdapter adapter;
    LocaHttp locaHttp;
    ArrayList<NameValuePair> httpParams;

    String requestURL;

    TextView txtName,txtId,titleMypage;

    EditText edtPhone,edtKakaoId,edtPw,edtPwConfirm;

    Button btnUpdate,btnDeleteMember,btnLogout;
    ImageButton btnBack,btnMypage,btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        SharedPreferences prefs=getSharedPreferences("LocaTime",MODE_PRIVATE);
        user_id=prefs.getString("user_id","");
        Log.d("user_id", user_id);
        listView = (ListView) findViewById(R.id.myListView);

        //나의 정보.
        requestURL=getBaseContext().getText(R.string.server_url) + "member/myPage";
        locaHttp = new LocaHttp();
        httpParams = new ArrayList<NameValuePair>();
        httpParams.add(new BasicNameValuePair("user_id",user_id));

        MemberVO memberVO=new MemberVO();
        memberVO=locaHttp.myPage(requestURL,httpParams);

        txtName=(TextView)findViewById(R.id.txtName);
        txtName.setText(memberVO.getUser_name());

        txtId=(TextView) findViewById(R.id.txtId);
        txtId.setText(memberVO.getUser_id());

        edtPhone=(EditText)findViewById(R.id.edtPhone);
        edtPhone.setText(memberVO.getUser_phone());

        edtKakaoId=(EditText)findViewById(R.id.edtKakaoId);
        edtKakaoId.setText(memberVO.getUser_kakao());

        edtPw=(EditText)findViewById(R.id.edtPw);
        edtPwConfirm=(EditText)findViewById(R.id.edtPwConfirm);

        Typeface font = Typeface.createFromAsset(
                getApplicationContext().getAssets(), "fonts/font_ssanai_thin.ttf");
        titleMypage=(TextView)findViewById(R.id.titleMypage);
        titleMypage.setTypeface(font);

        //정보수정.
        btnUpdate=(Button)findViewById(R.id.btnUpdate);
        btnUpdate.setTypeface(font);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPw.getText().toString().equals(edtPwConfirm.getText().toString())) {
                    requestURL = getBaseContext().getText(R.string.server_url) + "member/updateMember";
                    locaHttp = new LocaHttp();
                    httpParams = new ArrayList<NameValuePair>();
                    httpParams.add(new BasicNameValuePair("user_id", user_id));
                    httpParams.add(new BasicNameValuePair("user_phone", edtPhone.getText().toString()));
                    httpParams.add(new BasicNameValuePair("user_kakao", edtKakaoId.getText().toString()));
                    httpParams.add(new BasicNameValuePair("user_pw", edtPw.getText().toString()));

                    locaHttp.updateMember(requestURL, httpParams);
                    Toast.makeText(getApplicationContext(),"정상적으로 수정 되었습니다.", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
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

        //나의 판매목록 불러오기.
        requestURL = getBaseContext().getText(R.string.server_url) + "member/selectUserContent";
        locaHttp = new LocaHttp();
        httpParams = new ArrayList<NameValuePair>();
        httpParams.add(new BasicNameValuePair("user_id", user_id));

        list = locaHttp.receiveContentAll(requestURL,httpParams);
        adapter = new MyContentAdapter(getApplicationContext(), list, R.layout.myitem_list);
        listView.setAdapter(adapter);

        btnDeleteMember=(Button)findViewById(R.id.btnDeleteMember);
        btnDeleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"회원 탈퇴 처리해야함.",Toast.LENGTH_LONG).show();
            }
        });
        btnLogout=(Button)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "로그아웃 처리해야함.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
