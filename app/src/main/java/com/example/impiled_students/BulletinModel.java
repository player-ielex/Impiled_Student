package com.example.impiled_students;

import androidx.fragment.app.Fragment;

public class BulletinModel extends Fragment {
    String announ_date, announ_name, announ_what;

    public BulletinModel(){}

    public String getAnnoun_date() {
        return announ_date;
    }

    public void setAnnoun_date(String announ_date) {
        this.announ_date = announ_date;
    }

    public String getAnnoun_name() {
        return announ_name;
    }

    public void setAnnoun_name(String announ_name) {
        this.announ_name = announ_name;
    }

    public String getAnnoun_what() {
        return announ_what;
    }

    public void setAnnoun_what(String announ_what) {
        this.announ_what = announ_what;
    }

}
