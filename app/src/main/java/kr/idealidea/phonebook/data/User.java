package kr.idealidea.phonebook.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String user_id;
    private String auth;
    private String name;
    private boolean isAdmin = false;
    private Period userPeriod = new Period();

    public static User getUserFromJson(JSONObject json) {
        User u = new User();

        try {
            u.setId(json.getInt("id"));
            u.setUser_id(json.getString("user_id"));
            u.setAuth(json.getString("auth"));
            u.setName(json.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }

    public User() {
    }

    public User(int id, String user_id, String auth, String name) {
        this.id = id;
        this.user_id = user_id;
        this.auth = auth;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Period getUserPeriod() {
        return userPeriod;
    }

    public void setUserPeriod(Period userPeriod) {
        this.userPeriod = userPeriod;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
