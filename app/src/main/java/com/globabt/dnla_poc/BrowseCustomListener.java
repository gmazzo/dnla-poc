package com.globabt.dnla_poc;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.DIDLContent;

/**
 * Created by yamil.marques on 12/01/2017.
 */

public interface BrowseCustomListener {
    void received(ActionInvocation actionInvocation, DIDLContent didl);
    void updateStatus(Browse.Status status);
    void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg);
}
