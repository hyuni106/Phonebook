package kr.idealidea.phonebook.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Contact implements Serializable {
    private int id;
    private int uid;
    private String phone;
    private String name;
    private Calendar created_at;

    public static Contact getContactFromJson(JSONObject json) {
        Contact c = new Contact();

        try {
            c.setId(json.getInt("id"));
            c.setUid(json.getInt("uid"));
            c.setPhone(json.getString("phone"));
            c.setName(json.getString("name"));

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

    public Contact() {
    }

    public Contact(int id, int uid, String phone, String name, Calendar created_at) {
        this.id = id;
        this.uid = uid;
        this.phone = phone;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Calendar created_at) {
        this.created_at = created_at;
    }
}
