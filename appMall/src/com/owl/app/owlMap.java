package com.owl.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.owl.app.cms.util;

public class owlMap extends MapActivity {
	MapView owlMap;
	public String TAG = "owlMap";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);

	    // top.xml////////////////////////////////////////////////////////
		top top = new top();
		top.act = this;
		top.init();
//		TextView topTitle = (TextView) findViewById(R.id.topTitle);
//		topTitle.setText(R.string.map_name);
//		ImageButton top_arrow_left = (ImageButton) findViewById(R.id.top_arrow_left);
//		top_arrow_left.setOnClickListener(new ImageButton.OnClickListener() {
//			public void onClick(View v) {
//				onBackPressed();
//			}
//		});
//		ImageButton top_arrow_right = (ImageButton) findViewById(R.id.top_arrow_right);
//		top_arrow_right.setVisibility(View.INVISIBLE);
		// top.xml////////////////////////////////////////////////////////
	    
		
		
		final EditText mapAddress = (EditText) findViewById(R.id.mapAddress);
		Button mapSearchBtn = (Button) findViewById(R.id.mapSearchBtn);
		mapSearchBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				String tmpKeyWord = mapAddress.getText().toString();
				doSearchMap(tmpKeyWord);
			}
		});
	    

		String queryTmp = ((owllab) this.getApplication()).getCompanyMapState();
		Log.v(TAG, queryTmp);
		if (queryTmp.length()>2) {
			mapAddress.setText(queryTmp);
			doSearchMap(mapAddress.getText().toString());
		} else {
		    owlMap = (MapView)findViewById(R.id.maps);
		    owlMap.setSatellite(true);
		    owlMap.setBuiltInZoomControls(true);
	        GeoPoint p = new GeoPoint(37566535 , 126977969);
	        MapController map = owlMap.getController();
	        map.animateTo(p);
	        map.setZoom(6);
		}
	}
	
	public util cmsutil = new util();
	
	public void doSearchMap(String query) {

		int latitude = 37566535;
		int longitude = 126977969;
		String encoding = "UTF-8";
		String queryEnc = "";
		
		try {
			queryEnc = URLEncoder.encode(query, encoding);
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		}
		String theUrl = "http://maps.google.co.kr/maps/api/geocode/json?sensor=true&address="+queryEnc;
//		String theUrl = "http://maps.google.co.kr/maps/api/geocode/xml?sensor=true&address="+queryEnc;
		Log.i(TAG, theUrl);

		cmsHTTP cmsHttp = new cmsHTTP();
		cmsHttp.encoding = encoding;
		cmsHttp.act = this;
		String tmpData = cmsHttp.sendGet(theUrl);
		
		if (tmpData==null) return;
		
		Log.i(TAG, tmpData);
		
		try {
			JSONObject jObj = new JSONObject(tmpData);
			Log.i(TAG, jObj.toString());
			if (jObj!=null) {
				Log.i(TAG, "===========================");
				Log.i(TAG, jObj.getJSONArray("results").toString());
				Log.i(TAG, jObj.getJSONArray("results").getJSONObject(0).toString());
				Log.i(TAG, jObj.getJSONArray("results").getJSONObject(0).getString("geometry"));
				Log.i(TAG, jObj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getString("location"));
				Log.i(TAG, jObj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat"));

				double tmpX = jObj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
				double tmpY = jObj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
				
				latitude = Double.valueOf(tmpX*1E6).intValue();
				longitude = Double.valueOf(tmpY*1E6).intValue();
				
				Log.i(TAG, "tmpX : "+Double.toString(tmpX));
				Log.i(TAG, "tmpY : "+Double.toString(tmpY));
				Log.i(TAG, "latitude : "+Integer.toString(latitude));
				Log.i(TAG, "longitude : "+Integer.toString(longitude));
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
		viewMap(latitude, longitude, query);
	}
	
	
	public void viewMap(int latitude, int longitude, String query) {
		owlMap = (MapView)findViewById(R.id.maps);
	    owlMap.setSatellite(false);
	    owlMap.setBuiltInZoomControls(true);
	    
        GeoPoint p = new GeoPoint(latitude , longitude);
        List<Overlay> mapOverlays = owlMap.getOverlays();

        if (mapOverlays.size() > 0) {
        	mapOverlays.clear(); 
    		Log.d(TAG, "clear overlays : " + mapOverlays.size());
    	} else {
    		Log.d(TAG, "empty overlays");
    	}
        
        Drawable marker = this.getResources().getDrawable(R.drawable.map_mark);
        
    	marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());       
        mapOverlay mapOverlay = new mapOverlay(marker);
        mapOverlay.mContext = this;
        mapOverlay.mPopulate();
        
        OverlayItem overlayitem = new OverlayItem(p, "목적지", query);
        mapOverlay.addOverlay(overlayitem);
        
        owlMap.getOverlays().add(mapOverlay);
        
        owlMap.getController().animateTo(p);
        owlMap.getController().setZoom(19);
        owlMap.postInvalidate();

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	//optionMenu//////////////////////////
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
		if (tmp) return tmp;
		else return super.onOptionsItemSelected(item);
	}
	//optionMenu//////////////////////////
}
