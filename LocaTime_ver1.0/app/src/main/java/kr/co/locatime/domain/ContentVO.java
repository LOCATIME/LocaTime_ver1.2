package kr.co.locatime.domain;

/**
 * Created by Joonha on 2015-11-26.
 */
public class ContentVO {
    private String user_id;
    private int content_able;
    private int content_SellingPrice;
    private String content_deadline;
    private int content_OriPrice;
    private String content_title;
    private int content_num;
    private String user_kakao;

    public ContentVO() {
    }

    @Override
    public String toString() {
        return "ContentVO{" +
                "user_id='" + user_id + '\'' +
                ", content_able=" + content_able +
                ", content_SellingPrice=" + content_SellingPrice +
                ", content_deadline='" + content_deadline + '\'' +
                ", content_OriPrice=" + content_OriPrice +
                ", content_title='" + content_title + '\'' +
                ", content_num=" + content_num +
                ", user_kakao='" + user_kakao + '\'' +
                '}';
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getContent_able() {
        return content_able;
    }

    public void setContent_able(int content_able) {
        this.content_able = content_able;
    }

    public int getContent_SellingPrice() {
        return content_SellingPrice;
    }

    public void setContent_SellingPrice(int content_SellingPrice) {
        this.content_SellingPrice = content_SellingPrice;
    }

    public String getContent_deadline() {
        return content_deadline;
    }

    public void setContent_deadline(String content_deadline) {
        this.content_deadline = content_deadline;
    }

    public int getContent_OriPrice() {
        return content_OriPrice;
    }

    public void setContent_OriPrice(int content_OriPrice) {
        this.content_OriPrice = content_OriPrice;
    }

    public String getContent_title() {
        return content_title;
    }

    public void setContent_title(String content_title) {
        this.content_title = content_title;
    }

    public int getContent_num() {
        return content_num;
    }

    public void setContent_num(int content_num) {
        this.content_num = content_num;
    }

    public String getUser_kakao() {
        return user_kakao;
    }

    public void setUser_kakao(String user_kakao) {
        this.user_kakao = user_kakao;
    }


}
