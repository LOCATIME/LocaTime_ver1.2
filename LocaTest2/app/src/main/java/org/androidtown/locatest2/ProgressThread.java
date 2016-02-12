package org.androidtown.locatest2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by Joonha on 2015-11-27.
 */
public class ProgressThread extends Thread {
    Handler mhandler;
    final static int STATE_DONE=0;
    final static int STATE_RUNNING=1;

    int mState;
    int total;

    ProgressThread(Handler h){
        mhandler=h;
    }

    @Override
    public void run() {
        mState=STATE_RUNNING;
        total=0;
        while(mState==STATE_RUNNING){
            try{sleep(100);
            }catch (InterruptedException e){
                    Log.e("ERROR","Thread Interrupted");
            }
            Message msg = mhandler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt("total",total);
            msg.setData(b);
            mhandler.sendMessage(msg);
            total++;
        }

    }
        public void setState(int state) {
            mState = state;

        }



}
