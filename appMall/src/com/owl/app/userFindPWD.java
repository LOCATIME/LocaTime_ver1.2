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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.owl.app.cms.util;

public class userFindPWD extends Activity {

	Intent intent;
	String id;
	util cmsutil = new util(this);
	Activity act = this;
	HashMap<String, String> hm = new HashMap<String, String>();
	HashMap<String, String> hmAdmin = new HashMap<String, String>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		intent = this.getIntent();
		id = intent.getStringExtra("id");
		hmAdmin = cmsutil.getAdminInfo();
		confirmDialog();
	}

	public void confirmDialog() {
		String msg = "";
		if (cmsutil.getMyPhoneNumber(act).length() >= 10
				&& id.indexOf("error") < 0) {
			msg = "휴대전화정보를 이용하여 암호를 찾을 수 있습니다." 
				+ "\n찾은 암호는 SMS로 보내드립니다."
				+ "\n" + cmsutil.getMyPhoneNumber(act) + "으로"
				+ "\n임시암호를 요청하시겠습니까?";
			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			builder.setTitle("안내").setMessage(msg).setCancelable(false)
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									getXMLDataList();
								}
							}).setNeutralButton("고객센터",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									cmsutil.goActivity(act, act
											.getPackageName()
											+ ".helpDesk");
								}

							}).setNegativeButton("닫기",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									act.onBackPressed();
								}
							}).create().show();
		} else {
			msg = "죄송합니다." + "\n이 단말기는 휴대전화정보를 사용할 수 없도록 제한되어 있습니다."
					+ "\nSMS로 관리자에게 문의하시겠습니까?.";
			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			builder.setTitle("안내").setMessage(msg).setCancelable(false)
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									String destinationAddress = hmAdmin
											.get("sms[0]");
									String text = "암호가 기억나지 않습니다."
											+ "이 휴대전화번호에 대한 계정의 임시암호를 보내주세요.";
									cmsutil.act = act;
									cmsutil.gotoSMS(destinationAddress, text);
								}
							}).setNegativeButton("닫기",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									act.onBackPressed();
								}
							}).create().show();
		}
	}

	public void getXMLDataList() {
		String theUrl = "http://www.owllab.com/android/find_pass.php";
		Log.i(this.getLocalClassName(), theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("mode", "find"));
		httpParams.add(new BasicNameValuePair("id", id));
		httpParams.add(new BasicNameValuePair("tel", cmsutil
				.getMyPhoneNumber(act)));
		cmsHTTP cmsHttp = new cmsHTTP();
		// cmsHttp.encoding = encoding;
		cmsHttp.act = this;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);
		if (tmpData == null)
			return;
		hm = cmsutil.xml2HashMap(tmpData, cmsHttp.encoding);
		Log.v(this.getLocalClassName(), tmpData);
		outResult();
	}

	public void outResult() {
		Log.v(this.getLocalClassName(), "outResult");
		int rowid = cmsutil.str2int(hm.get("rowid[0]"));
		String msg = hm.get("msg[0]");
		// rowid : 1(성공), -1(실패:기타오류), -2(실패:계정없음)
		
		Log.v(this.getLocalClassName(), "rowid:"+hm.get("rowid[0]"));
		switch (rowid) {
		case 1:
			String destinationAddress = cmsutil.getMyPhoneNumber(act);
			String text = "임시암호는 [" + hm.get("tmppwd[0]") + "] 입니다."
			+ "\n이 암호는 3분간만 유효합니다.";
			// 가상기기 실험전용////////////////////////////////
			if (id.indexOf("test") >= 0 || id.indexOf("error") >= 0) {
				String tmp = cmsutil.str_replace("test", "", id);
				tmp = cmsutil.str_replace("error", "", tmp);
				destinationAddress = tmp;
			}
			if (id.indexOf("test") >= 0) {
				text = "your password is " + hm.get("tmppwd[0]") 
						+ ". this password is available for 3minutes only.";
			}
			// 가상기기 실험전용////////////////////////////////
			cmsutil.act = this;
			cmsutil.sendSMSWithState(destinationAddress, text);
			// cmsutil.sendSMS(destinationAddress, text);
			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			builder.setTitle("안내").setMessage(msg).setCancelable(false)
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									act.onBackPressed();
								}
							}).create().show();
			return;
		default:
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			act.onBackPressed();
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
