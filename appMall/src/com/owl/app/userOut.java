package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.owl.app.cms.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class userOut extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		confirmDialog();
	}

	util cmsutil = new util();
	Activity act = this;
	HashMap<String, String> hm = new HashMap<String, String>();

	public void confirmDialog() {
		String msg = "";
		if (cmsutil.getAuthLevel(act) >= 0) {
			msg = "회원에서 탈퇴하시겠습니까?";
			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			builder.setTitle("안내").setMessage(msg).setCancelable(false)
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									getXMLDataList();
								}
							}).setNegativeButton("닫기",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									act.onBackPressed();
								}
							}).create().show();
		} else {
			msg = "권한이 없습니다.\n다시 로그인 후 시도해 주세요.";
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			((owllab) act.getApplication()).startLoading(act);
			cmsutil.goActivity(act, act.getPackageName() + ".mainSite");
			((owllab) act.getApplication()).endLoading();
		}
	}

	public void getXMLDataList() {
		String theUrl = "http://www.owllab.com/android/user_proc.php";
		Log.i(this.getLocalClassName(), theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("mode", "out"));
		httpParams.add(new BasicNameValuePair("tel", cmsutil.getAuthHM(act)
				.get("tel[0]")));
		httpParams.add(new BasicNameValuePair("id", cmsutil.getAuthHM(act).get(
				"id[0]")));
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
		int rowid = cmsutil.str2int(hm.get("rowid[0]"));
		String msg = hm.get("msg[0]");
		// rowid : 3(탈퇴성공), -5(탈퇴실패)
		switch (rowid) {
		case 3:
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			hm = new HashMap<String, String>();
			cmsutil.setAuthHM(act, hm);
			((owllab) act.getApplication()).startLoading(act);
			cmsutil.goActivity(act, act.getPackageName() + ".mainSite");
			((owllab) act.getApplication()).endLoading();
			return;
		default:
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
			act.onBackPressed();
			return;
		}

	}

}
