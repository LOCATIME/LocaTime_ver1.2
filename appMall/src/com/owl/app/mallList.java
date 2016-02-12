package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.owl.app.cms.util;

public class mallList extends Activity {

	public String TAG = "";
	public String encoding = "UTF-8";
	public HashMap<String, String> hm;
	util cmsutil = new util();
	Activity act = this;
	Intent actIntent;
	String sort;
	String sortName;
	Activity actNext;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    setContentView(R.layout.mall_list);
	    top top = new top(this);
		top.init();
		
		sort = getIntent().getStringExtra("sort");
		sortName = getIntent().getStringExtra("subject");
		top.setTitle(sortName);
		
		cmsutil.act = this;
		TAG = this.getLocalClassName();
		getXMLDataList();
		
		((Button) findViewById(R.id.sortBtn))
		.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				actNext = cmsutil.findAct(act, act.getPackageName() + ".mainSite");
				Intent intent = new Intent(act, actNext.getClass());
				intent.putExtra("tabId", "tab3");
				intent.putExtra("sort", sort);
				act.startActivity(intent);
			}
		});
		((Button) findViewById(R.id.searchBtn))
		.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				getXMLDataList();
			}
		});
		
	}
	
	public void getXMLDataList() {
		String theUrl = "http://www.owllab.com/android/mall_list.php";
		String prodKeyword = cmsutil.getEditTextVal(act, R.id.prodKeyword);
		Log.i(TAG, theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("sort", sort));
		httpParams.add(new BasicNameValuePair("prodname", prodKeyword));
		cmsHTTP cmsHttp = new cmsHTTP();
		cmsHttp.encoding = encoding;
		cmsHttp.act = act;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);
		if (tmpData==null) return;
		Log.i(TAG, tmpData);
		util cmsutil = new util();
		hm = cmsutil.xml2HashMap(tmpData, encoding);
		mallListAdapter mallListAdapter = new mallListAdapter(act,
				R.layout.mall_list_row, hm);
		ListView listView = (ListView) act.findViewById(R.id.listView);
		listView.setAdapter(mallListAdapter);
		
		if (cmsutil.str2int(hm.get("count"),0)<=0) {
			Toast.makeText(act.getBaseContext(), "검색된 상품이 없습니다.",
					Toast.LENGTH_LONG).show();
		}
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
