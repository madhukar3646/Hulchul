package com.app.hulchul.model;

/**
 * Created by madhu on 4/12/2018.
 */

public class Country_model {

    public String country_code,country_name,namecode,flagpath;

    public String getFlagpath() {
        return flagpath;
    }

    public void setFlagpath(String flagpath) {
        this.flagpath = flagpath;
    }

    int countydrawable;

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public int getCountydrawable() {
        return countydrawable;
    }

    public void setCountydrawable(int countydrawable) {
        this.countydrawable = countydrawable;
    }


    public String getNamecode() {
        return namecode;
    }

    public void setNamecode(String namecode) {
        this.namecode = namecode;
    }
}
