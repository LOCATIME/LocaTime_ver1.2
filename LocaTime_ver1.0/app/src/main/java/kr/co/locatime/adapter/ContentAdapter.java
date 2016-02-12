package kr.co.locatime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

import kr.co.locatime.R;
import kr.co.locatime.domain.ContentVO;

/**
 * Created by Joonha on 2015-11-26.
 */
public class ContentAdapter extends BaseAdapter {
    Context context;
    List<ContentVO> list;
    LayoutInflater inflater;
    int layout;
    Button btnDetail;

    public ContentAdapter() {
    }

    public ContentAdapter(Context context, List<ContentVO> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if(convertView ==null){
            convertView= inflater.inflate(layout,null);
            TextView content_title=(TextView)convertView.findViewById(R.id.content_title);
            TextView content_OriPrice=(TextView)convertView.findViewById(R.id.content_OriPrice);
            TextView content_deadline=(TextView)convertView.findViewById(R.id.content_deadline);
            TextView content_SellingPrice=(TextView)convertView.findViewById(R.id.content_SellingPrice);
            TextView user_kakao=(TextView)convertView.findViewById(R.id.user_kakao);


            content_title.setText(String.valueOf(list.get(position).getContent_title()));
            content_OriPrice.setText(String.valueOf(list.get(position).getContent_OriPrice()));
            content_deadline.setText(String.valueOf(list.get(position).getContent_deadline()));
            content_SellingPrice.setText(String.valueOf(list.get(position).getContent_SellingPrice()));
            user_kakao.setText(String.valueOf(list.get(position).getUser_kakao()));
        }
        final RelativeLayout detailView = (RelativeLayout) convertView.findViewById(R.id.detailView);
        final TableLayout content_info=(TableLayout)convertView.findViewById(R.id.content_info);
        btnDetail=(Button)convertView.findViewById(R.id.btnDetail);

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailView.getVisibility()==View.GONE){
                    detailView.setVisibility(View.VISIBLE);
                    btnDetail=(Button)v.findViewById(R.id.btnDetail);
                    btnDetail.setText("접어두기");
                    content_info.setBackgroundColor(0xFFDCD6D6);
                }
                else {
                    detailView.setVisibility(View.GONE);
                    btnDetail=(Button)v.findViewById(R.id.btnDetail);
                    btnDetail.setText("상세보기");
                    content_info.setBackgroundColor(0xFFFFFFFF);
                }
            }
        });




        return convertView;

    }


}
