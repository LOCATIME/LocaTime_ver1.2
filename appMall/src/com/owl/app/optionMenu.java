package com.owl.app;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.owl.app.cms.util;

public class optionMenu {
	public Activity act;

	public optionMenu() {

	}

	public optionMenu(Activity actTmp) {
		act = actTmp;
	}

	public boolean initSelected(MenuItem item) {
		Log.v("getPackageCodePath", act.getPackageCodePath());
		Log.v("getLocalClassName", act.getLocalClassName());
		Log.v("getPackageName", act.getPackageName());
		Log.v("getPackageResourcePath", act.getPackageResourcePath());

		// VERBOSE/getPackageCodePath(305): /data/app/com.owl.app-2.apk
		// VERBOSE/getLocalClassName(305): appSite
		// VERBOSE/getPackageName(305): com.owl.app
		// VERBOSE/getPackageResourcePath(305): /data/app/com.owl.app-2.apk

		if (act != null)
			((owllab) act.getApplication()).startLoading(act);

		util cmsutil = new util();
		Activity actNext;
		Intent intent;

		switch (item.getItemId()) {
		case R.id.goHome:
			// cmsutil.goActivity(this, "com.owl.app.appSite");
			cmsutil.goActivity(act, act.getPackageName() + ".appSite");
			return returnBool(true);
		case R.id.goSearch:
			cmsutil.goActivity(act, act.getPackageName() + ".mallList");
			return returnBool(true);
		case R.id.goMyZone:
			cmsutil.goActivity(act, act.getPackageName() + ".myZone");
			return returnBool(true);
		case R.id.goNotice:
			cmsutil.goActivity(act, act.getPackageName() + ".mainSite");
			return returnBool(true);
		case R.id.goMap:
			cmsutil.goActivity(act, act.getPackageName() + ".owlMap");
			((owllab) act.getApplication()).setCompanyMapState(act
					.getResources().getString(R.string.company_address));
			return returnBool(true);
		case R.id.goVideo:
			cmsutil.goActivity(act, act.getPackageName() + ".SiteVideo");
			return returnBool(true);
		case R.id.goSong:
			cmsutil.goActivity(act, act.getPackageName() + ".mainSite");
			return returnBool(true);
		case R.id.goLogin:
			cmsutil.goActivity(act, act.getPackageName() + ".loginAct");
			return returnBool(true);
		case R.id.goLogout:
			cmsutil.goActivity(act, act.getPackageName() + ".logoutAct");
			return returnBool(true);
		case R.id.goUserAdd:
			cmsutil.goActivity(act, act.getPackageName() + ".userAgreement");
			return returnBool(true);
		case R.id.goUserInfo:
			cmsutil.goActivity(act, act.getPackageName() + ".userInfo");
			return returnBool(true);
		case R.id.goInfo:
			actNext = cmsutil.findAct(act, act.getPackageName() + ".mainSite");
			intent = new Intent(act, actNext.getClass());
			intent.putExtra("tabId", "tab2");
			act.startActivity(intent);
			return returnBool(true);
		case R.id.goCategory:
			actNext = cmsutil.findAct(act, act.getPackageName() + ".mainSite");
			intent = new Intent(act, actNext.getClass());
			intent.putExtra("tabId", "tab3");
			act.startActivity(intent);
			return returnBool(true);
		case R.id.goCart:
			cmsutil.goActivity(act, act.getPackageName() + ".mallCart");
			return returnBool(true);
		case R.id.goAdmin:
			cmsutil.goActivity(act, act.getPackageName() + ".adminAct");
			return returnBool(true);
		case R.id.goHelpDesk:
			cmsutil.goActivity(act, act.getPackageName() + ".helpDesk");
			return returnBool(true);
		default:
			return returnBool(false);
		}
	}

	public boolean returnBool(boolean b) {
		if (act != null)
			((owllab) act.getApplication()).endLoading();
		return b;
	}

	util cmsutil = new util();

	public void initMenu(Menu menu) {
		if (menu != null) {
			Log.v("initMenu", menu.toString());
			if (cmsutil.getLoginState(act)) {
				menu.findItem(R.id.goLogin).setVisible(false).setEnabled(false);
				menu.findItem(R.id.goLogout).setVisible(true).setEnabled(true);
				menu.findItem(R.id.goUserAdd).setVisible(false).setEnabled(false);
				menu.findItem(R.id.goUserInfo).setVisible(true).setEnabled(true);
			} else {
				menu.findItem(R.id.goLogin).setVisible(true).setEnabled(true);
				menu.findItem(R.id.goLogout).setVisible(false)
						.setEnabled(false);
				menu.findItem(R.id.goUserAdd).setVisible(true).setEnabled(true);
				menu.findItem(R.id.goUserInfo).setVisible(false).setEnabled(false);
			}
			
			if (cmsutil.getAuthLevel(act)>=3) {
				menu.findItem(R.id.goAdmin).setVisible(true).setEnabled(true);
			} else {
				menu.findItem(R.id.goAdmin).setVisible(false).setEnabled(false);
			}
		}

	}
}
