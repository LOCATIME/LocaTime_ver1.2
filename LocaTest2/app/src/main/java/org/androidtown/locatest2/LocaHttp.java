package org.androidtown.locatest2;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joonha on 2015-11-26.
 */
public class LocaHttp {



    public List<ContentVO> receiveContentAll(String url) {
        List<ContentVO> list = null;
        try {

            HttpClient client = new DefaultHttpClient();

            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            list = getXML(is);

        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return list;
    }

    public List<ContentVO> getXML(InputStream is){
        List<ContentVO> list = new ArrayList<>();

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            ContentVO contentVO = null;
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        String startTag=parser.getName();
                        if(startTag.equals("content")){
                            contentVO = new ContentVO();
                        }
                        if(contentVO !=null){
                            if(startTag.equals("content_title")){
                                contentVO.setContent_title(String.valueOf(parser.nextText()));
                            }
                            if(startTag.equals("content_OriPrice")){
                                contentVO.setContent_OriPrice(Integer.parseInt(parser.nextText()));
                            }
                            if(startTag.equals("content_deadline")){
                                contentVO.setContent_deadline(String.valueOf(parser.nextText()));
                            }
                            if(startTag.equals("content_SellingPrice")){
                                contentVO.setContent_SellingPrice(Integer.parseInt(parser.nextText()));
                            }
                            if(startTag.equals("user_kakao")){
                                contentVO.setUser_kakao(String.valueOf(parser.nextText()));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        String endTag=parser.getName();
                        if(endTag.equals("content")){
                            list.add(contentVO);
                        }
                        break;
                }//end of swtich
                eventType = parser.next();
            }//end of while


        }catch(Exception e){
            Log.d("Error...",e.toString());
        }
        return list;
    }
}
