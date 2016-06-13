package com.example.ysm0622.app_when.object;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {

    // TAG
    private static final String TAG = Group.class.getName();

    // Variables
    private int Id;
    private String Title;
    private String Desc;
    private int MasterId;

    private User Master;
    private ArrayList<User> Member;

    public Group() {
        Member = new ArrayList<>();
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public User getMaster() {
        return Master;
    }

    public void setMaster(User master) {
        Master = master;
    }

    public ArrayList<User> getMember() {
        return Member;
    }

    public User getMember(int i) {
        return Member.get(i);
    }

    public void setMember(ArrayList<User> member) {
        Member = member;
    }

    public int getMemberNum() {
        return Member.size();
    }

    public void addMember(User user) {
        Member.add(user);
    }

    public long getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getMasterId() {
        return MasterId;
    }

    public void setMasterId(int masterId) {
        MasterId = masterId;
    }
}
