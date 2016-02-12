package com.owl.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.owl.app.cms.util;

public class category {

	public Activity act;
	public String TAG = "category";
	public String encoding = "UTF-8";
	public HashMap<String, String> hm;
	public String sort = "";
	util cmsutil = new util();//추가 

	public category() {

	}

	public category(Activity tmpact) {
		act = tmpact;
	}

	public void getXMLDataList() {
		sort = cmsutil.null2empty(act.getIntent().getStringExtra("sort"));//추가 
		String theUrl = "http://www.owllab.com/android/category.php?code="+sort;//추가 
		Log.i(TAG, theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		// httpParams.add(new BasicNameValuePair("company_name_en", "OWL"));
		cmsHTTP cmsHttp = new cmsHTTP();
		cmsHttp.encoding = encoding;
		cmsHttp.act = act;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);
		if (tmpData==null) return;
		Log.i(TAG, tmpData);
		util cmsutil = new util();
		hm = cmsutil.xml2HashMap(tmpData, encoding);
		categoryListAdapter categoryListAdapter = new categoryListAdapter(act,
				R.layout.category_row, hm);
		ListView categoryMain = (ListView) act.findViewById(R.id.categoryMain);
		categoryMain.setAdapter(categoryListAdapter);
	}
}
