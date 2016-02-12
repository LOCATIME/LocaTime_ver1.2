package com.owl.app;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.owl.app.cms.util;

public class userInfo extends Activity {

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
	HashMap<String, String> hm;

	public void getXMLDataList() {

		hm = cmsutil.getAuthHM(act);

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

		userFormTEL.setText(hm.get("tel[0]"));
		userFormID.setText(hm.get("id[0]"));
		userFormPWD.setText(hm.get("pwd[0]"));
		userFormALIAS.setText(hm.get("alias[0]"));
		userFormNAME.setText(hm.get("name[0]"));
		userFormEMAIL.setText(hm.get("email[0]"));
		userFormZIPCODE.setText(hm.get("zipcode[0]"));
		userFormADDRESS.setText(hm.get("address[0]"));

		userFormTEL.setEnabled(false);
		userFormID.setEnabled(false);
		userFormPWD.setEnabled(false);
		userFormALIAS.setEnabled(false);
		userFormNAME.setEnabled(false);
		userFormEMAIL.setEnabled(false);
		userFormZIPCODE.setEnabled(false);
		userFormADDRESS.setEnabled(false);

		userFormTEL.setBackgroundColor(Color.TRANSPARENT);
		userFormID.setBackgroundColor(Color.TRANSPARENT);
		userFormPWD.setBackgroundColor(Color.TRANSPARENT);
		userFormALIAS.setBackgroundColor(Color.TRANSPARENT);
		userFormNAME.setBackgroundColor(Color.TRANSPARENT);
		userFormEMAIL.setBackgroundColor(Color.TRANSPARENT);
		userFormZIPCODE.setBackgroundColor(Color.TRANSPARENT);
		userFormADDRESS.setBackgroundColor(Color.TRANSPARENT);

		ImageView userFormTitle = (ImageView) findViewById(R.id.userFormTitle);
		userFormTitle.setImageDrawable(getResources().getDrawable(
				R.drawable.userinfo_title));

		LinearLayout userFormTitleLayout = 
			(LinearLayout) findViewById(R.id.userFormTitleLayout);
		userFormTitleLayout.getLayoutParams().height = 0;

		ImageButton userFormOKBtn = 
			(ImageButton) findViewById(R.id.userFormOKBtn);
		ImageButton userFormCancelBtn = 
			(ImageButton) findViewById(R.id.userFormCancelBtn);

		userFormOKBtn.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.userinfo_edit_btn));
		userFormCancelBtn.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.userinfo_unreg_btn));
	}

	public void btnTurnOn() {
		((ImageButton) this.findViewById(R.id.userFormOKBtn))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						((owllab) act.getApplication()).startLoading(act);
						cmsutil.goActivity(act, act.getPackageName()
								+ ".userEdit");
						((owllab) act.getApplication()).endLoading();
					}
				});

		findViewById(R.id.userFormCancelBtn).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						((owllab) act.getApplication()).startLoading(act);
						cmsutil.goActivity(act, act.getPackageName()
								+ ".userOut");
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
