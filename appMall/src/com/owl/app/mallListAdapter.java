package com.owl.app;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.owl.app.cms.util;

public class mallListAdapter extends BaseAdapter {
	LayoutInflater inflater;
	HashMap<String, String> hm;
	Context mContext;
	int mListLayout;
	public int[] listRowColora;
	public String TAG = "mallListAdapter";
	public int listCount = 0;
	public util cmsutil = new util();

	public mallListAdapter(Context tContext, int listLayout,
			HashMap<String, String> hmTmp) {
		mContext = tContext;
		mListLayout = listLayout;
		hm = hmTmp;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (hm != null) {
			listCount = cmsutil.str2int(hm.get("count"));
		}
		listRowColora = new int[] {
				tContext.getResources().getColor(R.color.list_row_bg),
				tContext.getResources().getColor(R.color.list_row_bg_alt) };
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listCount;
	}

	@Override
	public Object getItem(int rowNum) {
		// TODO Auto-generated method stub
		return hm.get("rowid[" + rowNum + "]");
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public HashMap<String, Bitmap> hmImg = new HashMap<String, Bitmap>();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(mListLayout, parent, false);
		}

		if (listRowColora != null) {
			if (listRowColora.length > 0) {
				int colorPos = position % listRowColora.length;
				convertView.setBackgroundColor(listRowColora[colorPos]);
			}
		}

		final int positionInt = position;
		
		Uri uri = Uri.parse(hm.get("img[" + position + "]"));
		Log.v(TAG, uri.getPath());

		try {
			if (hmImg.get("img[" + position + "]") == null) {
				String urlstr = hm.get("img[" + position + "]");
				URL url = new URL(urlstr);
				URLConnection conn = url.openConnection();
				conn.connect();
				BufferedInputStream bis = new BufferedInputStream(conn
						.getInputStream(), 512 * 1024);
				Bitmap bm = BitmapFactory.decodeStream(bis);
				bis.close();
				hmImg.put("img[" + position + "]", bm);
			}
			if (hmImg.get("img[" + position + "]") != null) {
				((ImageView) convertView.findViewById(R.id.prodImg))
						.setImageBitmap(hmImg.get("img[" + position + "]"));
			}

		} catch (IOException e) {
			Log.e(TAG, e.toString());
			((ImageView) convertView.findViewById(R.id.prodImg))
					.setImageDrawable(convertView.getResources().getDrawable(
							R.drawable.blank));
		}

		((TextView) convertView.findViewById(R.id.prodSort)).setText(hm
				.get("prodSort[" + position + "]"));
		((TextView) convertView.findViewById(R.id.prodName)).setText(hm
				.get("prodName[" + position + "]"));
		((TextView) convertView.findViewById(R.id.prodBrief)).setText(hm
				.get("prodBrief[" + position + "]"));
		((TextView) convertView.findViewById(R.id.prodPriceText)).setText(hm
				.get("priceText[" + position + "]"));
		((TextView) convertView.findViewById(R.id.rating)).setText(hm
				.get("rating[" + position + "]"));
		((TextView) convertView.findViewById(R.id.ratingText)).setText(hm
				.get("ratingText[" + position + "]"));

		ImageButton detailBtn = (ImageButton) convertView
				.findViewById(R.id.detailBtn);
		detailBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				detailInfo(positionInt);
			}
		});

		return convertView;
	}

	public void detailInfo(int position) {
		String rowid = hm.get("rowid[" + position + "]");
		Activity actNext = cmsutil.findAct(mContext, mContext.getPackageName()
				+ ".mallDetail");
		if (actNext != null) {
			Intent intent = new Intent(mContext, actNext.getClass());
			intent.putExtra("rowid", rowid);
			mContext.startActivity(intent);
		}

	}

}
