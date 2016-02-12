package org.androidtown.locatest;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Joonha on 2015-11-25.
 */
public class LocaHttp {
    public String encoding="UTF-8";


    //오래동안 응답이 없을 경우 통신을 자동으로 종료.(10초)
    public int REGISTRATION_TIMEOUT=10*1000;

    //LOG CAT에 기록.
    public String TAG="LocaHTTP";

    public LocaHttp() {
    }

    public void sendPost(String url,ArrayList<NameValuePair> params){
        HttpEntity entity=null;
        HttpClient client = new DefaultHttpClient();

        try{
            entity=new UrlEncodedFormEntity(params,encoding);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(entity.getContentType());
            httpPost.setEntity(entity);
            client.execute(httpPost);
        }catch(UnsupportedEncodingException e){
            throw new AssertionError(e);
        }catch(Exception e){
            Log.d("sendPost", e.toString());
        }



    }

}
