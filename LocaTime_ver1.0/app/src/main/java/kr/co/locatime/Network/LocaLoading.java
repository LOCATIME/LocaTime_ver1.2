package kr.co.locatime.Network;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Joonha on 2015-11-29.
 */
public class LocaLoading extends Application {

    public ProgressDialog progressDialog;

    public void startLoading(Context context){
        progressDialog=ProgressDialog.show(context,"로딩중","잠시만 기다려주십시오.",true,true
        );
    }

    public void endLoading(){
        endLoader endLoader = new endLoader();
        Timer timer = new Timer(false);
        timer.schedule(endLoader, 1000);
    }

    class endLoader extends TimerTask {
        endLoader() {}
        public void run() {
            progressDialog.dismiss();
        }
    }

}
