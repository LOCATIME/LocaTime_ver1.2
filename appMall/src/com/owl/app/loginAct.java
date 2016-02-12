package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.owl.app.cms.util;

public class loginAct extends Activity {

	String loginAfterClass = "";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.login_form);

		getXMLDataList();
		btnTurnOn();

		top top = new top(this);
		top.init();
		
		Intent actIntent = this.getIntent();
		loginAfterClass = actIntent.getStringExtra("loginAfterClass");
	}

	util cmsutil = new util();
	Activity act = this;
	Activity actNext;
	Intent intent;
	ToggleButton loginFormType;

	public void getXMLDataList() {
		loginFormType = (ToggleButton) findViewById(R.id.loginFormType);
		cmsutil.getEditText(act, R.id.loginFormID).setText("");
		cmsutil.getEditText(act, R.id.loginFormPWD).setText("");
		cmsutil.getEditText(act, R.id.loginFormID).setFilters(
				new InputFilter[] { cmsutil.filterAlphaNum });
		if (cmsutil.getLoginState(act)) {
			Toast.makeText(act, "이미 로그인되어 있습니다.", Toast.LENGTH_LONG).show();
			((owllab) act.getApplication()).startLoading(act);
			act.onBackPressed();
			((owllab) act.getApplication()).endLoading();
		} else {
			cmsutil.getEditText(act, R.id.loginFormTEL).setText(
					cmsutil.getMyPhoneNumber(act));
			if (cmsutil.getMyPhoneNumber(act).length() >= 10) {
				cmsutil.getEditText(act, R.id.loginFormTEL).setEnabled(false);
				loginFormType.setChecked(true);
			}
			setLoginType();
		}
	}

	public void setLoginType() {
		if (loginFormType.isChecked()) {
			cmsutil.getEditText(act, R.id.loginFormID).setEnabled(false);
			cmsutil.getEditText(act, R.id.loginFormPWD).requestFocus();
		} else {
			cmsutil.getEditText(act, R.id.loginFormID).setEnabled(true);
			cmsutil.getEditText(act, R.id.loginFormID).requestFocus();
		}
	}

	public void btnTurnOn() {

		findViewById(R.id.loginFormType).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						setLoginType();
					}
				});

		((ImageButton) this.findViewById(R.id.loginFormOKBtn))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						tryToLogin();
					}
				});

		findViewById(R.id.loginFormCancelBtn).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						((owllab) act.getApplication()).startLoading(act);
						act.onBackPressed();
						((owllab) act.getApplication()).endLoading();
					}
				});
	}

	HashMap<String, String> hm;

	public void tryToLogin() {
		if (checkFormValid()) {
			String theUrl = "http://www.owllab.com/android/login_proc.php";
			Log.i(this.getLocalClassName(), theUrl);
			ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
			httpParams.add(new BasicNameValuePair("tellogin", Boolean
					.toString(loginFormType.isChecked())));
			httpParams.add(new BasicNameValuePair("tel", ((EditText) act
					.findViewById(R.id.loginFormTEL)).getText().toString()));
			httpParams.add(new BasicNameValuePair("pwd", cmsutil
					.getEditTextVal(act, R.id.loginFormPWD)));
			httpParams.add(new BasicNameValuePair("id", cmsutil.getEditTextVal(
					act, R.id.loginFormID)));
			cmsHTTP cmsHttp = new cmsHTTP();
			// cmsHttp.encoding = encoding;
			cmsHttp.act = this;
			String tmpData = cmsHttp.sendPost(theUrl, httpParams);
			if (tmpData == null)
				return;
			hm = cmsutil.xml2HashMap(tmpData, cmsHttp.encoding);
			Log.v(this.getLocalClassName(), tmpData);
			addResult();
		}
	}

	public boolean checkFormValid() {
		boolean state = false;
		if (loginFormType.isChecked()
				&& cmsutil.getEditTextVal(act, R.id.loginFormTEL).length() < 10) {
			Toast.makeText(act, "휴대전화를 올바르게 입력하세요.", Toast.LENGTH_LONG).show();
			cmsutil.getEditText(act, R.id.loginFormTEL).requestFocus();
		} else if (!loginFormType.isChecked()
				&& cmsutil.getEditTextVal(act, R.id.loginFormID).length() <= 0) {
			Toast.makeText(act, "아이디를 입력하세요.", Toast.LENGTH_LONG).show();
			cmsutil.getEditText(act, R.id.loginFormID).requestFocus();
		} else if (cmsutil.getEditTextVal(act, R.id.loginFormPWD).length() <= 0) {
			Toast.makeText(act, "암호를 입력하세요.", Toast.LENGTH_LONG).show();
			cmsutil.getEditText(act, R.id.loginFormPWD).requestFocus();
		} else {
			state = true;
		}
		return state;
	}

	public void addResult() {
		int rowid = cmsutil.str2int(hm.get("rowid[0]"));
		String msg = hm.get("msg[0]");

		// rowid : 1(로그인성공),
		// -1(로그인실패:아이디오류),
		// -2(로그인실패:휴대전화오류),
		// -3(로그인실패:암호오류),
		// -4(접근제한계정)
		// rowid : 0(로그아웃)
		switch (rowid) {
		case 1:
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			cmsutil.setAuthHM(act, hm);
//			((owllab) act.getApplication()).startLoading(act);
			if (loginAfterClass==null) {
				cmsutil.goActivity(act, act.getPackageName() + ".userInfo");
			} else {
				cmsutil.goActivity(act, act.getPackageName() + "."+loginAfterClass);
			}
//			((owllab) act.getApplication()).endLoading();
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
