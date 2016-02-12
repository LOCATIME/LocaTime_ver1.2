package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.owl.app.cms.util;

public class logoutAct extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		getXMLDataList();

	}

	util cmsutil = new util();
	Activity act = this;
	HashMap<String, String> hm = new HashMap<String, String>();

	public void getXMLDataList() {
		String theUrl = "http://www.owllab.com/android/login_proc.php";
		Log.i(this.getLocalClassName(), theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("mode", "logout"));
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
		logoutResult();
	}

	public void logoutResult() {
		String msg = hm.get("msg[0]");
		hm = new HashMap<String, String>();
		cmsutil.setAuthHM(act, hm);
		Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
		((owllab) act.getApplication()).startLoading(act);
		cmsutil.goActivity(act, act.getPackageName() + ".mainSite");
		((owllab) act.getApplication()).endLoading();
	}

}
