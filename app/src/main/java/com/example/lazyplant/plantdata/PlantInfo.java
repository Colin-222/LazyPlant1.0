package com.example.lazyplant.plantdata;

import android.database.Cursor;

import com.example.lazyplant.R;

import java.util.ArrayList;
import java.util.List;

public class PlantInfo {
    public PlantInfo() {
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getCommon_name() {
        return common_name;
    }

    public void setCommon_name(String common_name) {
        this.common_name = common_name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getHeight_lower() {
        return height_lower;
    }

    public void setHeight_lower(String height_lower) {
        this.height_lower = height_lower;
    }

    public String getHeight_upper() {
        return height_upper;
    }

    public void setHeight_upper(String height_upper) {
        this.height_upper = height_upper;
    }

    public String getWidth_lower() {
        return width_lower;
    }

    public void setWidth_lower(String width_lower) {
        this.width_lower = width_lower;
    }

    public String getWidth_upper() {
        return width_upper;
    }

    public void setWidth_upper(String width_upper) {
        this.width_upper = width_upper;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOther_names() {
        return other_names;
    }

    public void setOther_names(String other_names) {
        this.other_names = other_names;
    }

    public String getFlower_color() {
        return flower_color;
    }

    public void setFlower_color(String flower_color) {
        this.flower_color = flower_color;
    }

    public String getFlowering_period() {
        return flowering_period;
    }

    public void setFlowering_period(String flowering_period) {
        this.flowering_period = flowering_period;
    }

    public String getFrost_tolerance() {
        return frost_tolerance;
    }

    public void setFrost_tolerance(String frost_tolerance) {
        this.frost_tolerance = frost_tolerance;
    }

    public String getLifespan() {
        return lifespan;
    }

    public void setLifespan(String lifespan) {
        this.lifespan = lifespan;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getWildlife() {
        return wildlife;
    }

    public void setWildlife(String wildlife) {
        this.wildlife = wildlife;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getSoil_type() {
        return soil_type;
    }

    public void setSoil_type(String soil_type) {
        this.soil_type = soil_type;
    }

    public String getSoil_pH() {
        return soil_pH;
    }

    public void setSoil_pH(String soil_pH) {
        this.soil_pH = soil_pH;
    }

    public String getSoil_moisture() {
        return soil_moisture;
    }

    public void setSoil_moisture(String soil_moisture) {
        this.soil_moisture = soil_moisture;
    }

    public List<String> getPlantDetailList(){
        List<String> l = new ArrayList<>();
        String x = this.getOther_names();
        if (!x.equals("")){ l.add("Other names: " + x); }
        x = this.getType();
        if (!x.equals("")){ l.add("Type: " + x); }
        x = this.getFlowering_period();
        if (!x.equals("")){ l.add("Flowering period: " + x); }
        x = this.getFrost_tolerance();
        if (!x.equals("")){ l.add("Frost tolerance: " + x); }
        x = this.getLight();
        if (!x.equals("")){ l.add("Shade required: " + x); }
        x = this.getHeight_lower();
        String x2 = this.getHeight_upper();
        if (x.equals("") && !x2.equals("")){
            l.add("Height: " + x2 + "m");
        } else if (!x.equals("") && x2.equals("")) {
            l.add("Height: " + x + "m");
        } else if (!x.equals("") && !x2.equals("")){
            l.add("Height: " + x + "-" + x2 + "m");
        }
        x = this.getWidth_lower();
        x2 = this.getWidth_upper();
        if (x.equals("") && !x2.equals("")){
            l.add("Height: " + x2 + "m");
        } else if (!x.equals("") && x2.equals("")) {
            l.add("Height: " + x + "m");
        } else if (!x.equals("") && !x2.equals("")){
            l.add("Height: " + x + "-" + x2 + "m");
        }
        x = this.getWildlife();
        if (!x.equals("")){ l.add("Wildlife Attracted: " + x); }
        return l;
    }

    private String scientific_name = "";
    private String common_name = "";
    private String family = "";
    private String height_lower = "";
    private String height_upper = "";
    private String width_lower = "";
    private String width_upper = "";

    private String type = "";
    private String other_names = "";
    private String flower_color = "";
    private String flowering_period = "";
    private String frost_tolerance = "";
    private String lifespan = "";
    private String light = "";
    private String wildlife = "";
    private String zone = "";
    private String soil_type = "";
    private String soil_pH = "";
    private String soil_moisture = "";

    
}
