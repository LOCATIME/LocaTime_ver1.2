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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.app.cms.util;

public class mallOrderList extends Activity {

	public String TAG = "";
	public String encoding = "UTF-8";
	public HashMap<String, String> hm;
	util cmsutil = new util();
	Activity act = this;
	Intent actIntent;
	String sort;
	String sortName;
	Activity actNext;
	String o = "";
	String state = "";
	String mode = "";
	int pg = 1;
	int totpg = 1;
	int totrec = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_order_list);
		top top = new top(this);
		top.init();

		actIntent = getIntent();
		pg = actIntent.getIntExtra("pg",1);
		o = cmsutil.null2empty(actIntent.getStringExtra("o"));
		state = cmsutil.null2empty(actIntent.getStringExtra("state"));
		mode = cmsutil.null2empty(actIntent.getStringExtra("mode"));
		
		cmsutil.act = this;
		TAG = this.getLocalClassName();
		
		checkAuth();

	}
	
	public void checkAuth() {
		if (cmsutil.getAuthLevel(act)<0) {
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
		getXMLDataList();
		btnTurnOn();
	}

	public void getXMLDataList() {
		String theUrl = "http://www.owllab.com/android/mall_order_list.php";
		Log.i(TAG, theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("pg", Integer.toString(pg)));
		httpParams.add(new BasicNameValuePair("o", o));
		httpParams.add(new BasicNameValuePair("state", state));
		httpParams.add(new BasicNameValuePair("mode", mode));
		cmsHTTP cmsHttp = new cmsHTTP();
		cmsHttp.encoding = encoding;
		cmsHttp.act = act;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);
		if (tmpData == null)
			return;
		Log.i(TAG, tmpData);
		util cmsutil = new util();
		hm = cmsutil.xml2HashMap(tmpData, encoding);
		pg = cmsutil.str2int(hm.get("pg[0]"),1);
		totpg = cmsutil.str2int(hm.get("totpg[0]"),1);
		totrec = cmsutil.str2int(hm.get("totrec[0]"),0);
		mallOrderListAdapter mallOrderListAdapter 
		= new mallOrderListAdapter(act, act, R.layout.mall_order_row, hm);
		ListView listView = (ListView) act.findViewById(R.id.listView);
		listView.setAdapter(mallOrderListAdapter);
		String pgText = hm.get("pg[0]") +"/"+hm.get("totpg[0]") 
		+"쪽 (총 "+hm.get("totrec[0]")+"건)";
		((TextView) findViewById(R.id.pgText)).setText(pgText);
		
		pgSeekBar = (SeekBar) findViewById(R.id.pgSeekBar);
		double pd100 = pg*100.0;
		double pd = pd100/totpg;
		int p = cmsutil.double2int(Math.ceil(pd));
		pgSeekBar.setProgress(p);
	}

	public SeekBar pgSeekBar;
	
	public void btnTurnOn() {
		((Button) findViewById(R.id.prevBtn))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						pg--;
						if (pg < 1)
							pg = 1;
						getXMLDataList();
					}
				});
		((Button) findViewById(R.id.nextBtn))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						pg++;
						if (pg > totpg)
							pg = totpg;
						getXMLDataList();
					}
				});
		
		pgSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
	}

	private SeekBar.OnSeekBarChangeListener seekBarChangeListener 
		= new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress
				, boolean fromUser) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			gotoPg();
		}
	};

	public void gotoPg() {
		int pgOrg = pg;
		int pro = pgSeekBar.getProgress();
		double d = (pro * totpg);
		double d100 = d/100;
		pg = cmsutil.double2int(Math.ceil(d100));
		if (pg < 1) pg = 1;
		if (pg > totpg) pg = totpg;
		if (pgOrg!=pg) getXMLDataList();
		double pd100 = pg*100.0;
		double pd = pd100/totpg;
		int p = cmsutil.double2int(Math.ceil(pd));
		pgSeekBar.setProgress(p);
	}
	// optionMenu//////////////////////////
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
		if (tmp)
			return tmp;
		else
			return super.onOptionsItemSelected(item);
	}
	// optionMenu//////////////////////////

}
