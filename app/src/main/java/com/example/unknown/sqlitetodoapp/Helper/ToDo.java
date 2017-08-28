package com.example.unknown.sqlitetodoapp.Helper;

/**
 * Created by UNKNOWN on 7/2/2016.
 */
public class ToDo {

    private int id;
    private String note;
    private int Status;
    private String created_at;

    public ToDo() {

    }

    public ToDo(String note,int status){
        this.setNote(note);
        this.setStatus(status);
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
