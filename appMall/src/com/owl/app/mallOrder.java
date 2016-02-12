package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.app.cms.util;

public class mallOrder extends Activity {

	String TAG = "";
	HashMap<String, String> hm = new HashMap<String, String>();
	HashMap<String, String> hmUser = new HashMap<String, String>();
	HashMap<String, String> hmCart = new HashMap<String, String>();
	HashMap<String, String> hmTot = new HashMap<String, String>();
	util cmsutil = new util();
	Activity act = this;
	Intent actIntent;
	Activity actNext;
	ContentResolver cr;
	static final String uriString = "content://com.owl.app.cartProvider/cart";
	static final Uri CONTENT_URI = Uri.parse(uriString);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		setContentView(R.layout.mall_order);
		top top = new top(this);
		top.init();

		cmsutil.act = this;
		TAG = this.getLocalClassName();
		cr = this.getContentResolver();

		checkAuth();

	}

	public void checkAuth() {
		if (cmsutil.getAuthLevel(act) < 0) {
			String tmp = "회원제입니다.\n먼저 로그인하세요.";
			Toast.makeText(act.getBaseContext(), tmp, Toast.LENGTH_LONG).show();

			actNext = cmsutil.findAct(act, act.getPackageName() + ".loginAct");
			if (actNext != null) {
				((owllab) act.getApplication()).startLoading(act);
				Intent intent = new Intent(act, actNext.getClass());
				intent.putExtra("loginAfterClass", this.getLocalClassName());
				act.startActivity(intent);
				((owllab) act.getApplication()).endLoading();
			}

			return;
		}
		initInfo();
	}

	public void initInfo() {
		hmUser = cmsutil.getAuthHM(act);

		EditText orderName = (EditText) findViewById(R.id.orderName);
		EditText orderTel = cmsutil.getEditText(act, R.id.orderTel);
		EditText orderZipcode = cmsutil.getEditText(act, R.id.orderZipcode);
		EditText orderAddress = cmsutil.getEditText(act, R.id.orderAddress);
		// EditText orderMemo = cmsutil.getEditText(act, R.id.orderMemo);
		EditText orderPayer = cmsutil.getEditText(act, R.id.orderPayer);

		orderName.setText(hmUser.get("name[0]"));
		orderTel.setText(hmUser.get("tel[0]"));
		orderZipcode.setText(hmUser.get("zipcode[0]"));
		orderAddress.setText(hmUser.get("address[0]"));
		// orderMemo.setText(hmUser.get("memo[0]"));
		orderPayer.setText(hmUser.get("name[0]"));

		Spinner orderBankSP = (Spinner) findViewById(R.id.orderBankSP);
		ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this,
				R.array.bank, android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		orderBankSP.setAdapter(adapter);

		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		Cursor cursor = cr.query(CONTENT_URI, projection, selection,
				selectionArgs, sortOrder);
		hmCart = cmsutil.cursor2HashMap(cursor);
		cursor.close();

		hmTot = cmsutil.calcPrice(hmCart);
		TextView orderPrice = (TextView) findViewById(R.id.orderPrice);
		orderPrice.setText(hmTot.get("payPriceText"));
		btnTurnOn();
	}

	public void btnTurnOn() {
		((Button) findViewById(R.id.orderBtn))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						orderRequest();
					}
				});
		((Button) findViewById(R.id.detailBtn))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						((owllab) act.getApplication()).startLoading(act);
						cmsutil.goActivity(act, act.getPackageName()
								+ ".mallCart");
						((owllab) act.getApplication()).endLoading();

					}
				});
	}

	public void orderRequest() {

		int totalPrice = cmsutil.str2int(hmTot.get("totalPrice"), 0);

		if (cmsutil.getAuthLevel(act) < 0)
			return;
		if (totalPrice <= 0)
			return;

		String theUrl = "http://www.owllab.com/android/mall_order_proc.php";
		Log.i(TAG, theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("mode", "order"));
		httpParams.add(new BasicNameValuePair("totalPrice", hmTot
				.get("totalPrice")));

		String orderName = cmsutil.getEditTextVal(act, R.id.orderName);
		String orderTel = cmsutil.getEditTextVal(act, R.id.orderTel);
		String orderZipcode = cmsutil.getEditTextVal(act, R.id.orderZipcode);
		String orderAddress = cmsutil.getEditTextVal(act, R.id.orderAddress);
		String orderMemo = cmsutil.getEditTextVal(act, R.id.orderMemo);
		String orderPayer = cmsutil.getEditTextVal(act, R.id.orderPayer);
		Spinner orderBankSP = (Spinner) findViewById(R.id.orderBankSP);
		String orderBank = orderBankSP.getSelectedItem().toString();

		httpParams.add(new BasicNameValuePair("orderName", orderName));
		httpParams.add(new BasicNameValuePair("orderTel", orderTel));
		httpParams.add(new BasicNameValuePair("orderZipcode", orderZipcode));
		httpParams.add(new BasicNameValuePair("orderAddress", orderAddress));
		httpParams.add(new BasicNameValuePair("orderMemo", orderMemo));
		httpParams.add(new BasicNameValuePair("orderPayer", orderPayer));
		httpParams.add(new BasicNameValuePair("orderBank", orderBank));

		String fields = "num,rowid,prodname,sortname,price,ea,maxea,img";
		String[] fielda = cmsutil.explode_trim(",", fields);
		for (int i = 0; i < cmsutil.str2int(hmCart.get("count"), 0); i++) {
			for (int j = 0; j < fielda.length; j++) {
				String tmpKey = fielda[j] + "[" + i + "]";
				String tmpVal = hmCart.get(fielda[j] + "[" + i + "]");
				httpParams.add(new BasicNameValuePair(tmpKey, tmpVal));
			}
		}
		httpParams
				.add(new BasicNameValuePair("cartCount", hmCart.get("count")));

		cmsHTTP cmsHttp = new cmsHTTP();
		cmsHttp.act = act;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);
		if (tmpData == null)
			return;
		Log.i(TAG, tmpData);
		util cmsutil = new util();
		hm = cmsutil.xml2HashMap(tmpData, cmsHttp.encoding);
		addResult();
	}

	public void addResult() {
		int rowid = cmsutil.str2int(hm.get("rowid[0]"));
		String msg = hm.get("msg[0]");

		// rowid : 1(주문성공), -1(실패:기타)
		switch (rowid) {
		case 1:
			cr.delete(CONTENT_URI, null, null);
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			cmsutil.goActivity(act, act.getPackageName() + ".mallOrderList");
			return;
		default:
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			return;
		}
	}

}
