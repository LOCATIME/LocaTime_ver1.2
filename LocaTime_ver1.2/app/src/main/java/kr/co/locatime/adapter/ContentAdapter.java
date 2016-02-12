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

import java.util.ArrayList;

import kr.co.locatime.R;
import kr.co.locatime.domain.ContentVO;

/**
 * Created by Joonha on 2015-11-26.
 */
public class ContentAdapter extends BaseAdapter {
    Context context;
    ArrayList<ContentVO> list;
    LayoutInflater inflater;
    int layout;

    public ContentAdapter() {
    }

    public ContentAdapter(Context context, ArrayList<ContentVO> list, int layout) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(layout, null);
            holder = new ViewHolder();

            holder.content_title = (TextView) view.findViewById(R.id.content_title);
            holder.content_OriPrice = (TextView) view.findViewById(R.id.content_OriPrice);
            holder.content_deadline = (TextView) view.findViewById(R.id.content_deadline);
            holder.content_SellingPrice = (TextView) view.findViewById(R.id.content_SellingPrice);
            holder.user_kakao = (TextView) view.findViewById(R.id.user_kakao);
            holder.btnDetail = (Button) view.findViewById(R.id.btnDetail);
            holder.detailView = (RelativeLayout) view.findViewById(R.id.detailView);
            holder.content_info = (TableLayout) view.findViewById(R.id.content_info);
            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.content_title.setText(String.valueOf(list.get(position).getContent_title()));
        holder.content_OriPrice.setText(String.valueOf(list.get(position).getContent_OriPrice()));
        holder.content_deadline.setText(String.valueOf(list.get(position).getContent_deadline()));
        holder.content_SellingPrice.setText(String.valueOf(list.get(position).getContent_SellingPrice()));
        holder.user_kakao.setText(String.valueOf(list.get(position).getUser_kakao()));


        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (holder.detailView.getVisibility() == View.GONE) {
                    holder.detailView.setVisibility(View.VISIBLE);
                    holder.btnDetail = (Button) v.findViewById(R.id.btnDetail);
                    holder.btnDetail.setText("접어두기");
                    holder.content_info.setBackgroundColor(0xFFDCD6D6);
                } else {
                    holder.detailView.setVisibility(View.GONE);
                    holder.btnDetail = (Button) v.findViewById(R.id.btnDetail);
                    holder.btnDetail.setText("상세보기");
                    holder.content_info.setBackgroundColor(0xFFFFFFFF);
                }
            }
        });

        return view;
    }

    static class ViewHolder {
        RelativeLayout detailView;
        TableLayout content_info;

        Button btnDetail;

        TextView content_title;
        TextView content_OriPrice;
        TextView content_deadline;
        TextView content_SellingPrice;
        TextView user_kakao;
    }


}
