package kr.co.locatime.Network;

import android.app.Activity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kr.co.locatime.decoder.Decoder_JSON;
import kr.co.locatime.decoder.Decoder_Stream;
import kr.co.locatime.decoder.Decoder_XML;
import kr.co.locatime.domain.ContentVO;
import kr.co.locatime.domain.MemberVO;

/**
 * Created by Joonha on 2015-11-26.
 */
public class LocaHttp {
    public String encoding = "UTF-8";

    //오래동안 응답이 없을 경우 통신을 자동으로 종료.(10초)
    public int REGISTRATION_TIMEOUT = 5 * 1000;
    public int REGISTRATION_FAIL = 1001;
    //LOG CAT에 기록.
    public String TAG = "LocaHTTP";

    public Activity act;

    public String nodata = "죄송합니다. \n네트워크 장애가 있습니다.\n 다시 시도해주세요.";

    public int result;


    //회원등록
    public void registMember(String url, ArrayList<NameValuePair> params) {
        if (act != null) {
            ((LocaLoading) act.getApplication()).startLoading(act);
        }
        HttpEntity entity = null;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        try {
            entity = new UrlEncodedFormEntity(params, encoding);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(entity.getContentType());
            httpPost.setEntity(entity);

            TimeOut(client.getParams());

            response = client.execute(httpPost);
            InputStream is = response.getEntity().getContent();
            Decoder_Stream decoderStream = new Decoder_Stream();
            result = Integer.parseInt(decoderStream.convertToString(is));
            Log.d("response_test1", "*" + result + "*");
            is.close();
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        } catch (Exception e) {
            Log.d("sendPost", e.toString());
        }
        if (act != null) {
            ((LocaLoading) act.getApplication()).endLoading();
        }
    }


    //상품등록
    public void registContent(String requestUrl, ArrayList<NameValuePair> httpParams) {
        HttpEntity entity = null;
        HttpClient client = new DefaultHttpClient();

        try {
            entity = new UrlEncodedFormEntity(httpParams, encoding);
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.addHeader(entity.getContentType());
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            InputStream is = response.getEntity().getContent();
            Decoder_Stream decoderStream = new Decoder_Stream();
            result = Integer.parseInt(decoderStream.convertToString(is));
            Log.d("response_test1", "*" + result + "*");
            is.close();

        } catch (Exception e) {
            Log.d("Regsit Content Error", e.toString());
        }

    }


    //상품 판매가능 상태 업데이트
    public void updateAble(String requestUrl, ArrayList<NameValuePair> httpParams) {
        HttpEntity entity = null;
        HttpClient client = new DefaultHttpClient();
        try {
            entity = new UrlEncodedFormEntity(httpParams, encoding);
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.addHeader(entity.getContentType());
            httpPost.setEntity(entity);
            client.execute(httpPost);
        } catch (Exception e) {
            Log.d("update", e.toString());
        }

    }


    private void TimeOut(HttpParams params) {
        HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
        ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
    }

    //리스트 받아오기.(전체)
    public ArrayList<ContentVO> receiveContentAll(String urlStr) {
        /*
        List<ContentVO> list = null;
        try {

            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost(url);
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            list = getXML(is);

        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return list;*/
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream buf = null;
        String line = null;

        String page = "";
        ArrayList<ContentVO> list = new ArrayList<ContentVO>();

        try {

            url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);

            buf = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "UTF-8"));

            while ((line = bufreader.readLine()) != null) {
                Log.d("line:", line);
                page += line;
            }

            JSONArray array = new JSONArray(page);
            Decoder_JSON decoder_json = new Decoder_JSON();
            list = decoder_json.getJSON(array);

        } catch (Exception e) {
            Log.d("Error:", e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return list;
    }

    //가격순으로 받기.
    public ArrayList<ContentVO> receiveContentAllPrice(String url) {
        ArrayList<ContentVO> list = null;
        try {
            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost(url);
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            Decoder_XML decoder_xml = new Decoder_XML();
            list = decoder_xml.getXML(is);

        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return list;

    }

    //유저의 판매목록 받아오기.
    public List<ContentVO> receiveContentAll(String url, ArrayList<NameValuePair> httpParams) {
        List<ContentVO> list = null;
        try {
            HttpEntity user_entity = null;
            user_entity = new UrlEncodedFormEntity(httpParams, encoding);
            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost(url);
            post.addHeader(user_entity.getContentType());
            post.setEntity(user_entity);
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            Decoder_XML decoder_xml = new Decoder_XML();
            list = decoder_xml.getXML(is);

        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return list;
    }

    /*public int LoginAction(String url,ArrayList<NameValuePair> params){
        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (act != null)
                    ((LocaLoading)act.getApplication()).startLoading(act);
            }
        });


        int result_Login=LoginActionOnly(url,params);

        if (act != null)
            ((LocaLoading) act.getApplication()).endLoading();
        return result_Login;

    }*/

    //로그인 결과처리
    public int LoginAction(String url, ArrayList<NameValuePair> params) {

        int result_login = REGISTRATION_FAIL;


        HttpClient client = new DefaultHttpClient();
        try {
            HttpEntity sendEntity = null;
            sendEntity = new UrlEncodedFormEntity(params, encoding);

            HttpPost post = new HttpPost(url);

            post.addHeader(sendEntity.getContentType());

            post.setEntity(sendEntity);
            HttpResponse response = client.execute(post);


            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            Decoder_XML decoderXml = new Decoder_XML();
            result_login = decoderXml.getLoginXML(is);

        } catch (Exception e) {
            Log.d("Error", e.toString() + "로그인 에러");
        }
        return result_login;
    }

    //마이페이지
    public MemberVO myPage(String url, ArrayList<NameValuePair> params) {
        MemberVO memberVO = new MemberVO();
        HttpClient client = new DefaultHttpClient();
        try {
            HttpEntity sendEntity = null;
            sendEntity = new UrlEncodedFormEntity(params, encoding);
            HttpPost post = new HttpPost(url);

            post.addHeader(sendEntity.getContentType());

            post.setEntity(sendEntity);
            HttpResponse response = client.execute(post);


            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            Decoder_XML decoder_xml = new Decoder_XML();
            memberVO = decoder_xml.getMyPageXML(is);
        } catch (Exception e) {
            Log.d("My page 불러오기 에러", e.toString());
        }

        return memberVO;
    }

    //정보수정
    public void updateMember(String requestURL, ArrayList<NameValuePair> httpParams) {

        HttpClient client = new DefaultHttpClient();
        try {
            HttpEntity sendEntity = null;
            sendEntity = new UrlEncodedFormEntity(httpParams, encoding);
            HttpPost post = new HttpPost(requestURL);

            post.addHeader(sendEntity.getContentType());

            post.setEntity(sendEntity);
            client.execute(post);
        } catch (Exception e) {
            Log.d("updateMember error", e.toString());
        }


    }

    //아이디 중복확인
    public int idCheck(String url, ArrayList<NameValuePair> httpParams) {
        int checkresult = 0;
        HttpEntity entity = null;
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        try {
            entity = new UrlEncodedFormEntity(httpParams, encoding);
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(entity.getContentType());
            httpPost.setEntity(entity);


            response = client.execute(httpPost);
            InputStream is = response.getEntity().getContent();
            Decoder_Stream decoderStream = new Decoder_Stream();
            checkresult = Integer.parseInt(decoderStream.convertToString(is));

            is.close();
        } catch (Exception e) {
            Log.d("sendPost", e.toString());
        }

        return checkresult;

    }

    // 회원 탈퇴
    public int dropMember(String requestURL, ArrayList<NameValuePair> httpParams) {
        int result_drop = 0;

        HttpClient client = new DefaultHttpClient();

        try {
            HttpEntity sendEntity = null;
            sendEntity = new UrlEncodedFormEntity(httpParams, encoding);
            HttpPost post = new HttpPost(requestURL);

            post.addHeader(sendEntity.getContentType());
            post.setEntity(sendEntity);

            HttpResponse response = client.execute(post);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            Decoder_XML decoderXml = new Decoder_XML();
            result_drop = decoderXml.getLoginXML(is);
        } catch (Exception e) {
            Log.d("dropMember Error", e.toString());
        }
        return result_drop;
    }


}
