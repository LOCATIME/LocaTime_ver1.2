package com.owl.app.cms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.app.R;
import com.owl.app.cmsHTTP;
import com.owl.app.owllab;

public class util {

	public util() {

	}

	public Activity act;
	public String TAG = "com.owl.app.cms.util";

	public util(Activity tmpact) {
		act = tmpact;
	}
	
	
	
	public HashMap<String, String> calcPrice(HashMap<String, String> hmCart) {
		HashMap<String, String> hm = new HashMap<String, String>();
		int totalPrice = 0;
		int orgPrice = 0;
		int deliveryPrice = 0;
		
		String deliveryPriceLabel = "※ 배송료 : ";
		String totalPriceLabel = "※ 합계금액 : ";
		String priceEnd = "원";
		
		for (int i=0;i<str2int(hmCart.get("count"),0);i++) {
			int priceTmp = str2int(hmCart.get("price["+i+"]"),0);
			int eaTmp = str2int(hmCart.get("ea["+i+"]"),0);
			totalPrice += priceTmp * eaTmp;
		}
		orgPrice = totalPrice;
		if (totalPrice<50000 && totalPrice>0) deliveryPrice = 4000;
		if (totalPrice>0) totalPrice += deliveryPrice;
		
		String deliveryPriceText = deliveryPriceLabel + number_format(deliveryPrice) + priceEnd;
		String totalPriceText = totalPriceLabel + number_format(totalPrice) + priceEnd;
		String payPriceText = number_format(totalPrice) + priceEnd;
		
		hm.put("deliveryPriceText", deliveryPriceText);
		hm.put("totalPriceText", totalPriceText);
		hm.put("deliveryPrice", Integer.toString(deliveryPrice));
		hm.put("totalPrice", Integer.toString(totalPrice));
		hm.put("payPriceText", payPriceText);
		hm.put("orgPrice", Integer.toString(orgPrice));
		return hm;
	}
	
	public HashMap<String, String> cursor2HashMap(Cursor cursor) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("count", "0");
		cursor.getColumnNames();
		try {
			int i = 0;
			while (cursor.moveToNext()) {
				for (int j = 0; j < cursor.getColumnCount(); j++) {
					String fieldName = cursor.getColumnName(j);
					String fieldValue = cursor.getString(j);
					hm.put(fieldName + "[" + i + "]", fieldValue);
				}// for j
				i++;
			}// while
			hm.put("count", Integer.toString(i));
		} catch (Exception e) {
			Log.e("cursor2HashMap", e.getMessage());
		}
		return hm;
	}
	

	public void sendSMS(String destinationAddress, String text) {
		Log.v(TAG, "sendSMS:" + destinationAddress);
		if (!PhoneNumberUtils.isWellFormedSmsAddress(destinationAddress)) {
			return;
		}
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(destinationAddress, null, text, null, null);
	}

	public void sendSMSWithState(String destinationAddress, String text) {
		Log.v(TAG, "sendSMSWithState:" + destinationAddress);
		Log.v(TAG, "text:" + text);
		if (act == null)
			return;
		if (!PhoneNumberUtils.isWellFormedSmsAddress(destinationAddress)) {
			Toast.makeText(act.getBaseContext(), "휴대전화번호가 올바르지 않습니다.",
					Toast.LENGTH_LONG).show();
			return;
		}

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentIntents = PendingIntent.getBroadcast(act, 0,
				new Intent(SENT), 0);

		PendingIntent deliveryIntents = PendingIntent.getBroadcast(act, 0,
				new Intent(DELIVERED), 0);

		act.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(act.getBaseContext(), "SMS를 발송했습니다.",
							Toast.LENGTH_LONG).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(act.getBaseContext(), "오류: 일반오류가 발생했습니다.",
							Toast.LENGTH_LONG).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(act.getBaseContext(), "오류: 서비스 오류입니다.",
							Toast.LENGTH_LONG).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(act.getBaseContext(),
							"오류: PDU가 제공되지 않았습니다.", Toast.LENGTH_LONG).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(act.getBaseContext(), "오류: Radio off상태입니다.",
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		act.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(act.getBaseContext(), "SMS를 발송했습니다.",
							Toast.LENGTH_LONG).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(act.getBaseContext(), "SMS가 발송되지 않았습니다.",
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(destinationAddress, null, text, sentIntents,
				deliveryIntents);
	}

	public void sendSMSForm(String destinationAddress, String text) {
		Log.v(TAG, "sendSMSForm:" + destinationAddress);
		gotoSMS(destinationAddress, text);
	}

	public void gotoSMS(String destinationAddress, String text) {
		Log.v(TAG, "gotoSMS:" + destinationAddress);
		if (act == null)
			return;
		Uri uri = Uri.parse("smsto:" + destinationAddress);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", text);
		act.startActivity(intent);
	}

	public void gotoTel(String destinationAddress) {
		Log.v(TAG, "gotoTel:" + destinationAddress);
		if (act == null)
			return;
		Uri uri = Uri.parse("tel:" + destinationAddress);
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		act.startActivity(intent);
	}

	public void gotoEmail(String destinationAddress) {
		Log.v(TAG, "gotoEmail:" + destinationAddress);
		if (act == null)
			return;
		Uri uri = Uri.parse("mailto:" + destinationAddress);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		act.startActivity(intent);
	}

	public void gotoEmail(String destinationAddress, String subject) {
		Log.v(TAG, "gotoEmail:" + destinationAddress);
		if (act == null)
			return;
		Uri uri = Uri.parse("mailto:" + destinationAddress);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra(Intent.EXTRA_EMAIL, destinationAddress);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		// intent.putExtra(Intent.EXTRA_TEXT, text);
		intent.setType("text/plain");
		act.startActivity(Intent.createChooser(intent, "이메일을 선택하세요."));
	}

	public void gotoWeb(String destinationAddress) {
		Log.v(TAG, "gotoWeb:" + destinationAddress);
		if (act == null)
			return;
		Uri uri = Uri.parse("http://" + destinationAddress);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		act.startActivity(intent);
	}

	public void gotoMap(String destinationAddress) {
		Log.v(TAG, "gotoMap:" + destinationAddress);
		if (act == null)
			return;
		HashMap<String, String> hm = new HashMap<String, String>();
		hm = searchGPS(destinationAddress);
		Uri uri = Uri.parse("geo:" + hm.get("latitude[0]") + ","
				+ hm.get("longitude[0]"));
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		act.startActivity(intent);
	}

	public void gotoWay(String fromAddress, String toAddress) {
		Log.v(TAG, "gotoWay:" + fromAddress + " -> " + toAddress);
		if (act == null)
			return;
		HashMap<String, String> hmFrom = new HashMap<String, String>();
		HashMap<String, String> hmTo = new HashMap<String, String>();
		hmFrom = searchGPS(fromAddress);
		hmTo = searchGPS(toAddress);
		Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr="
				+ hmFrom.get("latitude[0]") + "%20"
				+ hmFrom.get("longitude[0]") + "&daddr="
				+ hmTo.get("latitude[0]") + "%20" + hmTo.get("longitude[0]")
				+ "&hl=ko");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		act.startActivity(intent);
	}

	public HashMap<String, String> findMyLocation() {
		Log.v(TAG, "findMyLocation:");
		HashMap<String, String> hm = new HashMap<String, String>();
		if (act == null)
			return hm;

		LocationManager LocMan = (LocationManager) act
				.getSystemService(Context.LOCATION_SERVICE);
		List<String> arProvider = LocMan.getProviders(false);
		for (int i = 0; i < arProvider.size(); i++) {
			Log.v(TAG, "Provider " + i + " : " + arProvider.get(i));
		}

		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.NO_REQUIREMENT);
		crit.setPowerRequirement(Criteria.NO_REQUIREMENT);
		crit.setAltitudeRequired(false);
		crit.setCostAllowed(false);
		String best = LocMan.getBestProvider(crit, true);
		Log.v(TAG, "best provider : " + best);

		Location loc = LocMan.getLastKnownLocation(best);
		if (loc != null) {
			Log.v(TAG, "latitude:" + Double.toString(loc.getLatitude()));
			Log.v(TAG, "longitude:" + Double.toString(loc.getLongitude()));

			hm.put("latitude", Double.toString(loc.getLatitude()));
			hm.put("longitude", Double.toString(loc.getLongitude()));
		}
		return hm;
	}
	
	public HashMap<String, String> hmMyLocation = new HashMap<String, String>();

	public void gotoWayFromMe(String toAddress) {
		Log.v(TAG, "gotoWay:" + toAddress);
		if (act == null)
			return;
		HashMap<String, String> hmFrom = new HashMap<String, String>();
		HashMap<String, String> hmTo = new HashMap<String, String>();

		hmFrom = hmMyLocation;
		hmTo = searchGPS(toAddress);
		Uri uri = Uri.parse("http://maps.google.com/maps?f=d&saddr="
				+ hmFrom.get("latitude") + "%20" + hmFrom.get("longitude")
				+ "&daddr=" + hmTo.get("latitude") + "%20"
				+ hmTo.get("longitude") + "&hl=ko");
		Log.v(TAG, "uri:" + uri.toString());
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		act.startActivity(intent);
	}

	// http://maps.google.com/maps?f=d&saddr=37.566535%20126.977969&daddr=37.5237762%20127.0224608&hl=ko
	public HashMap<String, String> searchGPS(String query) {
		Log.v(TAG, "searchGPS:" + query);
		HashMap<String, String> hm = new HashMap<String, String>();
		if (act == null)
			return hm;
		String latitude = "37.566535";
		String longitude = "126.977969";
		String queryEnc = "";
		cmsHTTP cmsHttp = new cmsHTTP();
		// cmsHttp.encoding = encoding;
		cmsHttp.act = act;
		try {
			queryEnc = URLEncoder.encode(query, cmsHttp.encoding);
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		}
		String theUrl = "http://maps.google.co.kr/maps/api/geocode/json?sensor=true&address="
				+ queryEnc;
		// String theUrl =
		// "http://maps.google.co.kr/maps/api/geocode/xml?sensor=true&address="+queryEnc;
		Log.i(TAG, theUrl);
		String tmpData = cmsHttp.sendGet(theUrl);
		if (tmpData == null)
			return hm;
		Log.i(TAG, tmpData);
		try {
			JSONObject jObj = new JSONObject(tmpData);
			Log.i(TAG, jObj.toString());
			if (jObj != null) {
				latitude = jObj.getJSONArray("results").getJSONObject(0)
						.getJSONObject("geometry").getJSONObject("location")
						.getString("lat");
				longitude = jObj.getJSONArray("results").getJSONObject(0)
						.getJSONObject("geometry").getJSONObject("location")
						.getString("lng");
				Log.i(TAG, "latitude : " + latitude);
				Log.i(TAG, "longitude : " + longitude);
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
		hm.put("latitude", latitude);
		hm.put("longitude", longitude);
		return hm;
	}

	public HashMap<String, String> getAdminInfo() {
		Log.v(TAG, "getAdminInfo:");
		HashMap<String, String> hmAdmin = new HashMap<String, String>();
		if (act == null)
			return hmAdmin;

		String theUrl = "http://www.owllab.com/android/admin_info.php";
		Log.i(act.getLocalClassName(), theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("mode", "admin_tel"));
		cmsHTTP cmsHttp = new cmsHTTP();
		// cmsHttp.encoding = encoding;
		cmsHttp.act = act;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);
		if (tmpData == null)
			return hmAdmin;
		hmAdmin = xml2HashMap(tmpData, cmsHttp.encoding);
		Log.v(act.getLocalClassName(), tmpData);
		return hmAdmin;
	}

	public String getAuthID(Activity act) {
		String tmp = "";
		HashMap<String, String> hm = ((owllab) act.getApplication()).authHM;
		tmp = null2empty(hm.get("id[0]"));
		return tmp;
	}

	public boolean getLoginState(Activity act) {
		boolean tmp = false;
		if (getAuthID(act).length() > 0)
			tmp = true;
		return tmp;
	}

	public int getAuthLevel(Activity act) {
		int tmp = -1;
		HashMap<String, String> hm = ((owllab) act.getApplication()).authHM;
		tmp = str2int(hm.get("level[0]"), -1);
		return tmp;
	}

	public HashMap<String, String> getAuthHM(Activity act) {
		HashMap<String, String> hm = ((owllab) act.getApplication()).authHM;
		return hm;
	}

	public void setAuthHM(Activity act, HashMap<String, String> hm) {
		((owllab) act.getApplication()).authHM = hm;
	}

	public InputFilter filterAlphaNum = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
			if (!ps.matcher(source).matches()) {
				return "";
			}
			return null;
		}
	};

	public InputFilter filterJavaLetterOrDigit = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			for (int i = start; i < end; i++) {
				if (!Character.isJavaLetterOrDigit(source.charAt(i))) {
					return "";
				}
			}
			return null;
		}
	};

	public InputFilter filterLetterNum = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

			// Pattern ps=Pattern.compile("[a-zA-Z0-9가-R]*");
			// if (!ps.matcher(source).matches()) {
			// return "";
			// }
			for (int i = start; i < end; i++) {
				if (!Character.isLetterOrDigit(source.charAt(i))) {
					return "";
				}
			}
			return null;
		}
	};

	public InputFilter filterLetter = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			for (int i = start; i < end; i++) {
				if (!Character.isLetter(source.charAt(i))) {
					return "";
				}
			}
			return null;
		}
	};

	public InputFilter filterNum = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			for (int i = start; i < end; i++) {
				if (!Character.isDigit(source.charAt(i))) {
					return "";
				}
			}
			return null;
		}
	};

	public String getMyPhoneNumber(Activity act) {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) act
				.getSystemService(Context.TELEPHONY_SERVICE);
		String tmp = mTelephonyMgr.getLine1Number();
		if (tmp == null)
			tmp = "";
		return tmp;
	}

	public String getEditTextVal(Activity act, int RItem) {
		String tmp = ((EditText) act.findViewById(RItem)).getText().toString();
		if (tmp == null)
			tmp = "";
		return tmp;
	}

	public EditText getEditText(Activity act, int RItem) {
		return ((EditText) act.findViewById(RItem));
	}

	public void goActivity(Context mContext, String menuClass) {
		String findClass = "";
		Class<?> cls;
		Activity actCategory;
		try {
			cls = Class.forName(menuClass);
			actCategory = (Activity) cls.newInstance();
			Intent intent = new Intent(mContext, actCategory.getClass());
			mContext.startActivity(intent);
			findClass = cls.getName();
		} catch (ClassNotFoundException e) {
			findClass = "";
			AlertDialog dialog = new AlertDialog.Builder(mContext).create();
			dialog.setTitle("안내");
			dialog.setMessage("죄송합니다.\n페이지를 찾을 수 없습니다.");
			dialog.setButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			dialog.show();
		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		}
	}

	public Activity findAct(Context mContext, String menuClass) {
		String foundClass = "";
		Class<?> cls;
		Activity actCategory = null;
		try {
			cls = Class.forName(menuClass);
			actCategory = (Activity) cls.newInstance();
			foundClass = cls.getName();
		} catch (ClassNotFoundException e) {
			foundClass = "";
			AlertDialog dialog = new AlertDialog.Builder(mContext).create();
			dialog.setTitle("안내");
			dialog.setMessage("죄송합니다.\n페이지를 찾을 수 없습니다.");
			dialog.setButton("확인", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			dialog.show();
		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		}
		return actCategory;
	}

	public HashMap<String, String> xml2HashMap(String tmpData, String encoding) {
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
				for (int j = 0; j < dataNodeList.getLength(); j++) {
					;
					Node itemNode = dataNodeList.item(j);
					if (itemNode.getFirstChild() != null) {
						String nodeName = itemNode.getNodeName();
						String nodeValue = itemNode.getFirstChild()
								.getNodeValue();
						hm.put(nodeName + "[" + i + "]", nodeValue);
					}
				}// for j
				c++;
			}// for i
			hm.put("count", Integer.toString(c));
		} catch (Exception e) {
			Log.e("com.cms.app.util.xml2HashMap", e.getMessage());
		}
		return hm;
	}

	public int str2int(String txt, int mydefault) {
		int num = 0;
		if (txt == null || "".equals(txt)) {
			num = mydefault;
		} else {
			try {
				num = Integer.parseInt(txt);
			} catch (NumberFormatException e) {
				Log.e(TAG,e.toString());
			}
		}
		return num;
	}

	public int str2int(String txt) {
		int num = 0;
		if (txt == null || "".equals(txt)) {

		} else {
			num = double2int(txt);
		}
		return num;
	}

	public int double2int(double val) {
		int tmp = 0;
		Double d = new Double(val);
		tmp = d.intValue();
		return tmp;
	}

	public int double2int(String val) {
		int tmp = 0;
		Double d = new Double(val);
		tmp = d.intValue();
		return tmp;
	}

	public double str2double(String txt) {
		double num = 0;
		if (txt == null || "".equals(txt)) {
			num = 0.0;
		} else {
			num = Double.valueOf(txt).doubleValue();
		}
		return num;
	}

	public long str2long(String txt) {
		long num = 0;
		if (txt == null || "".equals(txt)) {
			num = 0;
		} else {
			num = Long.valueOf(txt).longValue();
		}
		return num;
	}

	public String str_replace(String src, String des, String org) {
		int fromindex = 0;
		int toindex = 0;
		String replaced = "";
		int i = 0;
		if ("".equals(src) || src == null) {
			replaced = org;
		} else {
			while (fromindex >= 0) {
				if (i == 0) {
					toindex = org.indexOf(src, 0);
					if (toindex < 0) {
						replaced = org.substring(0, org.length());
						break;
					} else {
						replaced = org.substring(0, toindex);
						replaced += des;
					}
				} else {
					toindex = org.indexOf(src, fromindex + src.length());
					if (toindex < 0) {
						replaced += org.substring(fromindex + src.length(), org
								.length());
						break;
					} else {
						replaced += org.substring(fromindex + src.length(),
								toindex);
						replaced += des;
					}
				}
				fromindex = toindex;
				i++;
			}
		}// if
		return replaced;
	}

	public String str_replace_i(String src, String des, String org) {

		String org_upper = org.toUpperCase();
		String src_upper = src.toUpperCase();
		int fromindex = 0;
		int toindex = 0;
		String replaced = "";
		int i = 0;
		if ("".equals(src) || src == null) {
			replaced = org;
		} else {
			while (fromindex >= 0) {
				if (i == 0) {
					toindex = org_upper.indexOf(src_upper, 0);
					if (toindex < 0) {
						replaced = org.substring(0, org_upper.length());
						break;
					} else {
						replaced = org.substring(0, toindex);
						replaced += des;
					}
				} else {
					toindex = org_upper.indexOf(src_upper, fromindex
							+ src.length());
					if (toindex < 0) {
						replaced += org.substring(fromindex + src.length(), org
								.length());
						break;
					} else {
						replaced += org.substring(fromindex + src.length(),
								toindex);
						replaced += des;
					}
				}
				fromindex = toindex;
				i++;
			}
		}
		return replaced;

	}

	public String null2empty(String str) {
		if (str == null)
			str = "";
		return str;
	}
	
	public String[] explode_trim(String src,String org) {
		
		String[] tmpa = explode(src,org);
		String[] tmpb;
		if (tmpa!=null) {
			tmpb = new String[tmpa.length];
			for (int i=0;i<tmpb.length;i++) {
				tmpb[i] = tmpa[i].trim();
			}//for i
			return tmpb;
		}
		return tmpa;
	}
	
	public String[] explode_trim(String src,String org,int limit) {
		
		String[] tmpa = explode(src,org);
		String[] tmpb = new String[limit];
		for (int i=0;i<limit;i++) {
			if (i>=tmpa.length) {
				tmpb[i] = "";
			} else {
				tmpb[i] = tmpa[i].trim();
			}//if
		}//for i
		return tmpb;
	}

	public String[] explode_trim(String src,String org,int limit,int didx) {
		
		String[] tmpa = explode(src,org);
		String[] tmpb = new String[limit];
		for (int i=0;i<limit;i++) {
			if (i>=tmpa.length) {
				tmpb[i] = tmpa[didx];
			} else {
				tmpb[i] = tmpa[i].trim();
			}//if
		}//for i
		return tmpb;
	}

	public String[] explode(String src,String org,int limit) {
		
		String[] tmpa = explode(src,org);
		String[] tmpb = new String[limit];
		for (int i=0;i<limit;i++) {
			if (i>=tmpa.length) {
				tmpb[i] = "";
			} else {
				tmpb[i] = tmpa[i];
			}//if
		}//for i
		return tmpb;
	}

	public String[] explode(String src,String org,int limit,int didx) {
		
		String[] tmpa = explode(src,org);
		String[] tmpb = new String[limit];
		for (int i=0;i<limit;i++) {
			if (i>=tmpa.length) {
				tmpb[i] = tmpa[didx];
			} else {
				tmpb[i] = tmpa[i];
			}//if
		}//for i
		return tmpb;	
	}

	public String[] explode(String src,String org){
		int fromindex = 0;
		int toindex = 0;
		int i=0;
		Vector<String> v = new Vector<String>();
		if ("".equals(src) || src==null) {
			v.addElement(new String(org));
		} else {
			while(fromindex >= 0){
				if (i==0){
					toindex = org.indexOf(src,0);
					if (toindex < 0){
						v.addElement(new String(org.substring(0,org.length())));
						break;
					} else {
						v.addElement(new String(org.substring(0,toindex)));
					}
				} else {
					toindex = org.indexOf(src, fromindex+src.length());
					if (toindex < 0){
						v.addElement(new String(org.substring(fromindex+src.length(),org.length())));
						break;
					} else {
						v.addElement(new String(org.substring(fromindex+src.length(),toindex)));
					}
				}
				fromindex = toindex;
			i++;}
		}
		//Object[] myarray = v.toArray();
		String[] myarray = new String[v.size()];
		for (i=0;i<myarray.length;i++) {
			myarray[i] = (String)v.elementAt(i);
		}//for i
		return myarray;
	}
	
	public String number_format(int mynum) {
		String tmp = "";
		int orgnum = mynum;
		if (orgnum<0) mynum = mynum*(-1);
		tmp = Integer.toString(mynum);
		
		StringBuffer bufnum = new StringBuffer(tmp);
		bufnum.reverse();
		int n = bufnum.length();
		int a = 0;
		for (int i=3;i<n;i=i+3){
			if (n>i) {
				bufnum.insert(i+a,",");
			}//if
			a++;
		}//for
		bufnum.reverse();
		String rtn = "";
		if (orgnum<0) rtn += "-";
		rtn += bufnum+"";
		return rtn;
	}
	public String number_format(String mynum) {
		return number_format(str2int(mynum));
	}
	public String number_format(String mynum,int period){
		String rownum = "";
		String extnum = "";
		double dnum = Double.parseDouble(mynum);
		if (mynum.compareTo(".")<0){
			rownum = mynum;
		}else{
			if (period<=0){
				dnum = Math.round(dnum);
				String strnum = Double.toString(dnum);
				String lastnum = "";
				if (strnum.indexOf("E")>=0){
					int e = Integer.parseInt(strnum.substring(strnum.indexOf("E")+1));
					lastnum = strnum.substring(0,strnum.indexOf(".")) + strnum.substring(strnum.indexOf(".")+1,strnum.indexOf("E"));
				}else{
					lastnum = strnum;
				}
				rownum = lastnum;
			}else{
				for (int i=0;i<period;i++) dnum = dnum * 10;
				dnum = Math.round(dnum);
				for (int i=0;i<period;i++) dnum = dnum * 0.1;
				String strnum = Double.toString(dnum);
				String lastnum = "";
				if (strnum.indexOf("E")>=0){
					int e = Integer.parseInt(strnum.substring(strnum.indexOf("E")+1));
					lastnum = strnum.substring(0,strnum.indexOf(".")) + strnum.substring(strnum.indexOf(".")+1,e+2) + "." + strnum.substring(e+2,strnum.indexOf("E"));
				}else{
					lastnum = strnum;
				}
				lastnum = lastnum.substring(0,lastnum.indexOf(".") + period + 1);
	
				String[] mynuma = explode(".",lastnum);
				rownum = mynuma[0];
				extnum = "." + mynuma[1];
			}
	
		}
		StringBuffer bufnum = new StringBuffer(rownum);
		bufnum.reverse();
		int n = bufnum.length();
		int a = 0;
		for (int i=3;i<n;i=i+3){
			if (n>i) {
				bufnum.insert(i+a,",");
			}//if
			a++;
		}//for
		bufnum.reverse();
		return bufnum+extnum;
	}
}
