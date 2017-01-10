package com.globabt.dnla_poc;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by yamil.marques on 06/01/2017.
 */

public class DevicesRVAdapter extends RecyclerView.Adapter<DevicesRVAdapter.DevicesviewHolder>{

    private static CheckBox lastCheckbox;
    private ArrayList<DeviceDisplay> devicesList;
    private RecyclerViewListener listener;
    private Context context;

    public DevicesRVAdapter(Context context, ArrayList<DeviceDisplay> devices){
        this.context = context;
        this.devicesList = devices;
    }

    public void setListener(RecyclerViewListener listener){ this.listener = listener; }
    public void clear(){
        devicesList.clear();
        notifyDataSetChanged();
    }
    public void add(DeviceDisplay itemToAdd){
        devicesList.add(itemToAdd);
        notifyDataSetChanged();
    }
    public void add(int position, DeviceDisplay itemToAdd){
        devicesList.add(position, itemToAdd);
        notifyDataSetChanged();
    }
    public void addAll(ArrayList<DeviceDisplay> addList){
        devicesList.addAll(addList);
        notifyDataSetChanged();
    }
    public void remove(DeviceDisplay device){
        devicesList.remove(device);
        notifyDataSetChanged();
    }
    public boolean contains(DeviceDisplay device){
       return devicesList.contains(device);
    }
    public int indexOf(DeviceDisplay device){
        return devicesList.indexOf(device);
    }
    public DeviceDisplay getItemAt(int position){return devicesList.get(position);}

    @Override
    public DevicesviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item_layout,parent,false);
        return new DevicesviewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(DevicesviewHolder holder, int position) {
        final DeviceDisplay device = devicesList.get(position);
        holder.deviceName.setText(device.toString());
    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    protected static class DevicesviewHolder extends RecyclerView.ViewHolder {

        protected LinearLayout mainContainer;
        protected TextView deviceName;
        protected CheckBox checkBox;

        public DevicesviewHolder(final View v, final RecyclerViewListener rListener) {
            super(v);
            mainContainer = (LinearLayout) v.findViewById(R.id.container);
            deviceName = (TextView) v.findViewById(R.id.device_name);
            checkBox = (CheckBox) v.findViewById(R.id.checkbox_selected);

            if(rListener != null){
                final View.OnClickListener action =  new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rListener.recyclerViewOnItemClickListener(view, getLayoutPosition());
                    }
                };
                //mainContainer.setOnClickListener(action);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        checkboxBehavior(checkBox);
                        if(checked) action.onClick(v);
                    }
                });
            }
        }

        private void checkboxBehavior(CheckBox checkBox){
            if(lastCheckbox != null) lastCheckbox.setChecked(false);
            lastCheckbox = checkBox;
        }
    }
}
