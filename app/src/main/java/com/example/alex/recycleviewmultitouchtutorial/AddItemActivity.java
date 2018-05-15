package com.example.alex.recycleviewmultitouchtutorial;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by Alex on 08.05.2018.
 */

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    EditText editText;
    Information data;
    Button timeFrom, timeTo, submit;
    final String TAG_TIME_FROM = "time from";
    final String TAG_TIME_TO = "time to";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_activity);
        editText = findViewById(R.id.editText);
        data = getIntent().getParcelableExtra(Information.class.getCanonicalName());
        submit = findViewById(R.id.btnSubmit);
        timeFrom = findViewById(R.id.btnTimeFrom);
        timeTo = findViewById(R.id.btnTimeTo);
        timeFrom.setOnClickListener(this);
        timeTo.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TimePickerFragment timePicker = new TimePickerFragment();
            switch (v.getId()) {
                case R.id.btnTimeFrom:
                    timePicker.show(getSupportFragmentManager(), TAG_TIME_FROM);
                   // Toast.makeText(this, "TimeFrom", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnTimeTo:
                    timePicker.show(getSupportFragmentManager(), TAG_TIME_TO);
                   // Toast.makeText(this, "TimeTo", Toast.LENGTH_SHORT).show();
                    break;

            }

        /*
        Intent intent = new Intent();
        String className = Information.class.getCanonicalName();
        Information newItem = new Information(R.drawable.ic_launcher_background, editText.getText().toString());
        intent.putExtra(className, newItem);
        setResult(RESULT_OK, intent);
        finish();
        */
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_TIME_FROM);
        StringBuilder builder = new StringBuilder();
        builder.append(hourOfDay).append(":")
                .append(minute);
        String text= builder.toString();
        if(getSupportFragmentManager().findFragmentByTag(TAG_TIME_FROM) != null) timeFrom.setText(text);
        if(getSupportFragmentManager().findFragmentByTag(TAG_TIME_TO) != null) timeTo.setText(text);


        //Toast.makeText(this, "Hour " + hourOfDay + " Minute " + minute, Toast.LENGTH_SHORT).show();
    }
}
