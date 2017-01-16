package com.globabt.dnla_poc;

/**
 * Created by yamil.marques on 16/01/2017.
 */

public class BackSpecialItem implements BrowsableItemInterface {

    private String redirectID;

    public BackSpecialItem(String redirectID){
        this.redirectID = redirectID;
    }

    @Override
    public int getTotalCount() {
        return 0;
    }

    @Override
    public String getParentID() {
        return "";
    }

    @Override
    public String getID() {
        return redirectID;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getType() {
        return Constants.ITEM_TYPE_BACK;
    }
}
