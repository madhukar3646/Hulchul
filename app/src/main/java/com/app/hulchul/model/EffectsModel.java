package com.app.hulchul.model;

public class EffectsModel {

    CharSequence filtername;
    boolean isSelected=false;

    public CharSequence getFiltername() {
        return filtername;
    }

    public void setFiltername(CharSequence filtername) {
        this.filtername = filtername;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
