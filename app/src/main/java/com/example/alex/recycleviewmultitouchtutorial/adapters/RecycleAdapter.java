package com.example.alex.recycleviewmultitouchtutorial.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.alex.recycleviewmultitouchtutorial.Alarm;
import com.example.alex.recycleviewmultitouchtutorial.Data;
import com.example.alex.recycleviewmultitouchtutorial.R;
import com.example.alex.recycleviewmultitouchtutorial.database.Base;

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
    private Base db;

    public RecycleAdapter(AppCompatActivity ctx, List<Data> dataList) {
        inflater = LayoutInflater.from(ctx);
        data = dataList;
        context = ctx;
        db = new Base(context);
    }

    public void setOnLongItemListener(OnLongClickListener listener) {
        itemLongClickListener = listener;
    }

    public void setOnClickItemListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public void removeSelectedItems() {
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
            Base db = new Base(context);
            Data deletedItem = data.remove(selectedPosition);
            Log.d("myLogs1", "removed id = " + deletedItem.id);
            db.delete(deletedItem.id);
            notifyItemRemoved(selectedPosition);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface OnLongClickListener {
        void onItemLongClick(View itemView, int position);
    }

    @Override
    public myViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_data, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {
        final Data currentItem = data.get(position);
        final Switch switcher = holder.switcher;
        final View itemView = holder.itemView;
        final ImageView image = holder.image;
        setImageView(image, currentItem);
        holder.description.setText(currentItem.description);
        String time = parseTimeText(currentItem.timeBegin, currentItem.timeEnd);
        holder.time.setText(time);
        String days = parseDaysText(currentItem.checkedDays);
        holder.days.setText(days);
        switcher.setChecked(currentItem.isAlarmOn);
        setSwitcherVisibility(switcher);
        setSwitcherListener(switcher, currentItem);
        setItemBackground(currentItem, itemView);
        setOnItemClickListener(itemView, holder.getAdapterPosition(), currentItem);
        setOnLongClickListener(itemView, holder.getAdapterPosition());
        Log.d("myLogs", "onBind ViewHolder " + position);
    }

    private void setImageView(ImageView image, Data currentItem) {
       if(currentItem.isVibrationAllowed)
           image.setImageResource(R.drawable.ic_baseline_vibration_48px);
       else
           image.setImageResource(R.drawable.do_not_disturb);
    }

    private String parseTimeText(int[] timeFrom, int[] timeUntil) {
        StringBuilder builder = new StringBuilder();
        parseTime(builder, timeFrom);
        builder.append(" - ");
        parseTime(builder, timeUntil);
        return builder.toString();
    }

    private void parseTime(StringBuilder builder, int[] time) {
        int hour = time[0];
        int minute = time[1];
        builder.append(hour)
                .append(":");
        if(minute < 10) builder.append("0");
        builder.append(minute);
    }

    private String parseDaysText(boolean[] daysOfWeek) {
        StringBuilder builder = new StringBuilder();
        if(isAllDaysChecked(daysOfWeek)) return builder.append("every day").toString();
        if(daysOfWeek[0]) {
            builder.append(context.getString(R.string.day_monday));
            builder.append(" ");
        }
        if(daysOfWeek[1]) {
            builder.append(context.getString(R.string.day_tuesday));
            builder.append(" ");
        }
        if(daysOfWeek[2]) {
            builder.append(context.getString(R.string.day_wednesday));
            builder.append(" ");
        }
        if(daysOfWeek[3]) {
            builder.append(context.getString(R.string.day_thursday));
            builder.append(" ");
        }
        if(daysOfWeek[4]) {
            builder.append(context.getString(R.string.day_friday));
            builder.append(" ");
        }
        if(daysOfWeek[5]) {
            builder.append(context.getString(R.string.day_saturday));
            builder.append(" ");
        }
        if(daysOfWeek[6]) {
            builder.append(context.getString(R.string.day_sunday));
            builder.append(" ");
        }
        return builder.toString();
    }

    private boolean isAllDaysChecked(boolean[] checkedDay) {
        boolean result = true;
        for(boolean isDayChecked: checkedDay) {
            if(!isDayChecked) result = false;
        }
        Log.d("myLogs", "result " + result);
        return result;
    }

    private void setSwitcherVisibility(Switch switcher) {
        if(isMultiSelection) switcher.setVisibility(View.INVISIBLE);
        else switcher.setVisibility(View.VISIBLE);
    }

    private void setSwitcherListener(Switch switcher, final Data currentItem) {
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMultiSelection) {
                    currentItem.isAlarmOn = !currentItem.isAlarmOn;
                    Alarm alarm = new Alarm();
                    if(currentItem.isAlarmOn) alarm.setAlarm(currentItem, data.lastIndexOf(currentItem));
                    else alarm.cancel(currentItem, data.lastIndexOf(currentItem));
                    db.update(currentItem.id, currentItem);
                }
                Log.d("myLogs1", "onChecked " + " position " + currentItem.isAlarmOn);
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

    public int getSelectedItemsCount() {
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

    public boolean isMultiSelection() { return isMultiSelection; }

    private void setItemBackground(Data currentItem, View itemView) {
        if(currentItem.isSelected)
            itemView.setBackgroundColor(context.getResources().getColor(R.color.colorItemBackground));
        else
            itemView.setBackgroundResource(R.drawable.custom_background);
    }

    public void setMultiSelection(boolean b) {
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
        TextView description;
        ImageView image;
        TextView time;
        TextView days;
        Switch switcher;
        myViewHolder(final View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.txtDescription);
            image = itemView.findViewById(R.id.imgView);
            time = itemView.findViewById(R.id.txtTimePeriod);
            days = itemView.findViewById(R.id.txtDaysOfWeek);
            switcher = itemView.findViewById(R.id.btnSwitch);
        }
    }
}


