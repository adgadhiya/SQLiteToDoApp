package com.example.unknown.sqlitetodoapp.Helper;

/**
 * Created by UNKNOWN on 7/2/2016.
 */
public class Tag {

    private String tag;
    private int id;
    private String created_at;

    public Tag(){

    }

    public Tag(String tag){
        this.setTag(tag);
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
