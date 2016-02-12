package com.owl.app;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.owl.app.cms.util;

public class appSite extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);
		setContentView(R.layout.intro);
		
		

		ImageButton icon1 = (ImageButton) findViewById(R.id.img_app_mark);
		// Drawable alpha1 = icon1.getBackground();
		// alpha1.setAlpha(0);
		icon1.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
//				Intent intent = new Intent(appSite.this, mainSite.class);
//				startActivity(intent);
				showDialog(PROGRESS_DIALOG);
			}
		});

		ImageButton icon2 = (ImageButton) findViewById(R.id.anim_bg);
		icon2.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(appSite.this, mainSite.class);
				startActivity(intent);
			}
		});
		
		registerForContextMenu(icon2);

		final TextView companyMapBtn = (TextView) findViewById(R.id.companyMapBtn);
		companyMapBtn.setOnClickListener(new TextView.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(appSite.this, owlMap.class);
				startActivity(intent);
				((owllab) appSite.this.getApplication())
						.setCompanyMapState(companyMapBtn.getText().toString());
			}
		});

		TextView GoogleMap = (TextView) findViewById(R.id.GoogleMap_btn);
		GoogleMap.setOnClickListener(new TextView.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(appSite.this, owlMap.class);
				startActivity(intent);
				((owllab) appSite.this.getApplication()).setCompanyMapState("");
			}
		});

		TextView video_btn = (TextView) findViewById(R.id.video_btn);
		video_btn.setOnClickListener(new TextView.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(appSite.this, SiteVideo.class);
				startActivity(intent);
			}
		});

		// ImageView image = new ImageView(this);
		// image.setImageResource(R.drawable.app_mark);
		// setContentView(image);

		ImageView image = (ImageView) findViewById(R.id.img_app_mark);
		Animation tween = AnimationUtils.loadAnimation(this, R.anim.tween);
		image.startAnimation(tween);

		// Animation alphaAnim = AnimationUtils.loadAnimation(this,
		// R.anim.alpha);
		// image.startAnimation(alphaAnim);
		// 자동 import 선언 : Mac(command+shift+O), MS(ctrl+shift+O)

		// ImageView img = (ImageView)findViewById(R.id.anim_bg);
		// img.setBackgroundResource(R.drawable.anim_list);
		//		
		// AnimationDrawable frameAnimation = (AnimationDrawable)
		// img.getBackground();
		// frameAnimation.start();

		ImageView img = (ImageView) findViewById(R.id.anim_bg);
		img.setBackgroundResource(R.drawable.anim_list);

		slideStart slideS = new slideStart();
		slideEnd slideE = new slideEnd();
		//
		Timer timerS = new Timer(false);
		timerS.schedule(slideS, 1000);
		// Timer timerE = new Timer(false);
		// timerE.schedule(slideE, 10000);
	}

	class slideStart extends TimerTask {
		slideStart() {
		}

		public void run() {
			ImageView img = (ImageView) findViewById(R.id.anim_bg);
			AnimationDrawable frameAnimation = (AnimationDrawable) img
					.getBackground();
			frameAnimation.start();
		}
	}

	class slideEnd extends TimerTask {
		slideEnd() {
		}

		public void run() {
			ImageView img = (ImageView) findViewById(R.id.anim_bg);
			AnimationDrawable frameAnimation = (AnimationDrawable) img
					.getBackground();
			frameAnimation.stop();
		}
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

	// context menu//////////////////////////
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	Activity act = this;
	@Override
	public boolean onContextItemSelected(MenuItem item) {
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		util cmsutil = new util();
//		Activity actNext;
//		Intent intent;
		
		if (act!=null) ((owllab) act.getApplication()).startLoading(act);
		
		switch (item.getItemId()) {
		case R.id.goMain:
			cmsutil.goActivity(this, this.getPackageName() + ".mainSite");
			return returnBool(true);
		case R.id.goCompanyMap:
			cmsutil.goActivity(this, this.getPackageName() + ".owlMap");
			((owllab) this.getApplication()).setCompanyMapState(this
					.getResources().getString(R.string.company_address));
			return returnBool(true);
		case R.id.goGoogleMap:
			cmsutil.goActivity(this, this.getPackageName() + ".owlMap");
			((owllab) this.getApplication()).setCompanyMapState("");
			return returnBool(true);
		case R.id.goOwlVideo:
			cmsutil.goActivity(this, this.getPackageName() + ".SiteVideo");
			return returnBool(true);
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	public boolean returnBool(boolean b) {
		if (act!=null) ((owllab) act.getApplication()).endLoading();
		return b;
	}
	// context menu//////////////////////////
	
	// ProgressDialog//////////////////////////
	static final int PROGRESS_DIALOG = 0;
    ProgressDialog progressDialog;
    ProgressThread progressThread;
	
    private class ProgressThread extends Thread {
        Handler mHandler;
        final static int STATE_DONE = 0;
        final static int STATE_RUNNING = 1;
        int mState;
        int total;
       
        ProgressThread(Handler h) {
            mHandler = h;
        }
       
        public void run() {
            mState = STATE_RUNNING;   
            total = 0;
            while (mState == STATE_RUNNING) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e("ERROR", "Thread Interrupted");
                }
                Message msg = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putInt("total", total);
                msg.setData(b);
                mHandler.sendMessage(msg);
                total++;
            }
        }
        
        public void setState(int state) {
            mState = state;
        }
    }
    
	protected Dialog onCreateDialog(int id) {
        switch(id) {
        case PROGRESS_DIALOG:
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Loading...");
            progressThread = new ProgressThread(handler);
            progressThread.start();
            progressDialog.setMax(10);
            return progressDialog;
        default:
            return null;
        }
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int total = msg.getData().getInt("total");
            progressDialog.setProgress(total);
            if (total >= 10){
				Intent intent = new Intent(appSite.this, mainSite.class);
				startActivity(intent);
				dismissDialog(PROGRESS_DIALOG);
                progressThread.setState(ProgressThread.STATE_DONE);
            }
        }
    };

    // ProgressDialog//////////////////////////

}
