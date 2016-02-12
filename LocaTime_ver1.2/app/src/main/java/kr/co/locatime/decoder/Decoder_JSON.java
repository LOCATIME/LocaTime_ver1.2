package kr.co.locatime.decoder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.locatime.domain.ContentVO;

/**
 * Created by Joonha on 2015-12-03.
 */
public class Decoder_JSON {


    //전체 판매가능 목록 받아오기ㅇ 대해 JSON 값들 처리.
    public ArrayList<ContentVO> getJSON(JSONArray array) {
        ArrayList<ContentVO> list = new ArrayList<ContentVO>();
        try {
            for (int i = 0; i < array.length(); i++) {
                ContentVO contentVO = new ContentVO();
                JSONObject json = array.getJSONObject(i);

                contentVO.setContent_title(json.getString("content_title"));
                contentVO.setContent_OriPrice(json.getInt("content_OriPrice"));
                contentVO.setContent_deadline(json.getString("content_deadline"));
                contentVO.setContent_SellingPrice(json.getInt("content_SellingPrice"));
                contentVO.setUser_kakao(json.getString("user_kakao"));
                contentVO.setUser_id(json.getString("user_id"));
                contentVO.setContent_able(json.getInt("content_able"));
                contentVO.setContent_num(json.getInt("content_num"));

                list.add(contentVO);
            }
        } catch (Exception e) {
            Log.d("JSON 해석 오류", e.toString());
        }


        return list;
    }


}
