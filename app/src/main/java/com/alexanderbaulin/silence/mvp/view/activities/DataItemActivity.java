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

package com.alexanderbaulin.silence.mvp.view.activities;


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

import com.alexanderbaulin.silence.silence.R;
import com.alexanderbaulin.silence.mvp.view.fragments.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DataItemActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.editText) EditText description;

    @BindView(R.id.btnTimeFrom) Button timeStartMode;
    @BindView(R.id.btnTimeTo) Button timeCancelMode;
    @BindView(R.id.btnSubmit) Button submit;

    @BindView(R.id.chkMon) CheckBox monday;
    @BindView(R.id.chkTue) CheckBox tuesday;
    @BindView(R.id.chkWed) CheckBox wednesday;
    @BindView(R.id.chkThu) CheckBox thursday;
    @BindView(R.id.chkFri) CheckBox friday;
    @BindView(R.id.chkSat) CheckBox saturday;
    @BindView(R.id.chkSun) CheckBox sunday;

    @BindView(R.id.radioGroup) RadioGroup radioGroup;

    com.alexanderbaulin.silence.mvp.model.DataItem dataItem;

    final String TAG_TIME_PICKER_START = "time start mode";
    final String TAG_TIME_PICKER_END = "time cancel mode";
    int updatedPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);

        ButterKnife.bind(this);

        dataItem = getDataFromIntent();
        if (dataItem == null)
            createNewItemAction();
        else
            updateItemAction();
        timeStartMode.setOnClickListener(this);
        timeCancelMode.setOnClickListener(this);
        submit.setOnClickListener(this);
        monday.setOnClickListener(this);
    }

    private void updateItemAction() {
        dataItem = getIntent().getParcelableExtra(com.alexanderbaulin.silence.mvp.model.DataItem.class.getCanonicalName());
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
        description.setText(dataItem.description);
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
        timeStartMode.setText(time);

        hour = dataItem.timeEnd[0];
        minute = dataItem.timeEnd[1];
        time = buildString(hour, minute);
        timeCancelMode.setText(time);
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
                    setResultDataItem();
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

    private void setResultDataItem() throws IllegalStateException {
        if (!isTimeStartSet()) throw new IllegalStateException(getStringResource(R.string.set_time_start_mode));
        if (!isTimeEndSet()) throw new IllegalStateException(getStringResource(R.string.set_time_cancel_mode));
        if (!isDaySet()) throw new IllegalStateException(getStringResource(R.string.no_day_selected));

        setCheckDays();
        setDescription();
        setVibration();

        Intent result = createIntent();
        setResult(RESULT_OK, result);
        finish();
    }

    private Intent createIntent() {
        Intent intent = new Intent();
        String className = com.alexanderbaulin.silence.mvp.model.DataItem.class.getCanonicalName();
        intent.putExtra(className, dataItem);
        if (updatedPosition != -1) intent.putExtra("updatedPosition", updatedPosition);
        return intent;
    }


    private void setVibration() {
        dataItem.isVibrationAllowed = (radioGroup.getCheckedRadioButtonId() == R.id.radVibration);
    }

    private void setDescription() {
        dataItem.description = description.getText().toString().trim();
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


    private com.alexanderbaulin.silence.mvp.model.DataItem getDataFromIntent() {
        return getIntent().getParcelableExtra(com.alexanderbaulin.silence.mvp.model.DataItem.class.getCanonicalName());
    }

    private void createNewItemAction() {
        dataItem = new com.alexanderbaulin.silence.mvp.model.DataItem();
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
        String text = timeCancelMode.getText().toString();
        String defaultText = getStringResource(R.string.time_until);
        return !text.equals(defaultText);
    }

    private boolean isTimeStartSet() {
        String text = timeStartMode.getText().toString();
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
            timeStartMode.setText(text);
            dataItem.timeBegin[0] = hourOfDay;
            dataItem.timeBegin[1] = minute;
        }
        if (findFragmentByTag(TAG_TIME_PICKER_END)) {
            timeCancelMode.setText(text);
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
