package kr.idealidea.phonebook.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message implements Serializable {
    private int id;
    private int uid;
    private String phone;
    private String content;
    private String shop_name;
    private Calendar created_at;

    public static Message getContactFromJson(JSONObject json) {
        Message m = new Message();

        try {
            m.setId(json.getInt("id"));
            m.setUid(json.getInt("uid"));
            m.setPhone(json.getString("phone"));
            m.setContent(json.getString("content"));
            m.setShop_name(json.getString("shop_name"));

            SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar createdTime = Calendar.getInstance();
            createdTime.setTime(myDateFormat.parse(json.getString("created_at")));
            m.setCreated_at(createdTime);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return m;
    }

    public Message() {
    }

    public Message(int id, int uid, String phone, String content, Calendar created_at) {
        this.id = id;
        this.uid = uid;
        this.phone = phone;
        this.content = content;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Calendar created_at) {
        this.created_at = created_at;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}
