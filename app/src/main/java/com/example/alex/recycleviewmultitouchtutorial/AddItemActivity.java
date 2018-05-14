package com.example.alex.recycleviewmultitouchtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Alex on 08.05.2018.
 */

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    Information data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_activity);
        editText = findViewById(R.id.editText);
        data = getIntent().getParcelableExtra(Information.class.getCanonicalName());
        Button submit = findViewById(R.id.btnSubmit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*
        Intent intent = new Intent();
        String className = Information.class.getCanonicalName();
        Information newItem = new Information(R.drawable.ic_launcher_background, editText.getText().toString());
        intent.putExtra(className, newItem);
        setResult(RESULT_OK, intent);
        finish();
        */
    }
}
