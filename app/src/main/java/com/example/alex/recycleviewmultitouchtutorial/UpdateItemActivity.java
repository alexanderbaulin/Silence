package com.example.alex.recycleviewmultitouchtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class UpdateItemActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    int updatedPosition;
    Information data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_activity);
        Button submit = findViewById(R.id.btnSubmit);
        editText = findViewById(R.id.editText);
        data = getIntent().getParcelableExtra(Information.class.getCanonicalName());
        updatedPosition = getIntent().getIntExtra("updatedPosition", -1);
        editText.setText(data.text);
        submit.setOnClickListener(this);
        Log.d("myLogs", "updatedPosition update item = " + updatedPosition);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String className = Information.class.getCanonicalName();
        Information newItem = new Information(R.drawable.ic_launcher_background, editText.getText().toString());
        intent.putExtra(className, newItem);
        intent.putExtra("updatedPosition", updatedPosition);
        setResult(RESULT_OK, intent);
        finish();
    }
}
