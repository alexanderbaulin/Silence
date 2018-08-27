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

package com.example.alex.silence.activities;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.alex.silence.Data;
import com.example.alex.silence.R;
import com.example.alex.silence.fragments.TimePicker;


public class DataItem extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    EditText editText;
    Data dataItem;
    Button timeFrom, timeUntil, submit;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    RadioGroup radioGroup;
    RadioButton noSound, vibrationAllowed;
    final String TAG_TIME_PICKER_START = "time from";
    final String TAG_TIME_PICKER_END = "time to";
    int updatedPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);
        initViews();
        dataItem = getDataFromIntent();
        if (dataItem == null)
            createNewItemAction();
        else
            updateItemAction();
        timeFrom.setOnClickListener(this);
        timeUntil.setOnClickListener(this);
        submit.setOnClickListener(this);
        monday.setOnClickListener(this);
    }

    private void updateItemAction() {
        dataItem = getIntent().getParcelableExtra(Data.class.getCanonicalName());
        updatedPosition = getIntent().getIntExtra("updatedPosition", -1);
        updateUI();
    }

    private void updateUI() {
        updateTextEditField();
        updateCheckedDays();
        updateTimeButtons();
        updateRadioGroupButtons();
    }

    private void updateTextEditField() {
        editText.setText(dataItem.description);
    }

    private void updateRadioGroupButtons() {
        if (dataItem.isVibrationAllowed)
            radioGroup.check(R.id.radVibration);
        else
            radioGroup.check(R.id.radMute);
    }

    private void updateCheckedDays() {
        monday.setChecked(dataItem.checkedDays[0]);
        tuesday.setChecked(dataItem.checkedDays[1]);
        wednesday.setChecked(dataItem.checkedDays[2]);
        thursday.setChecked(dataItem.checkedDays[3]);
        friday.setChecked(dataItem.checkedDays[4]);
        saturday.setChecked(dataItem.checkedDays[5]);
        sunday.setChecked(dataItem.checkedDays[6]);
    }

    private void updateTimeButtons() {
        int hour, minute;
        String time;

        hour = dataItem.timeBegin[0];
        minute = dataItem.timeBegin[1];
        time = buildString(hour, minute);
        timeFrom.setText(time);

        hour = dataItem.timeEnd[0];
        minute = dataItem.timeEnd[1];
        time = buildString(hour, minute);
        timeUntil.setText(time);
    }

    @Override
    public void onClick(View v) {
        TimePicker timePicker = new TimePicker();
        switch (v.getId()) {
            case R.id.btnTimeFrom:
                timePicker.show(getSupportFragmentManager(), TAG_TIME_PICKER_START);
                break;
            case R.id.btnTimeTo:
                timePicker.show(getSupportFragmentManager(), TAG_TIME_PICKER_END);
                break;
            case R.id.btnSubmit:
                try {
                    returnResultDataItem();
                } catch (IllegalStateException e) {
                    showAlertDialog(e.getMessage());
                }
                break;
        }
    }

    private void showAlertDialog(String s) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(s);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    private void returnResultDataItem() throws IllegalStateException {
        if (!isTimeStartSet()) throw new IllegalStateException("Set time from");
        if (!isTimeEndSet()) throw new IllegalStateException("Set time until");
        if (!isDaySet()) throw new IllegalStateException("No day selected");

        setCheckDays();
        setDescription();
        setVibration();

        Intent result = createIntent();
        setResult(RESULT_OK, result);
        finish();
    }

    private Intent createIntent() {
        Intent intent = new Intent();
        String className = Data.class.getCanonicalName();
        intent.putExtra(className, dataItem);
        if (updatedPosition != -1) intent.putExtra("updatedPosition", updatedPosition);
        return intent;
    }


    private void setVibration() {
        dataItem.isVibrationAllowed = (radioGroup.getCheckedRadioButtonId() == R.id.radVibration);
    }

    private void setDescription() {
        dataItem.description = editText.getText().toString().trim();
    }

    private void setCheckDays() {
        dataItem.checkedDays[0] = monday.isChecked();
        dataItem.checkedDays[1] = tuesday.isChecked();
        dataItem.checkedDays[2] = wednesday.isChecked();
        dataItem.checkedDays[3] = thursday.isChecked();
        dataItem.checkedDays[4] = friday.isChecked();
        dataItem.checkedDays[5] = saturday.isChecked();
        dataItem.checkedDays[6] = sunday.isChecked();
    }

    private void initViews() {
        editText = findViewById(R.id.editText);

        timeFrom = findViewById(R.id.btnTimeFrom);
        timeUntil = findViewById(R.id.btnTimeTo);
        submit = findViewById(R.id.btnSubmit);

        monday = findViewById(R.id.chkMon);
        tuesday = findViewById(R.id.chkTue);
        wednesday = findViewById(R.id.chkWed);
        thursday = findViewById(R.id.chkThu);
        friday = findViewById(R.id.chkFri);
        saturday = findViewById(R.id.chkSat);
        sunday = findViewById(R.id.chkSun);

        radioGroup = findViewById(R.id.radioGroup);
        noSound = findViewById(R.id.radMute);
        vibrationAllowed = findViewById(R.id.radVibration);
    }

    private Data getDataFromIntent() {
        return getIntent().getParcelableExtra(Data.class.getCanonicalName());
    }

    private void createNewItemAction() {
        dataItem = new Data();
    }

    private boolean isDaySet() {
        return
                monday.isChecked() ||
                        tuesday.isChecked() ||
                        wednesday.isChecked() ||
                        thursday.isChecked() ||
                        friday.isChecked() ||
                        saturday.isChecked() ||
                        sunday.isChecked();
    }

    private boolean isTimeEndSet() {
        String text = timeUntil.getText().toString();
        String defaultText = getStringResource(R.string.time_until);
        return !text.equals(defaultText);
    }

    private boolean isTimeStartSet() {
        String text = timeFrom.getText().toString();
        String defaultText = getStringResource(R.string.time_from);
        return !text.equals(defaultText);
    }

    private String getStringResource(int resource) {
        return getApplicationContext().getString(resource);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        String text = buildString(hourOfDay, minute);
        if (findFragmentByTag(TAG_TIME_PICKER_START)) {
            timeFrom.setText(text);
            dataItem.timeBegin[0] = hourOfDay;
            dataItem.timeBegin[1] = minute;
        }
        if (findFragmentByTag(TAG_TIME_PICKER_END)) {
            timeUntil.setText(text);
            dataItem.timeEnd[0] = hourOfDay;
            dataItem.timeEnd[1] = minute;
        }
    }

    private String buildString(int hourOfDay, int minute) {
        StringBuilder builder = new StringBuilder();
        builder
                .append(hourOfDay)
                .append(":");
        if (minute < 10) builder.append("0");
        builder.append(minute);
        return builder.toString();
    }

    private boolean findFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag) != null;
    }
}
