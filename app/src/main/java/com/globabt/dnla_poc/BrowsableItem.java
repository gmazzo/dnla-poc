package com.globabt.dnla_poc;


import org.fourthline.cling.support.model.item.Item;

/**
 * Created by yamil.marques on 13/01/2017.
 */

public class BrowsableItem implements BrowsableItemInterface {

    protected Item item;

    public BrowsableItem(Item item){
        this.item = item;
    }

    @Override
    public int getTotalCount() {
        return 0;
    }

    @Override
    public String getParentID() {
        return item.getParentID();
    }

    @Override
    public String getID() {
        return item.getId();
    }

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public String getTitle() {
        return item.getTitle();
    }

    public Item getItem(){ return item; }
}
