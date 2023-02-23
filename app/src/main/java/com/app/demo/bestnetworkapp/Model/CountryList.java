package com.app.demo.bestnetworkapp.Model;

public class CountryList {

    public String name;
    public String code;
    public String cuntryimages;

    public CountryList(String name, String code, String cuntryimages) {
        this.name =name;
        this.code =code;
        this.cuntryimages =cuntryimages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCuntryimages() {
        return cuntryimages;
    }

    public void setCuntryimages(String cuntryimages) {
        this.cuntryimages = cuntryimages;
    }
}
