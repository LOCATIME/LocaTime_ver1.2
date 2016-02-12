package com.owl.app;

import com.owl.app.cms.util;

import android.app.Activity;
import android.os.Bundle;

public class myZone extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    util cmsutil = new util();
		Activity act = this;
	    cmsutil.act = this;
	    cmsutil.goActivity(act, act.getPackageName() 
	    		+ ".mallOrderList");
	}

}
