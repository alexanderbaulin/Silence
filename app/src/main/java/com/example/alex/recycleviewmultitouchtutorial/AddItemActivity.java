package com.example.alex.recycleviewmultitouchtutorial;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;


public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    EditText editText;
    Data data;
    Button timeFrom, timeTo, submit;
    final String TAG_TIME_PICKER_FROM = "time from";
    final String TAG_TIME_PICKER_TO = "time to";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_activity);
        data = getIntent().getParcelableExtra(Data.class.getCanonicalName());
        editText = findViewById(R.id.editText);
        timeFrom = findViewById(R.id.btnTimeFrom);
        timeTo = findViewById(R.id.btnTimeTo);
        submit = findViewById(R.id.btnSubmit);
        timeFrom.setOnClickListener(this);
        timeTo.setOnClickListener(this);
        submit.setOnClickListener(this);
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
            }
        /*
        Intent intent = new Intent();
        String className = Data.class.getCanonicalName();
        Data newItem = new Data(R.drawable.ic_launcher_background, editText.getText().toString());
        intent.putExtra(className, newItem);
        setResult(RESULT_OK, intent);
        finish();
        */
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String text= buildString(hourOfDay, minute);
        if(findFragmentByTag(TAG_TIME_PICKER_FROM)) timeFrom.setText(text);
        if(findFragmentByTag(TAG_TIME_PICKER_TO)) timeTo.setText(text);
    }

    private String buildString(int hourOfDay, int minute) {
        StringBuilder builder = new StringBuilder();
        builder
                .append(hourOfDay)
                .append(":")
                .append(minute);
        return builder.toString();
    }

    private boolean findFragmentByTag(String tag) {
       return getSupportFragmentManager().findFragmentByTag(tag) != null;
    }
}
