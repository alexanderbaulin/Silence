package com.example.alex.recycleviewmultitouchtutorial;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;


public class ItemActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    EditText editText;
    Data dataItem;
    Button timeFrom, timeUntil, submit;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    RadioGroup radioGroup;
    RadioButton noSound, vibrationAllowed;
    final String TAG_TIME_PICKER_FROM = "time from";
    final String TAG_TIME_PICKER_TO = "time to";
    int updatedPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);
        initViews();
        dataItem = getDataFromIntent();
        if(dataItem == null)
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
        if(dataItem.isVibrationAllowed)
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

        hour = dataItem.timeFrom[0];
        minute = dataItem.timeFrom[1];
        time = buildString(hour, minute);
        timeFrom.setText(time);

        hour = dataItem.timeUntil[0];
        minute = dataItem.timeUntil[1];
        time = buildString(hour, minute);
        timeUntil.setText(time);
    }

    @Override
    public void onClick(View v) {
        TimePickerFragment timePicker = new TimePickerFragment();
            switch (v.getId()) {
                case R.id.btnTimeFrom:
                    timePicker.show(getSupportFragmentManager(), TAG_TIME_PICKER_FROM);
                    break;
                case R.id.btnTimeTo:
                    timePicker.show(getSupportFragmentManager(), TAG_TIME_PICKER_TO);
                    break;
                case R.id.btnSubmit:
                    Toast.makeText(this, "submit", Toast.LENGTH_SHORT).show();
                    try {
                        returnResultDataItem();
                    } catch (IllegalStateException e) {
                        Log.d("submit", e.getMessage());
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
        if(!isTimeFromSet()) throw new IllegalStateException("Set time from");
        if(!isTimeUntilSet()) throw new IllegalStateException("Set time until");
        if(!isDaySet()) throw new IllegalStateException("No day selected");

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
        if(updatedPosition != -1) intent.putExtra("updatedPosition", updatedPosition);
        return intent;
    }


    private void setVibration() {
        dataItem.isVibrationAllowed = (radioGroup.getCheckedRadioButtonId() == R.id.radVibration);
        Log.d("submitButton", "vibration allowed = " + dataItem.isVibrationAllowed);
    }

    private void setDescription() {
        dataItem.description = editText.getText().toString().trim();
        Log.d("submitButton", "description = " + dataItem.description);
        Log.d("submitButton", "description = " + dataItem.description.equals(""));
    }

    private void setCheckDays() {
        dataItem.checkedDays[0] = monday.isChecked();
        dataItem.checkedDays[1] = tuesday.isChecked();
        dataItem.checkedDays[2] = wednesday.isChecked();
        dataItem.checkedDays[3] = thursday.isChecked();
        dataItem.checkedDays[4] = friday.isChecked();
        dataItem.checkedDays[5] = saturday.isChecked();
        dataItem.checkedDays[6] = sunday.isChecked();
        Log.d("submitButton", Arrays.toString(dataItem.checkedDays));
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

    private boolean isTimeUntilSet() {
        String text = timeUntil.getText().toString();
        String defaultText = getStringResource(R.string.time_until);
        return !text.equals(defaultText);
    }

    private boolean isTimeFromSet() {
        String text = timeFrom.getText().toString();
        String defaultText = getStringResource(R.string.time_from);
        return !text.equals(defaultText);
    }

    private String getStringResource(int resource) {
        return getApplicationContext().getString(resource);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String text = buildString(hourOfDay, minute);
        if(findFragmentByTag(TAG_TIME_PICKER_FROM)) {
            timeFrom.setText(text);
            dataItem.timeFrom[0] = hourOfDay;
            dataItem.timeFrom[1] = minute;
        }
        if(findFragmentByTag(TAG_TIME_PICKER_TO)) {
            timeUntil.setText(text);
            dataItem.timeUntil[0] = hourOfDay;
            dataItem.timeUntil[1] = minute;
        }
    }

    private String buildString(int hourOfDay, int minute) {
        StringBuilder builder = new StringBuilder();
        builder
                .append(hourOfDay)
                .append(":");
                if(minute < 10) builder.append("0");
                builder.append(minute);
        return builder.toString();
    }

    private boolean findFragmentByTag(String tag) {
       return getSupportFragmentManager().findFragmentByTag(tag) != null;
    }
}
