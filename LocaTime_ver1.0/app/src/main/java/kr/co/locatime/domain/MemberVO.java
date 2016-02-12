package kr.co.locatime.domain;

/**
 * Created by Joonha on 2015-12-01.
 */
public class MemberVO {
    private String user_id;
    private String user_phone;
    private String user_pw;
    private String user_name;
    private String user_kakao;

    public MemberVO() {
    }

    public MemberVO(String user_id, String user_phone, String user_pw, String user_name, String user_kakao) {
        this.user_id = user_id;
        this.user_phone = user_phone;
        this.user_pw = user_pw;
        this.user_name = user_name;
        this.user_kakao = user_kakao;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_kakao() {
        return user_kakao;
    }

    public void setUser_kakao(String user_kakao) {
        this.user_kakao = user_kakao;
    }


    @Override
    public String toString() {
        return "MemberVO{" +
                "user_id='" + user_id + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_pw='" + user_pw + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_kakao='" + user_kakao + '\'' +
                '}';
    }
}
