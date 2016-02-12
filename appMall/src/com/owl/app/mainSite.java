package com.owl.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

public class mainSite extends Activity {

	WebView mainWebView;
	public String TAG = "mainSite";

	@Override
	protected void onStart() {
		super.onStart();
		Log.v(this.getLocalClassName(), "onStart");
//		noticeThread ncThread = new noticeThread();
//		ncThread.run();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(this.getLocalClassName(), "onResume");
		showDialog(PROGRESS_DIALOG);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.v(this.getLocalClassName(), "onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(this.getLocalClassName(), "onStop");
	}

	private class noticeThread extends Thread {
		noticeThread() {
		}

		public void run() {
			Log.v("noticeThread", "Thread run");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.e("ERROR", "Thread Interrupted");
			}
			showDialog(PROGRESS_DIALOG);
		}
	}

	// ProgressDialog
	static final int PROGRESS_DIALOG = 0;

	protected Dialog onCreateDialog(int id) {
		Log.v("noticeThread", "onCreateDialog");
		switch (id) {
		case PROGRESS_DIALOG:
			Intent intent = this.getIntent();
			String tabId = intent.getStringExtra("tabId");
			View tmpView = findViewById(R.id.mainMenu1);
			if ("tab2".equals(tabId)) {
				tmpView = findViewById(R.id.mainMenu2);
			} else if ("tab3".equals(tabId)) {
				tmpView = findViewById(R.id.mainMenu3);
			}
			mainContentToggle(tmpView);
			return null;
		default:
			return null;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO Auto-generated method stub
		setContentView(R.layout.main);

		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// top.xml////////////////////////////////////////////////////////
		top top = new top(this);
		top.init();

		// top top = new top();
		// top.act = this;
		// top.init();

		// TextView topTitle = (TextView) findViewById(R.id.topTitle);
		// topTitle.setText(R.string.main_name);
		// ImageButton top_arrow_left = (ImageButton)
		// findViewById(R.id.top_arrow_left);
		// top_arrow_left.setOnClickListener(new ImageButton.OnClickListener() {
		// public void onClick(View v) {
		// onBackPressed();
		// }
		// });
		// ImageButton top_arrow_right = (ImageButton)
		// findViewById(R.id.top_arrow_right);
		// top_arrow_right.setVisibility(View.INVISIBLE);
		// top.xml////////////////////////////////////////////////////////

		mainTabListenerOn();
		// mainContentToggle(findViewById(R.id.mainMenu1));

		// Intent intent = this.getIntent();
		// String tabId = intent.getStringExtra("tabId");
		// View tmpView = findViewById(R.id.mainMenu1);
		// if ("tab2".equals(tabId)) {
		// tmpView = findViewById(R.id.mainMenu2);
		// } else if ("tab3".equals(tabId)) {
		// tmpView = findViewById(R.id.mainMenu3);
		// }
		// mainContentToggle(tmpView);

		// mainWebView = (WebView) findViewById(R.id.mainWebView);
		// mainWebView.getSettings().setJavaScriptEnabled(true);
		// mainWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		// mainWebView.getSettings().setPluginsEnabled(true);
		// mainWebView.getSettings().setSupportMultipleWindows(true);
		// mainWebView.getSettings().setSupportZoom(true);
		// mainWebView.getSettings().setBuiltInZoomControls(true);
		// mainWebView.getSettings().setBlockNetworkImage(false);
		// mainWebView.getSettings().setLoadsImagesAutomatically(true);
		// mainWebView.getSettings().setUseWideViewPort(true);
		// mainWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		// mainWebView.setWebViewClient(new owlWebViewClient());
		// mainWebView.loadUrl("http://www.owllab.com/android/index.html");

		// getWindow().requestFeature(Window.FEATURE_PROGRESS);
		// mainWebView.setWebChromeClient(new WebChromeClient() {
		// public void onProgressChanged(WebView view, int progress) {
		// activity.setProgress(progress * 1000);
		// }
		// });

	}

	public void loadInfoHTML() {
		mainWebView = (WebView) findViewById(R.id.mainWebView);
		// mainWebView.getSettings().setJavaScriptEnabled(true);
		// mainWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		// mainWebView.getSettings().setPluginsEnabled(true);
		// mainWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
		// mainWebView.getSettings().setSupportMultipleWindows(true);
		// mainWebView.getSettings().setSupportZoom(true);
		// mainWebView.getSettings().setBuiltInZoomControls(true);
		// mainWebView.getSettings().setBlockNetworkImage(false);
		// mainWebView.getSettings().setLoadsImagesAutomatically(true);
		// mainWebView.getSettings().setUseWideViewPort(true);
		// mainWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mainWebView.setWebViewClient(new owlWebViewClient());
		// mainWebView.setWebChromeClient(new WebChromeClient() {
		// @Override
		// public boolean onCreateWindow(WebView view, final boolean dialog,
		// boolean userGesture, final Message resultMsg) {
		// // Request the host application to create a new Webview.
		// return true;
		// }
		//
		// });

		String theUrl = "http://www.owllab.com/android/index.php?company_name_en=OWLLAB&company_name_ko=아울컴";

		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("company_name_en", "OWL"));
		httpParams.add(new BasicNameValuePair("company_name_ko", "아울"));
		Log.i("WEB", theUrl);

		cmsHTTP cmsHttp = new cmsHTTP();
		cmsHttp.webview = mainWebView;
		cmsHttp.encoding = "UTF-8";
		cmsHttp.act = this;
		cmsHttp.getPost(theUrl, httpParams);

	}

	public void mainContentToggle(View v) {

		findViewById(R.id.main_tab1).setVisibility(View.INVISIBLE);
		findViewById(R.id.main_tab2).setVisibility(View.INVISIBLE);
		findViewById(R.id.main_tab3).setVisibility(View.INVISIBLE);

		if (v.getId() == R.id.mainMenu2) {
			findViewById(R.id.main_tab2).setVisibility(View.VISIBLE);
			loadInfoHTML();
		} else if (v.getId() == R.id.mainMenu3) {
			findViewById(R.id.main_tab3).setVisibility(View.VISIBLE);
			category categoryTmp = new category(this);
			categoryTmp.getXMLDataList();
		} else {
			findViewById(R.id.main_tab1).setVisibility(View.VISIBLE);
			notice noticeTmp = new notice(this);
			noticeTmp.getXMLDataList();
		}

		findViewById(R.id.mainMenu1).setBackgroundResource(
				R.drawable.main_menu1);
		findViewById(R.id.mainMenu2).setBackgroundResource(
				R.drawable.main_menu2);
		findViewById(R.id.mainMenu3).setBackgroundResource(
				R.drawable.main_menu3);

		if (v.getId() == R.id.mainMenu2)
			v.setBackgroundResource(R.drawable.main_menu2_on);
		else if (v.getId() == R.id.mainMenu3)
			v.setBackgroundResource(R.drawable.main_menu3_on);
		else
			v.setBackgroundResource(R.drawable.main_menu1_on);

	}

	public Activity activity = this;

	public void mainTabListenerOn() {
		findViewById(R.id.mainMenu1).setOnClickListener(
				new ImageButton.OnClickListener() {
					public void onClick(View v) {
						mainContentToggle(v);
					}
				});
		findViewById(R.id.mainMenu2).setOnClickListener(
				new ImageButton.OnClickListener() {
					public void onClick(View v) {
						mainContentToggle(v);
					}
				});
		findViewById(R.id.mainMenu3).setOnClickListener(
				new ImageButton.OnClickListener() {
					public void onClick(View v) {
						mainContentToggle(v);
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (findViewById(R.id.main_tab3).getVisibility() == View.VISIBLE) {
			if ((keyCode == KeyEvent.KEYCODE_BACK) && mainWebView.canGoBack()) {
				mainWebView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	// final Activity activity = this;
	private class owlWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Toast.makeText(activity, "Oh no! " + description,
					Toast.LENGTH_SHORT).show();
		}
	}

	// ///////////////////////////////////////////////////////////////////////

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
