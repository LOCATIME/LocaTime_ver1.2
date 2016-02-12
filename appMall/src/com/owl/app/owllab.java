package com.owl.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

public class owllab extends Application {

	private String companyMapState;
	public HashMap<String,String> authHM = new HashMap<String, String>();
	public HttpClient httpClient = new DefaultHttpClient();

	public String getCompanyMapState() {
		return companyMapState;
	}

	public void setCompanyMapState(String s) {
		companyMapState = s;
	}
	
	public ProgressDialog loadingDialog;

	public void startLoading(Context ctx) {
		loadingDialog = ProgressDialog.show(ctx, "Loading...", "Please wait...",
				false, true);
		Log.v("owllab", "startLoading" + ctx.toString());
	}

	public void endLoading() {
		Log.v("owllab", "endLoading");
		endLoader endLoader = new endLoader();
		Timer timer = new Timer(false);
		timer.schedule(endLoader, 1000);
	}

	class endLoader extends TimerTask {
		endLoader() {}
		public void run() {
			loadingDialog.dismiss();
		}
	}
	
	public MediaPlayer mp;
	
	public void prepareMP3(Context ctx) {
		if (mp==null) {
			mp = MediaPlayer.create(ctx, R.raw.doc);
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	// if (act!=null) ((owllab) act.getApplication()).startLoading(act);
	// if (act!=null) ((owllab) act.getApplication()).endLoading();
}
