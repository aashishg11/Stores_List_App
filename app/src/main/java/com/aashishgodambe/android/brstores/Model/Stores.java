package com.aashishgodambe.android.brstores.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Aashish on 1/15/2016.
 */
public class Stores implements Parcelable {

    private String address;
    private String city;
    private String name;
    private String latitude;
    private String zipcode;
    private String storeLogoURL;
    private String phone;
    private String longitude;
    private String storeID;
    private String state;

    public Stores(){

    }

    public Stores(Parcel input){
        address = input.readString();
        city = input.readString();
        name = input.readString();
        latitude = input.readString();
        zipcode = input.readString();
        storeLogoURL = input.readString();
        phone = input.readString();
        longitude = input.readString();
        storeID = input.readString();
        state = input.readString();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getStoreLogoURL() {
        return storeLogoURL;
    }

    public void setStoreLogoURL(String storeLogoURL) {
        this.storeLogoURL = storeLogoURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Stores{" +
                "address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", name='" + name + '\'' +
                ", latitude='" + latitude + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", storeLogoURL='" + storeLogoURL + '\'' +
                ", phone='" + phone + '\'' +
                ", longitude='" + longitude + '\'' +
                ", storeID='" + storeID + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("Stores", "writeToParcel invoked");

        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(name);
        dest.writeString(latitude);
        dest.writeString(zipcode);
        dest.writeString(storeLogoURL);
        dest.writeString(phone);
        dest.writeString(longitude);
        dest.writeString(storeID);
        dest.writeString(state);
    }

    public static final Parcelable.Creator<Stores> CREATOR
            = new Parcelable.Creator<Stores>() {
        public Stores createFromParcel(Parcel in) {
            return new Stores(in);
        }

        public Stores[] newArray(int size) {
            return new Stores[size];
        }
    };
}
