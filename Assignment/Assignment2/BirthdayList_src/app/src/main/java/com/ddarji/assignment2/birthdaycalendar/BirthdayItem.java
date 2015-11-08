package com.ddarji.assignment2.birthdaycalendar;

import android.widget.CalendarView;

import java.util.Date;

public class BirthdayItem {
    private long id;
    private String name;
    private String birthday;

    public BirthdayItem(){
        id = -1;
        name = "";
        birthday = "";
    }

    public BirthdayItem(Long i, String n, String b){
        setId(i);
        setName(n);
        setBirthday(b);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return id + ", " + name + ": " + birthday;
    }

}
