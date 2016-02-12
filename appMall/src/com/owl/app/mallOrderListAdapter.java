package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.owl.app.cms.util;

public class mallOrderListAdapter extends BaseAdapter {
	LayoutInflater inflater;
	HashMap<String, String> hm;
	Context mContext;
	int mListLayout;
	public int[] listRowColora;
	public String TAG = "mallOrderListAdapter";
	public int listCount = 0;
	public util cmsutil = new util();
	Activity act;

	public mallOrderListAdapter(Activity actTmp, Context tContext,
			int listLayout, HashMap<String, String> hmTmp) {
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
				tContext.getResources().getColor(R.color.list_row_bg_alt) 
				};
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

	public HashMap<String, String> hmViewed = new HashMap<String, String>();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(mListLayout, parent, false);
		}

		// if (listRowColora != null) {
		// if (listRowColora.length > 0) {
		// int colorPos = position % listRowColora.length;
		// convertView.setBackgroundColor(listRowColora[colorPos]);
		// }
		// }

		final int positionInt = position;
		String viewed = cmsutil.null2empty(hmViewed.get("viewed"));
		if (viewed.indexOf("," + position + ",") < 0) {
			String orderTitleText = "[" + hm.get("rowid[" + position + "]")
					+ "] " + hm.get("products[" + position + "]") 
					+ " / 주문일 : "
					+ hm.get("regDate[" + position + "]");
			((TextView) convertView.findViewById(R.id.orderTitle))
					.setText(orderTitleText);

			String orderContentText = cmsutil.number_format(hm
					.get("totalPrice[" + position + "]")) + "원"
					+ " / Tel:" + hm.get("orderTel[" + position + "]")
					+ " / 배송지:" + hm.get("orderZipcode[" + position + "]")
					+ " " + hm.get("orderAddress[" + position + "]")
					+ " " + hm.get("orderName[" + position + "]")
					+ " / 입금정보:" + hm.get("orderBank[" + position + "]")
					+ " " + hm.get("orderPayer[" + position + "]");
			String memo = cmsutil.null2empty(hm.get("orderMemo[" + position
					+ "]"));
			if (!"".equals(memo))
				orderContentText += memo;
			((TextView) convertView.findViewById(R.id.orderContent))
					.setText(orderContentText);
			ImageButton detailBtn = (ImageButton) convertView
					.findViewById(R.id.detailBtn);
			detailBtn.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					detailInfo(positionInt);
				}
			});
			initSpinner(position, convertView);
			if (viewed.length() <= 0)
				viewed = ",";
			hmViewed.put("viewed", viewed + position + ",");
		}//if viewed
		return convertView;
	}

	// public void detailInfo(int position) {
	//		
	// String o = hm.get("o[" + position + "]");
	// Activity actNext = cmsutil.findAct(mContext, mContext.getPackageName()
	// + ".mallOrderDetail");
	// if (actNext != null) {
	// ((owllab) act.getApplication()).startLoading(act);
	// Intent intent = new Intent(mContext, actNext.getClass());
	// intent.putExtra("o", o);
	// mContext.startActivity(intent);
	// ((owllab) act.getApplication()).endLoading();
	// }
	//		
	// }

	public void detailInfo(int position) {
		String o = hm.get("o[" + position + "]");
		String theUrl = "http://www.owllab.com/android/mall_order_detail.php";
		Log.i(TAG, theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("o", o));
		cmsHTTP cmsHttp = new cmsHTTP();
		cmsHttp.act = act;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);
		if (tmpData == null)
			return;
		Log.i(TAG, tmpData);
		util cmsutil = new util();
		HashMap<String, String> hmCart = new HashMap<String, String>();
		hmCart = cmsutil.xml2HashMap(tmpData, cmsHttp.encoding);

		HashMap<String, String> hmPrice = cmsutil.calcPrice(hmCart);
		String deliveryPriceLabel = "※ 배송료 : ";
		String totalPriceLabel = "※ 합계금액 : ";
		String priceEnd = "원";
		int totalPriceInt = cmsutil.str2int(hm.get("totalPrice[" + position
				+ "]"), 0);
		int deliveryPriceInt = totalPriceInt
				- cmsutil.str2int(hmPrice.get("orgPrice"), 0);

		Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.mall_order_detail);
		dialog.setTitle("주문내역");

		mallOrderDetailAdapter mallOrderDetailAdapter 
		= new mallOrderDetailAdapter(
				act, act, R.layout.mall_order_detail_row, hmCart);
		ListView listView = (ListView) dialog.findViewById(R.id.listView);
		listView.setAdapter(mallOrderDetailAdapter);
		((TextView) dialog.findViewById(R.id.deliveryPrice))
				.setText(deliveryPriceLabel
						+ Integer.toString(deliveryPriceInt) + priceEnd);
		((TextView) dialog.findViewById(R.id.totalPrice))
				.setText(totalPriceLabel + Integer.toString(totalPriceInt)
						+ priceEnd);
		dialog.show();
		Button buttonOK = (Button) dialog.findViewById(R.id.closeBtn);
		buttonOK.setOnClickListener(new closeListener(dialog));
	}

	protected class closeListener implements OnClickListener {
		private Dialog dialog;

		public closeListener(Dialog dialog) {
			this.dialog = dialog;
		}

		public void onClick(View v) {
			dialog.dismiss();
		}
	}

	public void initSpinner(int position, View convertView) {
		Log.v(TAG, "[initSpinner]");
		final int positionInt = position;

		Spinner stateSpinner = (Spinner) convertView
				.findViewById(R.id.stateSpinner);
		final String state = cmsutil.null2empty(hm.get("state[" + position
				+ "]"));

		String[] spinnerOptiona;
		if ("입금전".equals(state)) {
			spinnerOptiona = new String[2];
			spinnerOptiona[0] = "입금전";
			spinnerOptiona[1] = "주문취소";
		} else {
			spinnerOptiona = new String[1];
			spinnerOptiona[0] = state;
			stateSpinner.setEnabled(false);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item, spinnerOptiona);
		stateSpinner.setAdapter(adapter);

		stateSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> adapter, View v, int i, long lng) {

						if (!state.equals(adapter.getSelectedItem().toString())) {
							((owllab) act.getApplication()).startLoading(act);
							Activity actNext = cmsutil.findAct(act, act.getPackageName()
									+ ".mallOrderList");
							if (actNext != null) {
								Intent intent = new Intent(act, actNext.getClass());
								intent.putExtra("pg", hm.get("pg[" + positionInt + "]"));
								intent.putExtra("o", hm.get("o[" + positionInt + "]"));
								intent.putExtra("state", adapter .getSelectedItem().toString());
								intent.putExtra("mode", "chState");
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
