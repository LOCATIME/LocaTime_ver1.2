package kr.co.locatime.decoder;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

import kr.co.locatime.domain.ContentVO;
import kr.co.locatime.domain.MemberVO;

/**
 * Created by Joonha on 2015-12-03.
 */
public class Decoder_XML {

    //리스트 xml 해독기.(선택 xml도 포함)
    public ArrayList<ContentVO> getXML(InputStream is) {
        ArrayList<ContentVO> list = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            ContentVO contentVO = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (startTag.equals("content")) {
                            contentVO = new ContentVO();
                        }
                        if (contentVO != null) {
                            if (startTag.equals("content_title")) {
                                contentVO.setContent_title(String.valueOf(parser.nextText()));
                            }
                            if (startTag.equals("content_OriPrice")) {
                                contentVO.setContent_OriPrice(Integer.parseInt(parser.nextText()));
                            }
                            if (startTag.equals("content_deadline")) {
                                contentVO.setContent_deadline(String.valueOf(parser.nextText()));
                            }
                            if (startTag.equals("content_SellingPrice")) {
                                contentVO.setContent_SellingPrice(Integer.parseInt(parser.nextText()));
                            }
                            if (startTag.equals("user_kakao")) {
                                contentVO.setUser_kakao(String.valueOf(parser.nextText()));
                            }
                            if (startTag.equals("user_id")) {
                                contentVO.setUser_id(String.valueOf(parser.nextText()));
                            }
                            if (startTag.equals("content_able")) {
                                contentVO.setContent_able(Integer.parseInt(parser.nextText()));
                            }
                            if (startTag.equals("content_num")) {
                                contentVO.setContent_num(Integer.parseInt(parser.nextText()));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:

                        String endTag = parser.getName();
                        if (endTag.equals("content")) {
                            list.add(contentVO);
                        }
                        break;
                }//end of swtich
                eventType = parser.next();
            }//end of while


        } catch (Exception e) {
            Log.d("Error...", e.toString());
        }
        return list;
    }

    //마이페이지 정보 XML 처리.
    public MemberVO getMyPageXML(InputStream is) {

        MemberVO memberVO = new MemberVO();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();

                        if (memberVO != null) {
                            if (startTag.equals("user_id")) {
                                memberVO.setUser_id(String.valueOf(parser.nextText()));
                            }
                            if (startTag.equals("user_name")) {
                                memberVO.setUser_name(String.valueOf(parser.nextText()));
                            }
                            if (startTag.equals("user_phone")) {
                                memberVO.setUser_phone(String.valueOf(parser.nextText()));
                            }
                            if (startTag.equals("user_kakao")) {
                                memberVO.setUser_kakao(String.valueOf(parser.nextText()));
                            }
                        }
                        break;
                }//end of swtich
                eventType = parser.next();
            }//end of while


        } catch (Exception e) {
            Log.d("Analysis Error...", e.toString());
        }
        return memberVO;

    }


    //로그인 xml 해독기.
    public int getLoginXML(InputStream is) {
        int result_login = 0;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if (startTag.equals("result")) {
                            result_login = Integer.parseInt(parser.nextText());
                        }
                        break;
                    default:
                        result_login = 0;
                        break;
                }//end of swtich
                eventType = parser.next();
            }//end of while


        } catch (Exception e) {
        }
        return result_login;

    }


}
