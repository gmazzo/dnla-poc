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
    public String getTitle() {
        return item.getTitle();
    }

    @Override
    public String getType() {
        return Constants.ITEM_TYPE_FILE;
    }

    public Item getItem(){ return item; }

    public String getResolution(){
        return item.getResources().get(0).getResolution();
    }
}
