/*
* Silence for Android OS
* Copyright 2018 Alexander Baulin
* Contacts: alexander.baulin.github@yandex.ru
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.alexanderbaulin.silence.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alexanderbaulin.silence.Alarm;
import com.alexanderbaulin.silence.Data;
import com.alexanderbaulin.silence.MyApp;
import com.alexanderbaulin.silence.silence.R;
import com.alexanderbaulin.silence.database.Base;

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
        for (int position = 0; position < data.size(); position++) {
            Data dataItem = data.get(position);
            if (dataItem.isSelected) {
                selectedPositions.add(position);
            }
        }
        Collections.reverse(selectedPositions);
        for (int position = 0; position < selectedPositions.size(); position++) {
            int selectedPosition = selectedPositions.get(position);
            Base db = new Base(context);
            Data deletedItem = data.remove(selectedPosition);
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
    }

    private void setImageView(ImageView image, Data currentItem) {
        if (currentItem.isVibrationAllowed)
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
        if (minute < 10) builder.append("0");
        builder.append(minute);
    }

    private String parseDaysText(boolean[] daysOfWeek) {
        StringBuilder builder = new StringBuilder();
        if (isAllDaysChecked(daysOfWeek)) return builder.append("every day").toString();
        if (daysOfWeek[0]) {
            builder.append(context.getString(R.string.day_monday));
            builder.append(" ");
        }
        if (daysOfWeek[1]) {
            builder.append(context.getString(R.string.day_tuesday));
            builder.append(" ");
        }
        if (daysOfWeek[2]) {
            builder.append(context.getString(R.string.day_wednesday));
            builder.append(" ");
        }
        if (daysOfWeek[3]) {
            builder.append(context.getString(R.string.day_thursday));
            builder.append(" ");
        }
        if (daysOfWeek[4]) {
            builder.append(context.getString(R.string.day_friday));
            builder.append(" ");
        }
        if (daysOfWeek[5]) {
            builder.append(context.getString(R.string.day_saturday));
            builder.append(" ");
        }
        if (daysOfWeek[6]) {
            builder.append(context.getString(R.string.day_sunday));
            builder.append(" ");
        }
        return builder.toString();
    }

    private boolean isAllDaysChecked(boolean[] checkedDay) {
        boolean result = true;
        for (boolean isDayChecked : checkedDay) {
            if (!isDayChecked) result = false;
        }
        return result;
    }

    private void setSwitcherVisibility(Switch switcher) {
        if (isMultiSelection) switcher.setVisibility(View.INVISIBLE);
        else switcher.setVisibility(View.VISIBLE);
    }

    private void setSwitcherListener(Switch switcher, final Data currentItem) {
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMultiSelection) {
                    currentItem.isAlarmOn = !currentItem.isAlarmOn;
                    Alarm alarm = new Alarm();
                    if (currentItem.isAlarmOn)
                        alarm.setAlarm(currentItem, data.lastIndexOf(currentItem));
                    else alarm.cancel(currentItem, data.lastIndexOf(currentItem));
                    db.update(currentItem.id, currentItem);

                }
            }
        });
    }

    private void setOnItemClickListener(final View itemView, final int position, final Data currentItem) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMultiSelection) {
                    currentItem.isSelected = !currentItem.isSelected;
                    notifyItemChanged(position);
                    setItemBackground(currentItem, itemView);
                }
                itemClickListener.onItemClick(itemView, position);
            }
        });
    }

    private void setOnLongClickListener(final View itemView, final int position) {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setMultiSelection(!isMultiSelection);
                if (isMultiSelection) {
                    data.get(position).isSelected = true;
                    notifyItemChanged(position);
                }
                itemLongClickListener.onItemLongClick(itemView, position);
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
        for (int position = 0; position < getItemCount(); position++) {
            Data dataItem = data.get(position);
            if (dataItem.isSelected) {
                result++;
            }
        }
        return result;
    }

    public boolean isMultiSelection() {
        return isMultiSelection;
    }

    private void setItemBackground(Data currentItem, View itemView) {
        if (currentItem.isSelected)
            itemView.setBackgroundColor(ContextCompat.getColor(MyApp.getAppContext(), R.color.colorItemBackground));
        else
            itemView.setBackgroundResource(R.drawable.custom_background);
    }

    public void setMultiSelection(boolean b) {
        isMultiSelection = b;
        if (!isMultiSelection) {
            clearSelection();
        }
    }

    private void clearSelection() {
        for (int i = 0; i < getItemCount(); i++) {
            Data dataItem = data.get(i);
            if (dataItem.isSelected) {
                dataItem.isSelected = false;
                notifyItemChanged(i);
            }
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView time;
        TextView days;
        Switch switcher;

        myViewHolder(final View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgView);
            time = itemView.findViewById(R.id.txtTimePeriod);
            days = itemView.findViewById(R.id.txtDaysOfWeek);
            switcher = itemView.findViewById(R.id.btnSwitch);
        }
    }
}


