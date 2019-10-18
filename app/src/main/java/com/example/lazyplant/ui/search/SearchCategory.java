package com.example.lazyplant.ui.search;

/**
 * A class used to store information on each possible search category.
 */
public class SearchCategory {
    private String desc;
    private String term;
    private String type;
    private int image_resource;

    public SearchCategory(String desc, String term, String type, int image_resource) {
        this.desc = desc;
        this.term = term;
        this.type = type;
        this.image_resource = image_resource;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getImage_resource() {
        return image_resource;
    }

    public void setImage_resource(int image_resource) {
        this.image_resource = image_resource;
    }
}