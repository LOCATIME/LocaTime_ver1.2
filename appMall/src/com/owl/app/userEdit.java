package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.owl.app.cms.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class userEdit extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
		setContentView(R.layout.user_form);

		getXMLDataList();
		btnTurnOn();

		top top = new top(this);
		top.init();
	}
	
	util cmsutil = new util();
	Activity act = this;
	Activity actNext;
	Intent intent;
	HashMap<String, String> hmUser;
	HashMap<String, String> hm;

	public void getXMLDataList() {

		hmUser = cmsutil.getAuthHM(act);

		EditText userFormTEL = (EditText) findViewById(R.id.userFormTEL);
		EditText userFormID = cmsutil.getEditText(act, R.id.userFormID);
		EditText userFormPWD = cmsutil.getEditText(act, R.id.userFormPWD);
		EditText userFormALIAS = cmsutil.getEditText(act, R.id.userFormALIAS);
		EditText userFormNAME = cmsutil.getEditText(act, R.id.userFormNAME);
		EditText userFormEMAIL = cmsutil.getEditText(act, R.id.userFormEMAIL);
		EditText userFormZIPCODE = cmsutil.getEditText(act,
				R.id.userFormZIPCODE);
		EditText userFormADDRESS = cmsutil.getEditText(act,
				R.id.userFormADDRESS);

		userFormTEL.setText(hmUser.get("tel[0]"));
		userFormID.setText(hmUser.get("id[0]"));
		userFormPWD.setText(hmUser.get("pwd[0]"));
		userFormALIAS.setText(hmUser.get("alias[0]"));
		userFormNAME.setText(hmUser.get("name[0]"));
		userFormEMAIL.setText(hmUser.get("email[0]"));
		userFormZIPCODE.setText(hmUser.get("zipcode[0]"));
		userFormADDRESS.setText(hmUser.get("address[0]"));

		userFormTEL.setEnabled(false);
		userFormID.setEnabled(false);

		userFormTEL.setBackgroundColor(Color.TRANSPARENT);
		userFormID.setBackgroundColor(Color.TRANSPARENT);

		ImageView userFormTitle = (ImageView) findViewById(R.id.userFormTitle);
		userFormTitle.setImageDrawable(getResources().getDrawable(
				R.drawable.userinfo_edit_title));

		LinearLayout userFormTitleLayout = 
			(LinearLayout) findViewById(R.id.userFormTitleLayout);
		userFormTitleLayout.getLayoutParams().height = 0;

		ImageButton userFormOKBtn = 
			(ImageButton) findViewById(R.id.userFormOKBtn);
		ImageButton userFormCancelBtn = 
			(ImageButton) findViewById(R.id.userFormCancelBtn);

		userFormOKBtn.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.useredit_save_btn));
		userFormCancelBtn.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.useredit_cancel_btn));
	}

	public void btnTurnOn() {
		((ImageButton) this.findViewById(R.id.userFormOKBtn))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
//						((owllab) act.getApplication()).startLoading(act);
						tryToSave();
//						((owllab) act.getApplication()).endLoading();
					}
				});

		findViewById(R.id.userFormCancelBtn).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						((owllab) act.getApplication()).startLoading(act);
						act.onBackPressed();
						((owllab) act.getApplication()).endLoading();
					}
				});

	}
	
	

	public void tryToSave() {

		if (checkFormValid()) {
			String theUrl = "http://www.owllab.com/android/user_proc.php";
			Log.i(this.getLocalClassName(), theUrl);
			ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
			httpParams.add(new BasicNameValuePair("mode", "edit"));
			httpParams.add(new BasicNameValuePair("tel", ((EditText) act
					.findViewById(R.id.userFormTEL)).getText().toString()));
			httpParams.add(new BasicNameValuePair("pwd", cmsutil
					.getEditTextVal(act, R.id.userFormPWD)));
			httpParams.add(new BasicNameValuePair("id", cmsutil.getEditTextVal(
					act, R.id.userFormID)));
			httpParams.add(new BasicNameValuePair("alias", cmsutil
					.getEditTextVal(act, R.id.userFormALIAS)));
			httpParams.add(new BasicNameValuePair("name", cmsutil
					.getEditTextVal(act, R.id.userFormNAME)));
			httpParams.add(new BasicNameValuePair("email", cmsutil
					.getEditTextVal(act, R.id.userFormEMAIL)));
			httpParams.add(new BasicNameValuePair("zipcode", cmsutil
					.getEditTextVal(act, R.id.userFormZIPCODE)));
			httpParams.add(new BasicNameValuePair("address", cmsutil
					.getEditTextVal(act, R.id.userFormADDRESS)));
			cmsHTTP cmsHttp = new cmsHTTP();
			// cmsHttp.encoding = encoding;
			cmsHttp.act = this;
			String tmpData = cmsHttp.sendPost(theUrl, httpParams);
			if (tmpData == null)
				return;

			hm = cmsutil.xml2HashMap(tmpData, cmsHttp.encoding);
			Log.v(this.getLocalClassName(), tmpData);
			exeResult();
		}
	}

	public boolean checkFormValid() {
		boolean state = false;
		if (cmsutil.getEditTextVal(act, R.id.userFormPWD).length() < 6) {
			Toast.makeText(act, "암호를 6자이상 올바르게 입력하세요.", Toast.LENGTH_LONG)
					.show();
			cmsutil.getEditText(act, R.id.userFormPWD).requestFocus();
		} else if (cmsutil.getEditTextVal(act, R.id.userFormALIAS).length() < 6) {
			Toast.makeText(act, "별명을 6자이상 올바르게 입력하세요.", Toast.LENGTH_LONG)
					.show();
			cmsutil.getEditText(act, R.id.userFormALIAS).requestFocus();
		} else if (cmsutil.getEditTextVal(act, R.id.userFormNAME).length() < 2) {
			Toast.makeText(act, "이름을 올바르게 입력하세요.", Toast.LENGTH_LONG).show();
			cmsutil.getEditText(act, R.id.userFormNAME).requestFocus();
		} else if (cmsutil.getEditTextVal(act, R.id.userFormADDRESS).length() < 6) {
			Toast.makeText(act, "주소를 올바르게 입력하세요.", Toast.LENGTH_LONG).show();
			cmsutil.getEditText(act, R.id.userFormADDRESS).requestFocus();
		} else {
			state = true;
		}
		return state;
	}

	public void exeResult() {
		int rowid = cmsutil.str2int(hm.get("rowid[0]"));
		String msg = hm.get("msg[0]");

		// rowid : 2(저장성공), -4(저장실패)
		switch (rowid) {
		case 2:
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			cmsutil.setAuthHM(act, hm);
			((owllab) act.getApplication()).startLoading(act);
			cmsutil.goActivity(act, act.getPackageName() + ".userInfo");
			((owllab) act.getApplication()).endLoading();
			return;
		default:
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			return;
		}
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


//httpParams.add(new BasicNameValuePair("cID", hmUser.get("cID[0]")));
//httpParams.add(new BasicNameValuePair("cLevel", hmUser.get("cLevel[0]")));