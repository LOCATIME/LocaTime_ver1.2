package com.owl.app;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.NameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class notice {

	public Activity act;
	public ArrayList<noticeMainItem> noticeMA;
	public String TAG = "notice";
	public String encoding = "UTF-8";

	public notice() {

	}

	public notice(Activity tmpact) {
		act = tmpact;
	}

	public HashMap<String, String> hm;

	public void getXMLDataList() {

		String theUrl = "http://www.owllab.com/android/notice_list_main.php";
		Log.i(TAG, theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		// httpParams.add(new BasicNameValuePair("company_name_en", "OWL"));
		cmsHTTP cmsHttp = new cmsHTTP();
		cmsHttp.encoding = encoding;
		cmsHttp.act = act;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);
		
		if (tmpData==null) return;
		
		Log.i(TAG, tmpData);

		ArrayList<noticeMainItem> noticeMI = new ArrayList<noticeMainItem>();
		// HashMap<String,String> hm = xml2HashMap(tmpData);
		hm = xml2HashMap(tmpData);
		for (int i = 0; i < Integer.parseInt(hm.get("count")); i++) {
			noticeMI.add(new noticeMainItem(hm.get("subject[" + i + "]"), hm
					.get("reg_date[" + i + "]")));
		}

		noticeListAdapter noticeListAdapter = new noticeListAdapter(act,
				R.layout.notice_row_main, noticeMI);
		noticeListAdapter.hm = hm;

		ListView noticeListMain = (ListView) act.findViewById(R.id.noticeListMain);
		noticeListMain.setAdapter(noticeListAdapter);
//		if (noticeMI.size()>0) noticeListMain.setSelection(0);
	}
	
	public HashMap<String, String> xml2HashMap(String tmpData) {

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("count", "0");

		try {

			DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
			DocumentBuilder docB = docBF.newDocumentBuilder();
			InputStream is = new ByteArrayInputStream(tmpData
					.getBytes(encoding));
			Document doc = docB.parse(is);

			Element lists = doc.getDocumentElement();
			NodeList dataList = lists.getElementsByTagName("data");

			int c = 0;
			for (int i = 0; i < dataList.getLength(); i++) {
				NodeList dataNodeList = dataList.item(i).getChildNodes();
				Log.i(TAG, "dataNodeList.getLength():"
						+ dataNodeList.getLength());
				for (int j = 0; j < dataNodeList.getLength(); j++) {
					;
					Node itemNode = dataNodeList.item(j);
					if (itemNode.getFirstChild() != null) {
						String nodeName = itemNode.getNodeName();
						String nodeValue = itemNode.getFirstChild()
								.getNodeValue();
						Log.i(TAG, nodeName + "[" + i + "]" + nodeValue);
						hm.put(nodeName + "[" + i + "]", nodeValue);
					}
				}// for j
				c++;
			}// for i
			hm.put("count", Integer.toString(c));
		} catch (Exception e) {
			Toast.makeText(act, e.getMessage(), 10).show();
			Log.e(TAG, e.getMessage());
		}
		return hm;
	}

}
