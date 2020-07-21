package com.example.model.shas_masechtot_list_models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SederItem {

    @SerializedName("name")
    private String name;

    @SerializedName("masechtot")
    private List<MasechetItem> masechtot;

    private boolean isOpen = false;

    public SederItem(String name, List<MasechetItem> masechet) {
        this.name = name;
        this.masechtot = masechet;
    }

    public boolean isOpen() { return isOpen; }

    public void setOpen(boolean open) { isOpen = open; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MasechetItem> getMasechtot() {
        return masechtot;
    }

    public void setMasechtot(List<MasechetItem> masechet) {
        this.masechtot = masechet;
    }
}
