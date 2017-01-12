package com.globabt.dnla_poc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.fourthline.cling.support.model.container.Container;

import java.util.ArrayList;

/**
 * Created by yamil.marques on 11/01/2017.
 */

public class DirectoriesRVAdapter extends RecyclerView.Adapter<DirectoriesRVAdapter.DirectoriesViewHolder>{

    private ArrayList<Container> itemsList;
    private RecyclerViewListener listener;
    private Context context;

    public DirectoriesRVAdapter(Context context, ArrayList<Container> itemsList){
        this.context = context;
        this.itemsList = itemsList;
    }

    public void setListener(RecyclerViewListener listener){ this.listener = listener; }
    public void clear(){
        itemsList.clear();
        notifyDataSetChanged();
    }
    public void add(Container itemToAdd){
        itemsList.add(itemToAdd);
        notifyDataSetChanged();
    }
    public void add(int position, Container itemToAdd){
        itemsList.add(position, itemToAdd);
        notifyDataSetChanged();
    }
    public void addAll(ArrayList<Container> addList){
        itemsList.addAll(addList);
        notifyDataSetChanged();
    }
    public void remove(Container device){
        itemsList.remove(device);
        notifyDataSetChanged();
    }
    public boolean contains(Container item){
        return itemsList.contains(item);
    }
    public int indexOf(Container item){
        return itemsList.indexOf(item);
    }
    public Container getItemAt(int item){return itemsList.get(item);}


    @Override
    public DirectoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.browser_item_layout,parent,false);
        return new DirectoriesRVAdapter.DirectoriesViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(DirectoriesViewHolder holder, int position) {
        final Container item = itemsList.get(position);
        holder.itemName.setText(item.getTitle());
        int numDataInside = item.getChildCount() + item.getItems().size();
        if(numDataInside > 0){
            holder.itemsNumber.setText(String.valueOf(numDataInside));
            holder.itemsNumber.setVisibility(View.VISIBLE);
        }
        else {
            holder.itemsNumber.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    protected static class DirectoriesViewHolder extends RecyclerView.ViewHolder {

        protected LinearLayout mainContainer;
        protected TextView itemName;
        protected TextView itemsNumber;

        public DirectoriesViewHolder(final View v, final RecyclerViewListener rListener) {
            super(v);
            mainContainer = (LinearLayout) v.findViewById(R.id.container);
            itemName = (TextView) v.findViewById(R.id.device_name);
            itemsNumber = (TextView) v.findViewById(R.id.items_number);

            if(rListener != null){
                mainContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rListener.recyclerViewOnItemClickListener(view, getLayoutPosition());
                    }
                });
            }
        }
    }
}
