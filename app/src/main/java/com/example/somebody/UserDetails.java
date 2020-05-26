package com.example.somebody;

import java.io.Serializable;

public class UserDetails implements Serializable {
    String Name;
    String MobNo;
    String Email;
    String City;
    String State;
    String ProfPic;

    public UserDetails() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobNo() {
        return MobNo;
    }

    public void setMobNo(String mobNo) {
        MobNo = mobNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getProfPic() {
        return ProfPic;
    }

    public void setProfPic(String profPic) {
        ProfPic = profPic;
    }
}
