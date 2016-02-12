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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.owl.app.cms.util;

public class mallCartAdapter extends BaseAdapter {
	LayoutInflater inflater;
	HashMap<String, String> hm;
	Context mContext;
	int mListLayout;
	public int[] listRowColora;
	public String TAG = "mallCartAdapter";
	public int listCount = 0;
	public util cmsutil = new util();
	Activity act;

	public mallCartAdapter(Activity actTmp, Context tContext, int listLayout,
			HashMap<String, String> hmTmp) {
		act = actTmp;
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
	public HashMap<String, String> hmViewed = new HashMap<String, String>();

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
		String viewed = cmsutil.null2empty(hmViewed.get("viewed"));
		if (viewed.indexOf(","+position+",")<0) { 
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
					.get("prodsort[" + position + "]"));
			((TextView) convertView.findViewById(R.id.prodName)).setText(hm
					.get("prodname[" + position + "]"));
			String priceText = "- 가격: "
					+ cmsutil.number_format(hm.get("price[" + position + "]"));
			((TextView) convertView.findViewById(R.id.prodPriceText))
					.setText(priceText);
	
			ImageButton detailBtn = (ImageButton) convertView
					.findViewById(R.id.detailBtn);
			detailBtn.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					detailInfo(positionInt);
				}
			});
	
			initSpinner(position, convertView);
			if (viewed.length()<=0) viewed = ",";
			hmViewed.put("viewed", viewed + position + ",");
		}

		return convertView;
	}
	
	public void detailInfo(int position) {
		
		String rowid = hm.get("rowid[" + position + "]");
		Activity actNext = cmsutil.findAct(mContext, mContext.getPackageName()
				+ ".mallDetail");
		if (actNext != null) {
			((owllab) act.getApplication()).startLoading(act);
			Intent intent = new Intent(mContext, actNext.getClass());
			intent.putExtra("rowid", rowid);
			mContext.startActivity(intent);
			((owllab) act.getApplication()).endLoading();
		}
		
	}
	
	public void initSpinner(int position, View convertView) {
		
		Log.v(TAG, "[initSpinner]");
		
		final int positionInt = position;
		
		int maxEa = cmsutil.str2int(hm.get("maxea[" + position + "]"), 0);
		int ea = cmsutil.str2int(hm.get("ea[" + positionInt + "]"),0);
		final String eaText = ""+ea;
		String spinnerOptiona[];
		spinnerOptiona = new String[maxEa + 1];
		for (int i = 0; i <= maxEa; i++) {
			spinnerOptiona[i] = String.valueOf(i);
		}

		Spinner eaSpinner = (Spinner) convertView.findViewById(R.id.ea);

		if (maxEa <= 0) {
			spinnerOptiona = new String[1];
			spinnerOptiona[0] = "0";
			eaSpinner.setEnabled(false);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, spinnerOptiona);
		eaSpinner.setAdapter(adapter);
		eaSpinner.setSelection(adapter.getPosition(eaText));

		
		eaSpinner
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapter, View v, int i, long lng) {
				
				if (!eaText.equals(adapter.getSelectedItem().toString())) {
					((owllab) act.getApplication()).startLoading(act);
					Activity actNext = cmsutil.findAct(act, act.getPackageName()
							+ ".mallCart");
					if (actNext != null) {
						Intent intent = new Intent(act, actNext.getClass());
						intent.putExtra("rowid", hm.get("rowid[" + positionInt + "]"));
						intent.putExtra("ea", cmsutil.str2int(adapter.getSelectedItem().toString(),0));
						intent.putExtra("mode", "edit");
						act.startActivity(intent);
					}
					((owllab) act.getApplication()).endLoading();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});

	}
}
