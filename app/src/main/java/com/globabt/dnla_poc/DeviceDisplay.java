package com.globabt.dnla_poc;

import android.content.Context;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;

/**
 * Created by yamil.marques on 06/01/2017.
 */

public class DeviceDisplay {

    private Device device;
    private Context context;

    public DeviceDisplay(Context context, Device device) {
        this.device = device;
        this.context = context;
    }

    public Device getDevice() {
        return device;
    }

    public String getDetailsMessage() {
        StringBuilder sb = new StringBuilder();
        if (getDevice().isFullyHydrated()) {
            sb.append(getDevice().getDisplayString());
            sb.append("\n\n");
            for (Service service : getDevice().getServices()) {
                sb.append(service.getServiceType()).append("\n");
            }
        } else {
            sb.append(context.getString(R.string.deviceDetailsNotYetAvailable));
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDisplay that = (DeviceDisplay) o;
        return device.equals(that.device);
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }

    @Override
    public String toString() {
        String name = getDevice().getDetails() != null && getDevice().getDetails().getFriendlyName() != null ?
                getDevice().getDetails().getFriendlyName() : getDevice().getDisplayString();
        // Display a little star while the device is being loaded (see performance optimization earlier)
        return device.isFullyHydrated() ? name : name + " *";
    }
}
