package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.owl.app.cms.util;

public class userAgreement extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.user_agreement);
		getXMLDataList();
		btnTurnOn();

		top top = new top(this);
		top.init();
	}

	util cmsutil = new util();

	public void getXMLDataList() {
		TextView agreementView = (TextView) findViewById(R.id.agreementView);
		agreementView.setText("");
		agreementView.setMovementMethod(new ScrollingMovementMethod());

		String theUrl = "http://www.owllab.com/android/agreement.php";
		Log.i(this.getLocalClassName(), theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		// httpParams.add(new BasicNameValuePair("company_name_en", "OWL"));
		cmsHTTP cmsHttp = new cmsHTTP();
		// cmsHttp.encoding = encoding;
		cmsHttp.act = this;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);

		if (tmpData == null) return;
		Log.i(this.getLocalClassName(), tmpData);

		HashMap<String, String> hm = cmsutil.xml2HashMap(tmpData, cmsHttp.encoding);
		agreementView.setText(hm.get("content[0]"));

	}

	Activity act = this;
	Activity actNext;
	Intent intent;

	public void btnTurnOn() {
		ImageButton agreementOKBtn = (ImageButton) this
				.findViewById(R.id.agreementOKBtn);
		agreementOKBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				((owllab) act.getApplication()).startLoading(act);
				Activity actNext = cmsutil.findAct(act, act.getPackageName()
						+ ".userForm");
				intent = new Intent(act, actNext.getClass());
				intent.putExtra("agreeState", "Y");
				act.startActivity(intent);
				((owllab) act.getApplication()).endLoading();
			}
		});

		ImageButton agreementCancelBtn = (ImageButton) this
				.findViewById(R.id.agreementCancelBtn);
		agreementCancelBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				((owllab) act.getApplication()).startLoading(act);
				Activity actNext = cmsutil.findAct(act, act.getPackageName()
						+ ".mainSite");
				intent = new Intent(act, actNext.getClass());
				act.startActivity(intent);
				((owllab) act.getApplication()).endLoading();
			}
		});
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
