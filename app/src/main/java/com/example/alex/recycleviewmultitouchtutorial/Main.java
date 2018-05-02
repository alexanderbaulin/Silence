package com.example.alex.recycleviewmultitouchtutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main extends AppCompatActivity implements RecycleAdapter.OnLongClickListener, RecycleAdapter.OnItemClickListener {
    private List<Information> data = Collections.emptyList();
    private RecycleAdapter adapter;
    private MenuItem remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("myLogs", "Main onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        RecyclerView recyclerView = findViewById(R.id.drawerList);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        data = getData();
        adapter = new RecycleAdapter(this, data);
        adapter.setOnClickItemListener(this);
        adapter.setOnLongItemListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("myLogs", "onSaveInstanceState");
        saveSelectedItemPositions(outState);
        saveMultiSelectionMode(outState, adapter.isMultiSelection());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("myLogs", "onRestoreInstanceState");
        restoreSelectedItemPositions(savedInstanceState);
        restoreMultiSelectionMode(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if(adapter.isMultiSelection()) {
            adapter.setMultiSelection(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("myLogs", "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu, menu);
        remove = menu.findItem(R.id.action_remove);
        if(adapter.isMultiSelection())
            remove.setVisible(true);
        else
            remove.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                adapter.setMultiSelection(false);
                remove.setVisible(false);
                return true;
            case R.id.action_remove:
                Log.d("myLogs", "remove");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void restoreMultiSelectionMode(Bundle savedInstanceState) {
        boolean isMultiSelection = savedInstanceState.getBoolean("isMultiSelection");
        adapter.setMultiSelection(isMultiSelection);
        Log.d("myLogs", "isMultiSelectionRestore " + isMultiSelection);
    }

    private void saveSelectedItemPositions(Bundle outState) {
        ArrayList<Integer> selectedItems = new ArrayList<>();
        for(int i = 0; i < data.size(); i++) {
            Information dataItem = data.get(i);
            if(dataItem.isSelected) selectedItems.add(i);
        }
        outState.putIntegerArrayList("selectedItems", selectedItems);
    }

    private void saveMultiSelectionMode(Bundle outState, boolean b) {
        outState.putBoolean("isMultiSelection", b);
    }

    private void restoreSelectedItemPositions(Bundle savedInstanceState) {
        ArrayList<Integer> selectedPositions = savedInstanceState.getIntegerArrayList("selectedItems");
        for(int i = 0; i < selectedPositions.size(); i++) {
            int position = selectedPositions.get(i);
            data.get(position).isSelected = true;
            Log.d("myLogs", "selectedItem " + position);
        }
        adapter.setToolbarTitle(String.valueOf(selectedPositions.size()));
        adapter.notifyDataSetChanged();
    }

    List<Information> getData() {
        Information[] data = {
                new Information(R.drawable.ic_launcher_background, "0"),
                new Information(R.drawable.ic_launcher_background, "1"),
                new Information(R.drawable.ic_launcher_background, "2"),
                new Information(R.drawable.ic_launcher_background, "3"),
                new Information(R.drawable.ic_launcher_background, "4"),
                new Information(R.drawable.ic_launcher_background, "5"),
                new Information(R.drawable.ic_launcher_background, "6"),
                new Information(R.drawable.ic_launcher_background, "7"),
                new Information(R.drawable.ic_launcher_background, "8"),
                new Information(R.drawable.ic_launcher_background, "9")
        };
        return Arrays.asList(data);
    }

    @Override
    public void onItemLongClick(View itemView, int position) {
        if(adapter.isMultiSelection()) remove.setVisible(true);
        else remove.setVisible(false);
    }


    @Override
    public void onItemClick(View itemView, int position) {
        if(adapter.isMultiSelection()) remove.setVisible(true);
        else remove.setVisible(false);
    }
}
