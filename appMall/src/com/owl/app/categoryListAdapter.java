package com.owl.app;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.owl.app.cms.util;

public class categoryListAdapter extends BaseAdapter {
	LayoutInflater inflater;
	HashMap<String, String> hm;
	Context mContext;
	int mListLayout;
	public int[] listRowColora;
	public String TAG = "categoryListAdapter";
	public int listCount = 0;
	public util cmsutil = new util();

	public categoryListAdapter(Context tContext, int listLayout,
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

		TextView category_subject = (TextView) convertView
				.findViewById(R.id.category_subject);
		category_subject.setText(hm.get("subject[" + position + "]"));
		category_subject.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				categoryInfo(positionInt);
			}
		});

		ImageButton categoryDetailBtn = (ImageButton) convertView
				.findViewById(R.id.categoryDetailBtn);
		categoryDetailBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				categoryInfo(positionInt);
			}
		});

		return convertView;
	}

	public void categoryInfo(int position) {
		String menuClass = hm.get("menuClass[" + position + "]");
		String findClass = "";
		Class<?> cls;
		Activity actCategory;
		try {
			cls = Class.forName(menuClass);
			actCategory = (Activity) cls.newInstance();
			Intent intent = new Intent(mContext, actCategory.getClass());
			intent.putExtra("tabId", "tab3");//추가
			intent.putExtra("sort", hm.get("menuCode[" + position + "]"));//추가
			intent.putExtra("subject", hm.get("subject[" + position + "]"));//추가
			mContext.startActivity(intent);
			findClass = cls.getName();
		} catch (ClassNotFoundException e) {
			findClass="";
			AlertDialog dialog = new AlertDialog.Builder(mContext).create();
			dialog.setTitle("안내");
			dialog.setMessage("죄송합니다.\n이 버전에서는 페이지를 찾을 수 없습니다.\n업그레이드하세요.");
			dialog.setButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			dialog.show();
		} catch (InstantiationException e) {
			
		} catch (IllegalAccessException e) {
			
		}
	}


}
