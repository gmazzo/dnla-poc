package com.globabt.dnla_poc;

/**
 * Created by yamil.marques on 13/01/2017.
 */

public interface BrowsableItemInterface {
    int getTotalCount();
    String getParentID();
    String getID();
    boolean isContainer();
    String getTitle();
}