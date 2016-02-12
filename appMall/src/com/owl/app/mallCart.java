package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.app.cms.util;

public class mallCart extends Activity {

	public String TAG = "";
	public String encoding = "UTF-8";
	public HashMap<String, String> hm = new HashMap<String, String> ();
	HashMap<String, String> hmCart = new HashMap<String, String> ();
	util cmsutil = new util();
	Activity act = this;
	Intent actIntent;
	Activity actNext;
	ContentResolver cr;
	public static final String uriString = "content://com.owl.app.cartProvider/cart";
	public static final Uri CONTENT_URI = Uri.parse(uriString);
	String rowid;
	int ea;
	String mode;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    setContentView(R.layout.mall_cart);
	    top top = new top(this);
		top.init();
		
		actIntent = this.getIntent();
		rowid = cmsutil.null2empty(actIntent.getStringExtra("rowid"));
		ea = actIntent.getIntExtra("ea", 0);
		mode = cmsutil.null2empty(actIntent.getStringExtra("mode"));
		
		cmsutil.act = this;
		TAG = this.getLocalClassName();
		cr = this.getContentResolver();
		
		cartIn();
		btnTurnOn();
		
	}
	
	public void getXMLDataList() {
		if (!"".equals(rowid) && ea>0) {} else return;
		
		String theUrl = "http://www.owllab.com/android/mall_detail.php";
		Log.i(TAG, theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("rowid", rowid));
		cmsHTTP cmsHttp = new cmsHTTP();
		cmsHttp.act = act;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);
		if (tmpData == null)
			return;
		Log.i(TAG, tmpData);
		util cmsutil = new util();
		hm = cmsutil.xml2HashMap(tmpData, cmsHttp.encoding);

		if (cmsutil.str2int(hm.get("count"),0)<=0) {
			String tmp = "죄송합니다.\n상품정보를 가져오지 못했습니다.\n다시 시도해주세요.";
			Toast.makeText(act.getBaseContext(), tmp, Toast.LENGTH_LONG).show();
			((owllab) act.getApplication()).startLoading(act);
			act.onBackPressed();
			((owllab) act.getApplication()).endLoading();
			return;
		}

	}
	
	public void cartIn() {
		
		getXMLDataList();
		
		if (!"".equals(rowid)) {
			int eaMax = cmsutil.str2int(hm.get("maxEa[0]"),0);
			if (eaMax<0) eaMax=0;
			if (eaMax<ea) {
				String tmp = "죄송합니다.\n재고가 부족하여 주문할 수 없습니다.";
				if (eaMax>0) tmp = "죄송합니다.\n남은 재고량 만큼만 주문할 수 있습니다.";
				Toast.makeText(act.getBaseContext(), tmp, Toast.LENGTH_LONG).show();
				ea = eaMax;
			}
			
			String fields = "num,rowid,prodname,sortname,price,ea,maxea,img";
			String[] projection = cmsutil.explode_trim(",", fields);
			String selection = "rowid='"+rowid+"'";
			String[] selectionArgs = null;
			String sortOrder = null;
			Cursor cursor = cr.query(CONTENT_URI, projection, selection, selectionArgs, sortOrder);
			HashMap<String, String> hmCartOld = new HashMap<String, String>();
			hmCartOld = cmsutil.cursor2HashMap(cursor);
			cursor.close();
			
			int rowNum = cmsutil.str2int(hmCartOld.get("count"), 0);
			if (rowNum>0 && rowid.equals(hmCartOld.get("rowid[0]"))) {
				int eaOrg = cmsutil.str2int(hmCartOld.get("ea[0]"));
				if ("edit".equals(mode)) {} 
				else ea += eaOrg;
				
				if (eaMax<ea) {
					String tmp = "죄송합니다.\n재고가 부족하여 주문할 수 없습니다.";
					if (eaMax>0) tmp = "죄송합니다.\n남은 재고량 만큼만 주문할 수 있습니다.";
					Toast.makeText(act.getBaseContext(), tmp, Toast.LENGTH_LONG).show();
					ea = eaMax;
				}
				if (ea>0) {
					ContentValues contVal = new ContentValues();
					contVal.put("ea", ea);
					cr.update(CONTENT_URI, contVal, selection, selectionArgs);
				} else {
					cr.delete(CONTENT_URI, selection, selectionArgs);
					String tmp = "주문량이 없어 다음 제품을 삭제합니다.\n"+hmCartOld.get("prodname[0]");
					Toast.makeText(act.getBaseContext(), tmp, Toast.LENGTH_LONG).show();
				}
			} else {
				if (ea>0) {
					ContentValues contVal = new ContentValues();
					contVal.put("rowid", cmsutil.str2int(rowid,-1));
					contVal.put("prodname", hm.get("prodName[0]"));
					contVal.put("sortname", hm.get("sortName[0]"));
					contVal.put("price", cmsutil.str2int(hm.get("price[0]"),0));
					contVal.put("ea", ea);
					contVal.put("maxea", cmsutil.str2int(hm.get("maxEa[0]"),0));
					contVal.put("img", hm.get("img[0]"));
					cr.insert(CONTENT_URI, contVal);
				}
			}
		}// if rowid 
		initRow();
	}
	
	public void initRow() {
		
		String[] projection = null;
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = null;
		Cursor cursor = cr.query(CONTENT_URI, projection, selection, selectionArgs, sortOrder);
		hmCart = cmsutil.cursor2HashMap(cursor);
		cursor.close();
		
		mallCartAdapter mallCartAdapter = new mallCartAdapter(act, act,
				R.layout.mall_cart_row, hmCart);
		
		ListView listView = (ListView) act.findViewById(R.id.listView);
		listView.setAdapter(mallCartAdapter);
		calcPrice();
		
	}
	
	public void calcPrice() {
		HashMap<String, String> hmTot = cmsutil.calcPrice(hmCart);		
		((TextView) findViewById(R.id.deliveryPrice)).setText(hmTot.get("deliveryPriceText"));
		((TextView) findViewById(R.id.totalPrice)).setText(hmTot.get("totalPriceText"));
	}
	
	public void cartUpdate(String rowidSelected, int eaSelected) {
		rowid = rowidSelected;
		ea = eaSelected;

		cartIn();
	}

	public void btnTurnOn() {

		((ImageButton) findViewById(R.id.mallOrderBtn))
				.setOnClickListener(new ImageButton.OnClickListener() {
					public void onClick(View v) {
						((owllab) act.getApplication()).startLoading(act);
						actNext = cmsutil.findAct(act, act.getPackageName()
								+ ".mallOrder");
						if (actNext != null) {
							Intent intent = new Intent(act, actNext.getClass());
							act.startActivity(intent);
						}
						((owllab) act.getApplication()).endLoading();
					}
				});
		((ImageButton) findViewById(R.id.mallListBtn))
				.setOnClickListener(new ImageButton.OnClickListener() {
					public void onClick(View v) {
						((owllab) act.getApplication()).startLoading(act);
						actNext = cmsutil.findAct(act, act.getPackageName()
								+ ".mallList");
						if (actNext != null) {
							Intent intent = new Intent(act, actNext.getClass());
							act.startActivity(intent);
						}
						((owllab) act.getApplication()).endLoading();

					}
				});
	}
	
	//optionMenu//////////////////////////
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		new optionMenu(this).initMenu(menu);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean tmp = new optionMenu(this).initSelected(item);
		if (tmp) return tmp;
		else return super.onOptionsItemSelected(item);
	}
	//optionMenu//////////////////////////

}
