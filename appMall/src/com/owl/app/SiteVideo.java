package com.owl.app;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class SiteVideo extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_video);
        
     // top.xml////////////////////////////////////////////////////////
		top top = new top();
		top.act = this;
		top.init();
//		TextView topTitle = (TextView) findViewById(R.id.topTitle);
//		topTitle.setText(R.string.media_name);
//		ImageButton top_arrow_left = (ImageButton) findViewById(R.id.top_arrow_left);
//		top_arrow_left.setOnClickListener(new ImageButton.OnClickListener() {
//			public void onClick(View v) {
//				onBackPressed();
//			}
//		});
//		ImageButton top_arrow_right = (ImageButton) findViewById(R.id.top_arrow_right);
//		top_arrow_right.setVisibility(View.INVISIBLE);
		// top.xml////////////////////////////////////////////////////////
        
        VideoView video = (VideoView)findViewById(R.id.video); 
        Uri uri = Uri.parse("android.resource://com.owl.app/" + R.raw.owlvideo); 
        MediaController mc = new MediaController(this); 
//        MediaController mc = new MediaController(this,false); 
        video.setMediaController(mc);
        video.setVideoURI(uri); 
        video.start();
        
//        MediaPlayer mp = MediaPlayer.create(this, R.raw.owlvideo);
//        mp.start();
        
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
