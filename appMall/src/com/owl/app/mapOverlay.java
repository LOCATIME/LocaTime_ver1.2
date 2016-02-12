package com.owl.app;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class mapOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> tmpOverlays = new ArrayList<OverlayItem>();
	public Context mContext;

	public mapOverlay(Drawable defaultMarker) {
		// super(arg0);
		super(boundCenterBottom(defaultMarker));
		tmpOverlays = new ArrayList<OverlayItem>();

	}

	public mapOverlay(Drawable defaultMarker, Context context) {
		super(defaultMarker);
		mContext = context;
	}

	public void addOverlay(OverlayItem overlay) {
		tmpOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		// return null;
		return tmpOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		// return 0;
		return tmpOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		 OverlayItem item = tmpOverlays.get(index);
		 AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		 dialog.setTitle(item.getTitle());
		 dialog.setMessage(item.getSnippet());
		 dialog.show();

//		if ("목적지".equals(tmpOverlays.get(index).getTitle())) {
//			Toast.makeText(getApplicationContext(),
//					tmpOverlays.get(index).getSnippet(), Toast.LENGTH_SHORT)
//					.show();
//		} else {
//
//		}

		return true;
	}

	public void mPopulate() {
		populate();
	}

}
