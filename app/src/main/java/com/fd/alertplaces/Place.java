package com.fd.alertplaces;

/**
 * Created by frog on 05/01/15.
 */
public class Place {


    public Place() { }

    private int id;
    private String name;
    private String type;
    private double longitude;
    private double latitude;
    private String comment;
    private String referComment;
    private String openingHours;
    private String averagePrice;
    private String address;
    private String city;
    private String province;

    public Place(int id, String name, String type, double longitude, double latitude, String comment, String referComment, String openingHours, String averagePrice, String address, String city, String province) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
        this.comment = comment;
        this.referComment = referComment;
        this.openingHours = openingHours;
        this.averagePrice = averagePrice;
        this.address = address;
        this.city = city;
        this.province = province;
    }

    public String getProvince() {

        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReferComment() {
        return referComment;
    }

    public void setReferComment(String referComment) {
        this.referComment = referComment;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
