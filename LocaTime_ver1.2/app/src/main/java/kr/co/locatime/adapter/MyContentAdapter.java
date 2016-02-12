package kr.co.locatime.adapter;

import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import kr.co.locatime.Network.LocaHttp;
import kr.co.locatime.R;
import kr.co.locatime.domain.ContentVO;

/**
 * Created by Joonha on 2015-11-26.
 */
public class MyContentAdapter extends BaseAdapter {
    Context context;
    List<ContentVO> list;
    LayoutInflater inflater;
    int layout;
    Button btnAble;
    Button btnDetail;

    public MyContentAdapter() {
    }

    public MyContentAdapter(Context context, List<ContentVO> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, null);
            TextView content_title = (TextView) convertView.findViewById(R.id.content_title);
            TextView content_OriPrice = (TextView) convertView.findViewById(R.id.content_OriPrice);
            TextView content_deadline = (TextView) convertView.findViewById(R.id.content_deadline);
            TextView content_SellingPrice = (TextView) convertView.findViewById(R.id.content_SellingPrice);
            TextView user_kakao = (TextView) convertView.findViewById(R.id.user_kakao);
            btnAble = (Button) convertView.findViewById(R.id.btnAble);

            content_title.setText(String.valueOf(list.get(position).getContent_title()));
            content_OriPrice.setText(String.valueOf(list.get(position).getContent_OriPrice()));
            content_deadline.setText(String.valueOf(list.get(position).getContent_deadline()));
            content_SellingPrice.setText(String.valueOf(list.get(position).getContent_SellingPrice()));
            user_kakao.setText(String.valueOf(list.get(position).getUser_kakao()));
            if (list.get(position).getContent_able() == 1) {
                btnAble.setText("판매완료");
            } else {
                btnAble.setText("판매가능");
            }

            btnAble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).getContent_able() == 0) {
                        btnAble = (Button) v.findViewById(R.id.btnAble);
                        btnAble.setText("판매완료");
                        Toast.makeText(context, "판매완료 처리되었습니다.", Toast.LENGTH_LONG).show();
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

                        LocaHttp locaHttp = new LocaHttp();
                        ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
                        httpParams.add(new BasicNameValuePair("content_num", String.valueOf(list.get(position).getContent_num())));
                        httpParams.add(new BasicNameValuePair("content_able", "1"));
                        String url = context.getText(R.string.server_url) + "member/updateAble";

                        locaHttp.updateAble(url, httpParams);
                        list.get(position).setContent_able(1);
                    } else {
                        btnAble = (Button) v.findViewById(R.id.btnAble);
                        btnAble.setText("판매가능");
                        Toast.makeText(context, "판매 가능 하도록 처리되었습니다.", Toast.LENGTH_LONG).show();
                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

                        LocaHttp locaHttp = new LocaHttp();
                        ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
                        httpParams.add(new BasicNameValuePair("content_num", String.valueOf(list.get(position).getContent_num())));
                        httpParams.add(new BasicNameValuePair("content_able", "0"));
                        String url = context.getText(R.string.server_url) + "member/updateAble";

                        locaHttp.updateAble(url, httpParams);
                        list.get(position).setContent_able(0);
                    }
                }
            });


        }
        final RelativeLayout detailView = (RelativeLayout) convertView.findViewById(R.id.detailView);
        final TableLayout myContentInfo = (TableLayout) convertView.findViewById(R.id.myContentInfo);
        btnDetail = (Button) convertView.findViewById(R.id.btnDetail);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailView.getVisibility() == View.GONE) {
                    detailView.setVisibility(View.VISIBLE);
                    btnDetail = (Button) v.findViewById(R.id.btnDetail);
                    btnDetail.setText("접어두기");
                    myContentInfo.setBackgroundColor(0xFFDCD6D6);
                } else {
                    detailView.setVisibility(View.GONE);
                    btnDetail = (Button) v.findViewById(R.id.btnDetail);
                    btnDetail.setText("상세보기");
                    myContentInfo.setBackgroundColor(0xFFFFFFFF);
                }
            }
        });
        return convertView;

    }


}
