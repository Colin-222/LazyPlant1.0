package com.example.lazyplant.ui.share;

public class ShareData {
    private String plantId;
    private String postcode;

    public ShareData(String idInfo, String postcodeInfo){
        this.plantId = idInfo;
        this.postcode = postcodeInfo;
    }

    public String getPlantId() {
        return plantId;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
