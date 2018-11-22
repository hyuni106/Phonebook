package kr.search.phonebook.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CallLog implements Serializable {
    private int id;
    private int uid;
    private String name;
    private String log_type;
    private String phone;
    private String time;
    private String shop_name;
    private Calendar created_at;

    public static CallLog getCallLogsFromJson(JSONObject json) {
        CallLog c = new CallLog();

        try {
            c.setId(json.getInt("id"));
            c.setUid(json.getInt("uid"));
            c.setLog_type(json.getString("log_type"));
            c.setPhone(json.getString("phone"));
            c.setName(json.getString("name"));
            c.setTime(json.getString("time"));
            c.setShop_name(json.getString("shop_name"));

            SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar createdTime = Calendar.getInstance();
            createdTime.setTime(myDateFormat.parse(json.getString("created_at")));
            c.setCreated_at(createdTime);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return c;
    }

    public CallLog() {
    }

    public CallLog(int id, int uid, String name, String log_type, String phone, String time, Calendar created_at) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.log_type = log_type;
        this.phone = phone;
        this.time = time;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLog_type() {
        return log_type;
    }

    public void setLog_type(String log_type) {
        this.log_type = log_type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
