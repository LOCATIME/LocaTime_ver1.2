package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.owl.app.cms.util;

public class userForm extends Activity {

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

	public void getXMLDataList() {
		Intent intent = this.getIntent();
		String mode = intent.getStringExtra("mode");
		if ("edit".equals(mode)) {

		} else {
			cmsutil.getEditText(act, R.id.userFormTEL).setText(
					cmsutil.getMyPhoneNumber(act));
			if (cmsutil.getMyPhoneNumber(act).length() >= 10) {
				cmsutil.getEditText(act, R.id.userFormTEL).setEnabled(false);
			}
			cmsutil.getEditText(act, R.id.userFormPWD).setText("");
		}
	}

	public void btnTurnOn() {
		((ImageButton) this.findViewById(R.id.userFormOKBtn))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						// ((owllab) act.getApplication()).startLoading(act);
						tryToAdd();
						// ((owllab) act.getApplication()).endLoading();
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

		cmsutil.getEditText(act, R.id.userFormID).setFilters(
				new InputFilter[] { cmsutil.filterAlphaNum });

	}

	HashMap<String, String> hm;

	public void tryToAdd() {

		if (checkFormValid()) {

			String theUrl = "http://www.owllab.com/android/user_proc.php";
			Log.i(this.getLocalClassName(), theUrl);
			ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
			httpParams.add(new BasicNameValuePair("mode", "add"));
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
			addResult();
		}
	}

	public boolean checkFormValid() {
		boolean state = false;
		if (cmsutil.getEditTextVal(act, R.id.userFormTEL).length() < 10) {
			Toast.makeText(act, "휴대전화를 올바르게 입력하세요.", Toast.LENGTH_LONG).show();
			cmsutil.getEditText(act, R.id.userFormTEL).requestFocus();
		} else if (cmsutil.getEditTextVal(act, R.id.userFormID).length() < 6) {
			Toast.makeText(act, "아이디를 6자이상 올바르게 입력하세요.", Toast.LENGTH_LONG)
					.show();
			cmsutil.getEditText(act, R.id.userFormID).requestFocus();
		} else if (cmsutil.getEditTextVal(act, R.id.userFormPWD).length() < 6) {
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

	public void addResult() {
		int rowid = cmsutil.str2int(hm.get("rowid[0]"));
		String msg = hm.get("msg[0]");

		// rowid : 1(성공), -1(오류/재시도), -2(휴대번호중복), -3(아이디중복)
		switch (rowid) {
		case 1:
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			cmsutil.setAuthHM(act, hm);
			((owllab) act.getApplication()).startLoading(act);
			cmsutil.goActivity(act, act.getPackageName() + ".userInfo");
			((owllab) act.getApplication()).endLoading();
			return;
		case -2:
			conflictTel(msg);
			return;
		case -3:
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			cmsutil.getEditText(act, R.id.userFormID).requestFocus();
			return;
		default:
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			return;
		}
	}

	public void conflictTel(String msg) {
		String msgAdd = "\n암호를 찾으시겠습니까?";
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setTitle("안내").setMessage(msg + msgAdd).setCancelable(false)
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// act.finish();
						// dialog.dismiss();
						((owllab) act.getApplication()).startLoading(act);
						// cmsutil.goActivity(act, act.getPackageName() +
						// ".userFindPWD");

						actNext = cmsutil.findAct(act, act.getPackageName()
								+ ".userFindPWD");
						intent = new Intent(act, actNext.getClass());
						intent.putExtra("id", cmsutil.getEditTextVal(act,
								R.id.userFormID));
						act.startActivity(intent);

						((owllab) act.getApplication()).endLoading();
					}
				}).setNegativeButton("닫기",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						}).create().show();
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
