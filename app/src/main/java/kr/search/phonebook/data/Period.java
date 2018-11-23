package kr.search.phonebook.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Period implements Serializable {
    private int id;
    private Calendar start;
    private Calendar end;

    public static Period getPeriodFromJson(JSONObject json) {
        Period p = new Period();

        try {
            p.setId(json.getInt("id"));

            SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar start = Calendar.getInstance();
            start.setTime(myDateFormat.parse(json.getString("start")));
            p.setStart(start);

            Calendar end = Calendar.getInstance();
            end.setTime(myDateFormat.parse(json.getString("end")));
            p.setStart(end);

//            p.setStart(json.getString("start"));
//            p.setEnd(json.getString("end"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return p;
    }

    public Period() {
    }

    public Period(int id, Calendar start, Calendar end) {
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

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }
}
