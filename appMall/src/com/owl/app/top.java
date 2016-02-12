package com.owl.app;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class top {

	public Activity act;
	public MediaPlayer mp;

	public top() {

	}

	public top(Activity tmpact) {
		act = tmpact;
	}

	public void init() {
		// top.xml////////////////////////////////////////////////////////
		TextView topTitle = (TextView) act.findViewById(R.id.topTitle);
//		topTitle.setText(R.string.main_name);
		topTitle.setText(act.getTitle());
		
		ImageButton top_arrow_left = (ImageButton) act
				.findViewById(R.id.top_arrow_left);
		top_arrow_left.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) {
				backgroundStop();
				act.onBackPressed();
			}
		});
		ImageButton top_arrow_right = (ImageButton) act
				.findViewById(R.id.top_arrow_right);
		top_arrow_right.setVisibility(View.INVISIBLE);
		// top.xml////////////////////////////////////////////////////////

		// audio/////////
		owllab owllab = ((owllab) act.getApplication());
		owllab.prepareMP3(act);
		mp = owllab.mp;

		if (mp.isPlaying()) {
			ImageView img = (ImageView) act.findViewById(R.id.equal_bg);
			AnimationDrawable equalAnim = (AnimationDrawable) img.getBackground();
			ImageView playBtn = (ImageView) act.findViewById(R.id.play_btn);
			playBtn.getBackground().setLevel(1);
			equalAnim.start();
		}

//		mp = MediaPlayer.create(act, R.raw.doc);
//		try {
//			mp.prepare();
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			Toast.makeText(act, e.getMessage(), Toast.LENGTH_SHORT).show();
//		}

		ImageView playBtn = (ImageView) act.findViewById(R.id.play_btn);
		playBtn.setOnClickListener(new ImageView.OnClickListener() {
			public void onClick(View v) {
				audioControl();
			}
		});

		ImageView equalBg = (ImageView) act.findViewById(R.id.equal_bg);
		equalBg.setOnClickListener(new ImageView.OnClickListener() {
			public void onClick(View v) {
				audioInfo();
			}
		});
		
		// audio/////////

	}

	public void audioInfo() {
		AlertDialog dialog = new AlertDialog.Builder(act).create();
		dialog.setTitle(act.getString(R.string.audio_title));
		dialog.setMessage(act.getString(R.string.audio_info));
		dialog.setIcon(act.getResources().getDrawable(R.drawable.swak));
		dialog.setButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		dialog.show();
	}

	public void audioControl() {
		ImageView img = (ImageView) act.findViewById(R.id.equal_bg);
		final AnimationDrawable equalAnim = (AnimationDrawable) img.getBackground();
		final ImageView playBtn = (ImageView) act.findViewById(R.id.play_btn);
		if (playBtn.getBackground().getLevel() == 0) {
			playBtn.getBackground().setLevel(1);
			mp.start();
			mp.setVolume(10, 10);
			equalAnim.start();
			mp.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer arg0) {
					playBtn.getBackground().setLevel(0);
					equalAnim.stop();
				}
			});
		} else {
			playBtn.getBackground().setLevel(0);
			mp.pause();
			equalAnim.stop();
		}
	}
	
	public void backgroundStop() {
		ImageView img = (ImageView) act.findViewById(R.id.equal_bg);
		AnimationDrawable equalAnim = (AnimationDrawable) img.getBackground();
		ImageView playBtn = (ImageView) act.findViewById(R.id.play_btn);
		playBtn.getBackground().setLevel(0);
		mp.pause();
		equalAnim.stop();
	}
	
	public void setTitle(String title) {
		TextView topTitle = (TextView) act.findViewById(R.id.topTitle);
		topTitle.setText(title);
	}
}
