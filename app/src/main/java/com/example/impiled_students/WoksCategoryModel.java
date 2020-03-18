package com.example.impiled_students;

public class WoksCategoryModel {
    private String journ_name, journ_id;

    public String getJourn_id() {
        return journ_id;
    }

    public void setJourn_id(String journ_id) {
        this.journ_id = journ_id;
    }

    public String getJourn_name() {
        return journ_name;
    }

    public void setJourn_name(String journ_name) {
        this.journ_name = journ_name;
    }

    public WoksCategoryModel(){}

    public WoksCategoryModel(String journ_id, String journ_name) {
        this.journ_id = journ_id;
        this.journ_name = journ_name;
    }

    @Override
    public String toString(){
        return journ_name;
    }



}
