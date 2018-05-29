package com.example.alex.recycleviewmultitouchtutorial;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class UpdateItemActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    int updatedPosition;
    Data data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_activity);
        /*
        Button submit = findViewById(R.id.btnSubmit);
        editText = findViewById(R.id.editText);
        dataItem = getIntent().getParcelableExtra(Data.class.getCanonicalName());
        updatedPosition = getIntent().getIntExtra("updatedPosition", -1);
        editText.setText(dataItem.text);
        submit.setOnClickListener(this);
        Log.checkedDays("myLogs", "updatedPosition update item = " + updatedPosition);
        */
    }

    @Override
    public void onClick(View v) {
        /*
        Intent intent = new Intent();
        String className = Data.class.getCanonicalName();
        Data newItem = new Data(R.drawable.ic_launcher_background, editText.getText().toString());
        intent.putExtra(className, newItem);
        intent.putExtra("updatedPosition", updatedPosition);
        setResult(RESULT_OK, intent);
        finish();
        */
    }

    public void checkButton(View view) {
    }

    public void submit(View view) {
        Log.d("checkButton", "dsfsdf");
    }
}
