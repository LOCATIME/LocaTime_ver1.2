package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

class noticeMainItem {
	String subject;
	String reg_date;

	noticeMainItem(String subject_tmp, String reg_date_tmp) {
		subject = subject_tmp;
		reg_date = reg_date_tmp;
	}
}

public class noticeListAdapter extends BaseAdapter {
	LayoutInflater inflater;
	ArrayList<noticeMainItem> noticeMA;
	HashMap<String, String> hm;
	Context mContext;
	int mListLayout;
	private int[] listRowColora;
	public String TAG = "noticeListAdapter";

	public noticeListAdapter(Context tContext, int listLayout,
			ArrayList<noticeMainItem> noticeMAT) {
		mContext = tContext;
		mListLayout = listLayout;
		noticeMA = noticeMAT;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listRowColora = new int[] {
				tContext.getResources().getColor(R.color.list_row_bg),
				tContext.getResources().getColor(R.color.list_row_bg_alt) };
	}

	public noticeListAdapter(Context tContext, int listLayout,
			HashMap<String, String> hmTmp) {
		mContext = tContext;
		mListLayout = listLayout;
		hm = hmTmp;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listRowColora = new int[] {
				tContext.getResources().getColor(R.color.list_row_bg),
				tContext.getResources().getColor(R.color.list_row_bg_alt) };
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return noticeMA.size();
	}

	@Override
	public Object getItem(int rowNum) {
		// TODO Auto-generated method stub
		return noticeMA.get(rowNum);
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

		// TextView notice_subject = (TextView) convertView
		// .findViewById(R.id.notice_subject);
		// notice_subject.setText(noticeMA.get(position).subject);
		//	
		// TextView notice_reg_date = (TextView) convertView
		// .findViewById(R.id.notice_reg_date);
		// notice_reg_date.setText(noticeMA.get(position).reg_date);

		TextView notice_subject = (TextView) convertView
				.findViewById(R.id.notice_subject);
		notice_subject.setText(hm.get("rowid[" + position + "]") + ". "
				+ noticeMA.get(position).subject);

		TextView notice_reg_date = (TextView) convertView
				.findViewById(R.id.notice_reg_date);
		notice_reg_date.setText(noticeMA.get(position).reg_date);

		int colorPos = position % listRowColora.length;
		convertView.setBackgroundColor(listRowColora[colorPos]);

		final int positionInt = position;

		ImageButton noticeDetailBtn = (ImageButton) convertView
				.findViewById(R.id.noticeDetailBtn);
		Log.v(TAG, noticeDetailBtn.toString());

		try {
			noticeDetailBtn.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					noticeInfo(positionInt);
					// String tmp = hm.get("content[" + positionInt + "]");
					// Toast.makeText(mContext, tmp, Toast.LENGTH_SHORT).show();
					// Log.v(TAG, tmp);
				}
			});
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

		return convertView;
	}

	public void noticeInfo(int position) {

		Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.notice_detail);
		dialog.setTitle("공지사항");
		TextView noticeDetailSubject = (TextView) dialog
				.findViewById(R.id.noticeDetailSubject);
		noticeDetailSubject.setText(hm.get("rowid[" + position + "]") + ". "
				+ hm.get("subject[" + position + "]"));
		TextView noticeDetailInfo = (TextView) dialog
				.findViewById(R.id.noticeDetailInfo);
		noticeDetailInfo.setText(hm.get("reg_date[" + position + "]"));
		
		
//		ClassLoader.getSystemResource("com/owl/app/notice")
		
		TextView noticeDetailContent = (TextView) dialog
				.findViewById(R.id.noticeDetailContent);
		noticeDetailContent.setText(hm.get("content[" + position + "]"));
		noticeDetailContent.setMovementMethod(new ScrollingMovementMethod());
		dialog.show();
		Button buttonOK = (Button) dialog.findViewById(R.id.noticeDailtOKBtn);
		buttonOK.setOnClickListener(new noticeDetailOKListener(dialog));

	}

	protected class noticeDetailOKListener implements OnClickListener {
		private Dialog dialog;

		public noticeDetailOKListener(Dialog dialog) {
			this.dialog = dialog;
		}

		public void onClick(View v) {
			dialog.dismiss();
		}
	}
}
