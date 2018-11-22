package kr.search.phonebook.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Recent implements Serializable {
    private String num;
    private String name;
    private String shop_name;
    private int count;

    public static Recent getRecentFromJson(JSONObject json) {
        Recent r = new Recent();

        try {
            r.setNum(json.getString("num"));
            r.setName(json.getString("name"));
            r.setShop_name(json.getString("shop_name"));
            r.setCount(json.getInt("count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return r;
    }

    public Recent() {
    }

    public Recent(String num, String name, String shop_name) {
        this.num = num;
        this.name = name;
        this.shop_name = shop_name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
