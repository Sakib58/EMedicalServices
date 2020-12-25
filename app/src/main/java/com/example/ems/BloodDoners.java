package com.example.ems;

public class BloodDoners {
    String bdName,bdPhone,bdLocation,bdBg,bdPass;

    public BloodDoners() {
    }

    public BloodDoners(String bdName, String bdPhone, String bdLocation, String bdBg, String bdPass) {
        this.bdName = bdName;
        this.bdPhone = bdPhone;
        this.bdLocation = bdLocation;
        this.bdBg = bdBg;
        this.bdPass = bdPass;
    }

    public String getBdName() {
        return bdName;
    }

    public void setBdName(String bdName) {
        this.bdName = bdName;
    }

    public String getBdPhone() {
        return bdPhone;
    }

    public void setBdPhone(String bdPhone) {
        this.bdPhone = bdPhone;
    }

    public String getBdLocation() {
        return bdLocation;
    }

    public void setBdLocation(String bdLocation) {
        this.bdLocation = bdLocation;
    }

    public String getBdBg() {
        return bdBg;
    }

    public void setBdBg(String bdBg) {
        this.bdBg = bdBg;
    }

    public String getBdPass() {
        return bdPass;
    }

    public void setBdPass(String bdPass) {
        this.bdPass = bdPass;
    }
}
