package com.example.alex.recycleviewmultitouchtutorial;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.myViewHolder> {
    private LayoutInflater inflater;
    private List<Data> data;
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
        LinkedList<Integer> selectedPositions = new LinkedList<>();
        for(int position = 0; position < data.size(); position++) {
            Data dataItem = data.get(position);
            if(dataItem.isSelected) {
                selectedPositions.add(position);
            }
        }
        Collections.reverse(selectedPositions);
        for(int position = 0; position < selectedPositions.size(); position++) {
            int selectedPosition = selectedPositions.get(position);
            data.remove(selectedPosition);
            notifyItemRemoved(selectedPosition);
        }
    }

    interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    interface OnLongClickListener {
        void onItemLongClick(View itemView, int position);
    }

    RecycleAdapter(AppCompatActivity ctx, List<Data> dataList) {
        inflater = LayoutInflater.from(ctx);
        data = dataList;
        context = ctx;
    }

    @Override
    public myViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row2, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {
        final Data currentItem = data.get(position);
        final Switch switcher = holder.switcher;
        final View itemView = holder.itemView;
        holder.time.setText(currentItem.time);
        holder.days.setText(currentItem.days);
        switcher.setChecked(currentItem.isChecked);
        setSwitcherVisibility(switcher);
        setSwitcherListener(switcher, holder.getAdapterPosition());
        setItemBackground(currentItem, itemView);
        setOnItemClickListener(itemView, holder.getAdapterPosition(), currentItem);
        setOnLongClickListener(itemView, holder.getAdapterPosition());
        Log.d("myLogs", "onBind ViewHolder " + position);
    }

    private void setSwitcherVisibility(Switch switcher) {
        if(isMultiSelection) switcher.setVisibility(View.INVISIBLE);
        else switcher.setVisibility(View.VISIBLE);
    }

    private void setSwitcherListener(Switch switcher, final int position) {
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("myLogs", "onChecked " + isChecked + " position " + position);
                if(!isMultiSelection) data.get(position).isChecked = isChecked;
            }
        });
    }

    private void setOnItemClickListener(final View itemView, final int position, final Data currentItem) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMultiSelection) {
                    currentItem.isSelected = !currentItem.isSelected;
                    notifyItemChanged(position);
                    setItemBackground(currentItem, itemView);
                }
                itemClickListener.onItemClick(itemView, position);
                Log.d("myLogs", position + " " + currentItem.isSelected);
            }
        });
    }

    private void setOnLongClickListener(final View itemView, final int position) {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setMultiSelection(!isMultiSelection);
                if(isMultiSelection) {
                    data.get(position).isSelected = true;
                    notifyItemChanged(position);
                }
                itemLongClickListener.onItemLongClick(itemView, position);
                Log.d("myLogs", "onLongClick " + position + " isMultiSelection " + isMultiSelection);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    int getSelectedItemsCount() {
        int result = 0;
        for(int position = 0; position < getItemCount(); position++) {
            Data dataItem = data.get(position);
            if(dataItem.isSelected) {
                result++;
            }
        }
        Log.d("myLogs", "selected items = " + result);
        return result;
    }

    boolean isMultiSelection() { return isMultiSelection; }

    private void setItemBackground(Data currentItem, View itemView) {
        if(currentItem.isSelected)
            itemView.setBackgroundColor(context.getResources().getColor(R.color.colorItemBackground));
        else
            itemView.setBackgroundResource(R.drawable.custom_background);
    }

    void setMultiSelection(boolean b) {
        isMultiSelection = b;
        if(!isMultiSelection) {
            clearSelection();
        }
    }

    private void clearSelection() {
        for(int i = 0; i < getItemCount(); i++) {
            Data dataItem = data.get(i);
            if(dataItem.isSelected) {
                dataItem.isSelected = false;
                notifyItemChanged(i);
            }
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView days;
        Switch switcher;
        myViewHolder(final View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.txtTimePeriod);
            days = itemView.findViewById(R.id.txtDaysOfWeek);
            switcher = itemView.findViewById(R.id.btnSwitch);
        }
    }
}


