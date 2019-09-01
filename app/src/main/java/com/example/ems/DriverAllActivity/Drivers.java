package com.example.ems.DriverAllActivity;

public class Drivers {
    String dId;
    String dName;
    String dEmail;
    String dPhone;
    String dLicense;
    Drivers(){

    }

    public Drivers(String dId, String dName, String dEmail, String dPhone, String dLicense) {
        this.dId = dId;
        this.dName = dName;
        this.dEmail = dEmail;
        this.dPhone = dPhone;
        this.dLicense = dLicense;
    }

    public String getdId() {
        return dId;
    }

    public String getdName() {
        return dName;
    }

    public String getdEmail() {
        return dEmail;
    }

    public String getdPhone() {
        return dPhone;
    }

    public String getdLicense() {
        return dLicense;
    }
}
