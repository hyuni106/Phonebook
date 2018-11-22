package kr.search.phonebook.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Period implements Serializable {
    private int id;
    private String start;
    private String end;

    public static Period getPeriodFromJson(JSONObject json) {
        Period p = new Period();

        try {
            p.setId(json.getInt("id"));
            p.setStart(json.getString("start"));
            p.setEnd(json.getString("end"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return p;
    }

    public Period() {
    }

    public Period(int id, String start, String end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
