package com.example.alex.recycleviewmultitouchtutorial;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.logging.Logger;

/**
 * Created by Alex on 08.05.2018.
 */

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_activity);
        Button submit = findViewById(R.id.btnSubmit);
        Information data = getIntent().getParcelableExtra(Information.class.getCanonicalName());
        position = getIntent().getIntExtra("position", -1);
        Log.d("myLogs", "position update item = " + position);
        editText = findViewById(R.id.editText);
        editText.setText(data.text);
        submit.setOnClickListener(this);
        //Log.d("myLogs", "text = " + data.text);
    }


    @Override
    public void onClick(View v) {
        Intent result = new Intent();
        result.putExtra(Information.class.getCanonicalName(), new Information(R.drawable.ic_launcher_background, editText.getText().toString()));
        result.putExtra("position", position);
        setResult(RESULT_OK, result);
        finish();
    }
}
