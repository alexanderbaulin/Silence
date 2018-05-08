package com.example.alex.recycleviewmultitouchtutorial;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.myViewHolder> {
    private LayoutInflater inflater;
    private List<Information> data;
    private AppCompatActivity context;
    private boolean isMultiSelection;
    private OnLongClickListener itemLongClickListener;
    private OnItemClickListener itemClickListener;

    void setOnLongItemListener(OnLongClickListener listener) {
        itemLongClickListener = listener;
    }

    void setOnClickItemListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    void removeSelectedItems() {
        LinkedList<Integer> selectedItems = new LinkedList<>();
        for(int i = 0; i < data.size(); i++) {
            Information dataItem = data.get(i);
            if(dataItem.isSelected) {
                selectedItems.add(i);
            }
        }
        Collections.reverse(selectedItems);
        for(int i = 0; i < selectedItems.size(); i++) {
            int index = selectedItems.get(i);
            data.remove(index);
            notifyItemRemoved(index);
        }
    }

    interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    interface OnLongClickListener {
        void onItemLongClick(View itemView, int position);
    }

    RecycleAdapter(AppCompatActivity ctx, List<Information> informationList) {
        inflater = LayoutInflater.from(ctx);
        data = informationList;
        context = ctx;
    }

    @Override
    public myViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {
        final Information currentItem = data.get(position);
        holder.image.setImageResource(currentItem.image);
        holder.text.setText(currentItem.text);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMultiSelection) {
                    currentItem.isSelected = !currentItem.isSelected;
                    notifyItemChanged(holder.getAdapterPosition());
                    setItemBackgroundColor(currentItem, holder.itemView);
                    if(getSelectedItemsCount() == 0) {
                        setMultiSelection(false);
                    }
                }
                itemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                Log.d("myLogs", holder.getAdapterPosition() + " " + currentItem.isSelected);
            }
        });

        setItemBackgroundColor(currentItem, holder.itemView);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setMultiSelection(!isMultiSelection);
                if(isMultiSelection) {
                    data.get(holder.getAdapterPosition()).isSelected = true;
                    notifyItemChanged(holder.getAdapterPosition());
                }
                itemLongClickListener.onItemLongClick(holder.itemView, holder.getAdapterPosition());
                Log.d("myLogs", "onLongClick " + holder.getAdapterPosition() + " isMultiSelection " + isMultiSelection);
                return true;
            }
        });
        Log.d("myLogs", "onBind ViewHolder " + position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    int getSelectedItemsCount() {
        int result = 0;
        for(int i = 0; i < getItemCount(); i++) {
            Information dataItem = data.get(i);
            if(dataItem.isSelected) {
                result++;
            }
        }
        Log.d("myLogs", "selected items = " + result);
        return result;
    }

    boolean isMultiSelection() { return isMultiSelection; }

    private void setItemBackgroundColor(Information currentItem, View itemView) {
        if(currentItem.isSelected)
            itemView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        else
            itemView.setBackgroundResource(R.drawable.custom_background);

    }

    void setMultiSelection(boolean b) {
        isMultiSelection = b;
        if(!b) {
            clearSelection();
        }
    }

    private void clearSelection() {
        for(int i = 0; i < getItemCount(); i++) {
            Information dataItem = data.get(i);
            if(dataItem.isSelected) {
                dataItem.isSelected = false;
                notifyItemChanged(i);
            }
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        myViewHolder(final View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            text = itemView.findViewById(R.id.textView);
        }
    }
}


    /*
    private void clearSelection() {
        for(int i = 0; i < getItemCount(); i++) {
            if(isItemSelected(i)) {
                selectItem(i, false);
            }
        }
    }

    private void selectItem(int position, boolean isSelected) {
        data.get(position).isSelected = isSelected;
        notifyItemChanged(position);
    }

        private boolean isItemSelected(int position) {
        return data.get(position).isSelected;
    }
    */



