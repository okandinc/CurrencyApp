package com.okandinc.testcase.data.model;

public class ExchangeRate {

    private int Cur_ID;
    private String Date;
    private String Cur_Abbreviation;
    private int Cur_Scale;
    private String Cur_Name;
    private float Cur_OfficialRate;

    public int getCur_ID() {
        return Cur_ID;
    }

    public String getDate() {
        return Date;
    }

    public String getCur_Abbreviation() {
        return Cur_Abbreviation;
    }

    public int getCur_Scale() {
        return Cur_Scale;
    }

    public String getCur_Name() {
        return Cur_Name;
    }

    public float getCur_OfficialRate() {
        return Cur_OfficialRate;
    }
}
