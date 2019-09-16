package com.example.lazyplant;



public class StoreLocationData {

    private String storeName;
    private String storeAddress;
    private Double storeLatitude;
    private Double storeLongitude;

    public StoreLocationData(String name, String address, Double latitude, Double longitude) {
        this.storeName = name;
        this.storeAddress = address;
        this.storeLatitude = latitude;
        this.storeLongitude = longitude;
    }

    public Double getStoreLatitude() {
        return storeLatitude;
    }

    public Double getStoreLongitude() {
        return storeLongitude;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public void setStoreLatitude(Double storeLatitude) {
        this.storeLatitude = storeLatitude;
    }

    public void setStoreLongitude(Double storeLongitude) {
        this.storeLongitude = storeLongitude;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}

