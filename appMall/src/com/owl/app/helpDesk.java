package com.owl.app;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.owl.app.cms.util;

public class helpDesk extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.help_desk);
		hmAdmin = cmsutil.getAdminInfo();
		btnTurnOn();
		top top = new top(this);
		top.init();
		startLocationService();
	}

	util cmsutil = new util(this);
	Activity act = this;
	Activity actNext;
	Intent intent;

	HashMap<String, String> hmAdmin = new HashMap<String, String>();

	public void btnTurnOn() {

		findViewById(R.id.findPassBtn).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						((owllab) act.getApplication()).startLoading(act);
						actNext = cmsutil.findAct(act, act.getPackageName()
								+ ".userFindPWD");
						intent = new Intent(act, actNext.getClass());
						intent.putExtra("id", "");
						act.startActivity(intent);
						((owllab) act.getApplication()).endLoading();
					}
				});

		findViewById(R.id.helpSMSBtn).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						String destinationAddress = hmAdmin.get("sms[0]");
						String text = "문의합니다.";
						cmsutil.gotoSMS(destinationAddress, text);
					}
				});

		findViewById(R.id.helpTelBtn).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						String destinationAddress = hmAdmin.get("tel[0]");
						cmsutil.gotoTel(destinationAddress);
					}
				});
		findViewById(R.id.helpEmailBtn).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						String destinationAddress = hmAdmin.get("email[0]");
						cmsutil.gotoEmail(destinationAddress);
						// String subject = "문의합니다.";
						// cmsutil.gotoEmail(destinationAddress, subject);
					}
				});
		findViewById(R.id.helpWebBtn).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						String destinationAddress = hmAdmin.get("web[0]");
						cmsutil.gotoWeb(destinationAddress);
					}
				});
		findViewById(R.id.helpMapBtn).setOnClickListener(
				new Button.OnClickListener() {
					public void onClick(View v) {
						Toast.makeText(act, sStatus, Toast.LENGTH_LONG).show();
						// if (sStatus.indexOf("[3]") >= 0) {
						if (cmsutil.hmMyLocation.get("latitude") != null) {
							Log.v("latitude", cmsutil.hmMyLocation
									.get("latitude"));
							String toAddress = hmAdmin.get("address[0]");
							cmsutil.gotoWayFromMe(toAddress);
						}
					}
				});

	}

	boolean findlocation = false;
	LocationManager mLocMan;
	String mProvider;
	int mCount;

	public void startLocationService() {
		mLocMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mProvider = mLocMan.getBestProvider(new Criteria(), true);
	}

	@Override
	public void onResume() {
		Log.v(this.getLocalClassName(), "onResume");
		super.onResume();
		mCount = 0;
		mLocMan.requestLocationUpdates(mProvider, 3000, 10, mListener);
		cmsutil.hmMyLocation = cmsutil.findMyLocation();
	}

	@Override
	public void onPause() {
		Log.v(this.getLocalClassName(), "onPause");
		super.onPause();
		mLocMan.removeUpdates(mListener);
	}

	String sStatus = "[0] 현재위치를 찾는중입니다.";
	LocationListener mListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			mCount++;
			String sloc = String.format("수신회수:%d\n위도:%f\n경도:%f\n고도:%f", mCount,
					location.getLatitude(), location.getLongitude(), location
							.getAltitude());
			cmsutil.hmMyLocation.put("latitude", Double.toString(location
					.getLatitude()));
			cmsutil.hmMyLocation.put("longitude", Double.toString(location
					.getLongitude()));
			Log.v("LocationListener", sloc);
			sStatus = "[3] 현재위치를 찾았습니다.";
		}

		public void onProviderDisabled(String provider) {
			sStatus = "[-3] 서비스 사용 불가";
			Log.v("LocationListener", sStatus);
		}

		public void onProviderEnabled(String provider) {
			sStatus = "[2] 서비스 사용 가능";
			Log.v("LocationListener", sStatus);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// String sStatus = "";
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				sStatus = "[-2] 범위 벗어남";
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				sStatus = "[-1] 일시적 불능";
				break;
			case LocationProvider.AVAILABLE:
				sStatus = "[1] 현재위치를 찾는중입니다.";
				break;
			}
			Log.v("LocationListener", provider + "상태 변경 : " + sStatus);
		}
	};

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
