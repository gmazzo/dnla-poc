package com.globabt.dnla_poc;

import org.fourthline.cling.support.model.container.Container;

/**
 * Created by yamil.marques on 13/01/2017.
 */

public class BrowsableDirectory implements BrowsableItemInterface {

    private Container container;

    public BrowsableDirectory(Container container){
        this.container = container;
    }

    @Override
    public int getTotalCount() {
        return container.getChildCount() + container.getItems().size();
    }

    @Override
    public String getParentID() {
        return container.getParentID();
    }

    @Override
    public String getID() {
        return container.getId();
    }

    @Override
    public String getTitle() {
        return container.getTitle();
    }

    @Override
    public String getType() {
        return Constants.ITEM_TYPE_DIRECTORY;
    }

    public Container getContainer(){ return container; }
}
