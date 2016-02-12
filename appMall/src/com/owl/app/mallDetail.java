package com.owl.app;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.owl.app.cms.util;

public class mallDetail extends Activity {

	public String TAG = "";
	public HashMap<String, String> hm;
	util cmsutil = new util();
	Activity act;
	Intent actIntent;
	String sort;
	String sortName;
	Spinner ea;
	Activity actNext;
	top top;
	String rowid = "";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.mall_detail);
		top = new top(this);
		top.init();

		act = this;
		rowid = getIntent().getStringExtra("rowid");
		cmsutil.act = this;
		TAG = this.getLocalClassName();
		getXMLDataList();
		btnTurnOn();
	}

	public void getXMLDataList() {
		String theUrl = "http://www.owllab.com/android/mall_detail.php";
		Log.i(TAG, theUrl);
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("rowid", rowid));
		cmsHTTP cmsHttp = new cmsHTTP();
		cmsHttp.act = act;
		String tmpData = cmsHttp.sendPost(theUrl, httpParams);
		if (tmpData == null)
			return;
		Log.i(TAG, tmpData);
		util cmsutil = new util();
		hm = cmsutil.xml2HashMap(tmpData, cmsHttp.encoding);

		if (cmsutil.str2int(hm.get("count"), 0) <= 0) {
			Toast.makeText(act.getBaseContext(), "상품정보를 찾을 수 없습니다.",
					Toast.LENGTH_LONG).show();
			((owllab) act.getApplication()).startLoading(act);
			act.onBackPressed();
			((owllab) act.getApplication()).endLoading();
		}

		sort = hm.get("sort[0]");
		sortName = hm.get("sortName[0]");
		top.setTitle(sortName);

		printInfo();
		initSpinner();
		loadImg();

	}

	public void printInfo() {
		((TextView) findViewById(R.id.prodSort)).setText(hm.get("prodSort[0]"));
		((TextView) findViewById(R.id.prodName)).setText(hm.get("prodName[0]"));
		((TextView) findViewById(R.id.prodBrief)).setText(hm
				.get("prodBrief[0]"));
		((TextView) findViewById(R.id.prodPriceText)).setText(hm
				.get("priceText[0]"));
		((TextView) findViewById(R.id.rating)).setText(hm.get("rating[0]"));
		((TextView) findViewById(R.id.ratingText)).setText(hm
				.get("ratingText[0]"));
		((TextView) findViewById(R.id.prodInfo)).setText(hm.get("prodInfo[0]"));
	}
	
	public void initSpinner() {
		String spinnerOptiona[];
		int maxEa = cmsutil.str2int(hm.get("maxEa[0]"), 0);
		spinnerOptiona = new String[maxEa + 1];
		for (int i = 0; i <= maxEa; i++) {
			spinnerOptiona[i] = String.valueOf(i);
		}
		ea = (Spinner) findViewById(R.id.ea);
		if (maxEa <= 0) {
			// ea.setPrompt("재고가 부족합니다.");
			spinnerOptiona = new String[1];
			spinnerOptiona[0] = "재고가 부족합니다.";
			ea.setEnabled(false);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerOptiona);
		ea.setAdapter(adapter);
		ea.setSelection(adapter.getPosition("1"));
		if (ea.getSelectedItemPosition() < 0) {
			ea.setSelection(0);
		}
	}

	public HashMap<String, Bitmap> hmImg = new HashMap<String, Bitmap>();
	ImageView prodImg;
	Bitmap img;
	Bitmap imgL;
	int imgOrgWidth;
	Drawable blankDw;

	public void loadImg() {

		prodImg = (ImageView) findViewById(R.id.prodImg);
		blankDw = getResources().getDrawable(R.drawable.blank);
		prodImg.setImageDrawable(blankDw);
		imgOrgWidth = 120;

		Uri uri = Uri.parse(hm.get("img[0]"));
		Log.v(TAG, uri.getPath());

		try {
			if (hmImg.get("img[0]") == null) {
				String urlstr = hm.get("img[0]");
				URL url = new URL(urlstr);
				URLConnection conn = url.openConnection();
				conn.connect();
				BufferedInputStream bis = new BufferedInputStream(conn
						.getInputStream(), 512 * 1024);
				Bitmap bm = BitmapFactory.decodeStream(bis);
				bis.close();
				hmImg.put("img[0]", bm);
			}
		} catch (IOException e) {
			Log.e(TAG, e.toString());

		}

		if (hmImg.get("img[0]") != null) {
			img = getResizedBitmap(hmImg.get("img[0]"), imgOrgWidth);
			imgL = getResizedBitmap(hmImg.get("img[0]"), imgOrgWidth * 2);
			prodImg.setImageBitmap(img);
		} else {

		}

	}

	public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		int newHeight = (int) Math.ceil(height * newWidth / width);
		return getResizedBitmap(bm, newWidth, newHeight);
	}

	public void btnTurnOn() {

		prodImg.setOnClickListener(new ImageView.OnClickListener() {
			public void onClick(View v) {
				Log.v(TAG, "" + prodImg.getWidth());
				if (imgOrgWidth >= prodImg.getWidth()) {
					if (imgL != null)
						prodImg.setImageBitmap(imgL);
				} else {
					if (img != null)
						prodImg.setImageBitmap(img);
				}
			}
		});

		((ImageButton) findViewById(R.id.mallCartBtn))
				.setOnClickListener(new ImageButton.OnClickListener() {
					public void onClick(View v) {

						int eaSelected = cmsutil.str2int(ea.getSelectedItem().toString(),0);
						if (eaSelected<=0) {
							String tmp = "주문할 수량을 선택하세요.";
							Toast.makeText(act.getBaseContext(), tmp, Toast.LENGTH_LONG).show();
							return;
						}
						
						((owllab) act.getApplication()).startLoading(act);
						actNext = cmsutil.findAct(act, act.getPackageName()
								+ ".mallCart");
						if (actNext != null) {
							Intent intent = new Intent(act, actNext.getClass());
							intent.putExtra("rowid", rowid);
							intent.putExtra("ea", ea.getSelectedItemPosition());
							act.startActivity(intent);
						}
						((owllab) act.getApplication()).endLoading();
					}
				});

		((ImageButton) findViewById(R.id.mallListBtn))
				.setOnClickListener(new ImageButton.OnClickListener() {
					public void onClick(View v) {
						((owllab) act.getApplication()).startLoading(act);
						actNext = cmsutil.findAct(act, act.getPackageName()
								+ ".mallList");
						if (actNext != null) {
							Intent intent = new Intent(act, actNext.getClass());
							intent.putExtra("sort", sort);
							intent.putExtra("subject", sortName);
							act.startActivity(intent);
						}
						((owllab) act.getApplication()).endLoading();

					}
				});
		((Button) findViewById(R.id.webInfoBtn))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						((owllab) act.getApplication()).startLoading(act);
						String destinationAddress = hm.get("web[0]");
						if (destinationAddress == null) {
							destinationAddress = hm.get("img[0]");
						}
						destinationAddress = cmsutil.str_replace("http://", "",
								destinationAddress);
						cmsutil.gotoWeb(destinationAddress);
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
