package com.okandinc.testcase.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Currency implements Parcelable {

    private int Cur_ID;

    private int Cur_ParentID;

    private String Cur_Code;

    private String Cur_Abbreviation;

    private String Cur_Name;

    private String Cur_Name_Bel;

    private String Cur_Name_Eng;

    private String Cur_NameMulti;

    private String Cur_Name_BelMulti;

    private String Cur_Name_EngMulti;

    private int Cur_Scale;

    private int Cur_Periodicity;

    private String Cur_DateStart;

    private String Cur_DateEnd;

    public int getCur_ID() {
        return Cur_ID;
    }

    public int getCur_ParentID() {
        return Cur_ParentID;
    }

    public String getCur_Code() {
        return Cur_Code;
    }

    public String getCur_Abbreviation() {
        return Cur_Abbreviation;
    }

    public String getCur_Name() {
        return Cur_Name;
    }

    public String getCur_Name_Bel() {
        return Cur_Name_Bel;
    }

    public String getCur_Name_Eng() {
        return Cur_Name_Eng;
    }

    public String getCur_NameMulti() {
        return Cur_NameMulti;
    }

    public String getCur_Name_BelMulti() {
        return Cur_Name_BelMulti;
    }

    public String getCur_Name_EngMulti() {
        return Cur_Name_EngMulti;
    }

    public int getCur_Scale() {
        return Cur_Scale;
    }

    public int getCur_Periodicity() {
        return Cur_Periodicity;
    }

    public String getCur_DateStart() {
        return Cur_DateStart;
    }

    public String getCur_DateEnd() {
        return Cur_DateEnd;
    }

    protected Currency(Parcel in) {
        Cur_ID = in.readInt();
        Cur_ParentID = in.readInt();
        Cur_Code = in.readString();
        Cur_Abbreviation = in.readString();
        Cur_Name = in.readString();
        Cur_Name_Bel = in.readString();
        Cur_Name_Eng = in.readString();
        Cur_NameMulti = in.readString();
        Cur_Name_BelMulti = in.readString();
        Cur_Name_EngMulti = in.readString();
        Cur_Scale = in.readInt();
        Cur_Periodicity = in.readInt();
        Cur_DateStart = in.readString();
        Cur_DateEnd = in.readString();
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Cur_ID);
        parcel.writeInt(Cur_ParentID);
        parcel.writeString(Cur_Code);
        parcel.writeString(Cur_Abbreviation);
        parcel.writeString(Cur_Name);
        parcel.writeString(Cur_Name_Bel);
        parcel.writeString(Cur_Name_Eng);
        parcel.writeString(Cur_NameMulti);
        parcel.writeString(Cur_Name_BelMulti);
        parcel.writeString(Cur_Name_EngMulti);
        parcel.writeInt(Cur_Scale);
        parcel.writeInt(Cur_Periodicity);
        parcel.writeString(Cur_DateStart);
        parcel.writeString(Cur_DateEnd);
    }
}
