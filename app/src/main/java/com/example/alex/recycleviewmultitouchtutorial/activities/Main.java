package com.example.alex.recycleviewmultitouchtutorial.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.alex.recycleviewmultitouchtutorial.Data;
import com.example.alex.recycleviewmultitouchtutorial.R;
import com.example.alex.recycleviewmultitouchtutorial.adapters.RecycleAdapter;
import com.example.alex.recycleviewmultitouchtutorial.database.Base;

import java.util.ArrayList;
import java.util.LinkedList;


public class Main extends AppCompatActivity implements RecycleAdapter.OnLongClickListener, RecycleAdapter.OnItemClickListener {
    private LinkedList<Data> data;
    private RecycleAdapter adapter;
    private MenuItem remove;
    private FloatingActionButton btnFloatingAction;
    private RecyclerView recyclerView;
    final int REQUEST_CODE_UPDATE_DATA_ITEM = 1;
    final int REQUEST_CODE_ADD_DATA_ITEM = 2;
    final static String ACTION_ADD_ITEM = "add item";
    final static String ACTION_UPDATE_ITEM = "update item";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("myLogs", "Main onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        recyclerView = findViewById(R.id.drawerList);
        Toolbar toolbar = findViewById(R.id.toolbar);
        btnFloatingAction = findViewById(R.id.floatingActionButton);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSingleSelectionUI();
                adapter.setMultiSelection(false);
                adapter.notifyDataSetChanged();
            }
        });
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
            setSingleSelectionUI();
            adapter.notifyDataSetChanged();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("myLogs", "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu, menu);
        remove = menu.findItem(R.id.action_remove);
        setUI();
        return true;
    }

    private void setUI() {
        if(adapter.isMultiSelection()) {
            setMultiSelectionUI();
        }
        else {
            setSingleSelectionUI();
        }
        //adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                adapter.setMultiSelection(false);
                setSingleSelectionUI();
                return true;
            case R.id.action_remove:
                Log.d("myLogs", "remove");
                adapter.removeSelectedItems();
                adapter.setMultiSelection(false);
                setUI();
                int size = adapter.getItemCount();
                adapter.notifyItemRangeChanged(0, size);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSingleSelectionUI() {
        setStatusBarColor(R.color.colorPrimaryDark);
        setToolbarColor(R.color.colorPrimary);
        setToolbarTitle("Title");
        remove.setVisible(false);
        setNavigationButton(false);
        setAddFloatingActionButton(true);
    }

    private void setMultiSelectionUI() {
        setStatusBarColor(R.color.colorPrimaryDarkMultiselect);
        setToolbarColor(R.color.colorAccent);
        setToolbarTitle(String.valueOf(adapter.getSelectedItemsCount()));
        remove.setVisible(true);
        setNavigationButton(true);
        setAddFloatingActionButton(false);
    }

    void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void restoreMultiSelectionMode(Bundle savedInstanceState) {
        boolean isMultiSelection = savedInstanceState.getBoolean("isMultiSelection");
        adapter.setMultiSelection(isMultiSelection);
        Log.d("myLogs", "isMultiSelectionRestore " + isMultiSelection);
    }

    private void saveSelectedItemPositions(Bundle outState) {
        ArrayList<Integer> selectedItems = new ArrayList<>();
        for(int i = 0; i < data.size(); i++) {
            Data dataItem = data.get(i);
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
        setToolbarTitle(String.valueOf(selectedPositions.size()));
        adapter.notifyDataSetChanged();
    }

    private void setStatusBarColor(int color) {
        getWindow().setStatusBarColor(getColor2(color));
    }

    private void setToolbarColor(int id) {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getColor2(id)));
    }

    private int getColor2(int id) {
        return ContextCompat.getColor(this, id);
    }

    private void setNavigationButton(boolean b) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(b);
    }

    private void setAddFloatingActionButton(boolean b) {
        if(b)
            btnFloatingAction.setVisibility(View.VISIBLE);
        else
            btnFloatingAction.setVisibility(View.INVISIBLE);
    }

    private LinkedList<Data> getData() {
        Base db = new Base(getApplicationContext());
        return db.select();
    }

    @Override
    public void onItemLongClick(View itemView, int position) {
        setUI();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if(!adapter.isMultiSelection()) {
            Intent intent = new Intent(this, DataItem.class);
            Data item = data.get(position);
            intent.putExtra(Data.class.getCanonicalName(), item);
            intent.putExtra("updatedPosition", position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_DATA_ITEM);
        } else {
            setToolbarTitle(String.valueOf(adapter.getSelectedItemsCount()));
        }
        if(adapter.getSelectedItemsCount() == 0) {
            adapter.setMultiSelection(false);
            setSingleSelectionUI();
            adapter.notifyDataSetChanged();
           // setUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if(resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_UPDATE_DATA_ITEM) {
                Log.d("updateItem", "updateItemResult");

                Data dataItem = result.getParcelableExtra(Data.class.getCanonicalName());
                int position = result.getIntExtra("updatedPosition", -1);
                Data updatedItem = data.get(position);
                updatedItem.description = dataItem.description;
                updatedItem.timeBegin = dataItem.timeBegin;
                updatedItem.timeEnd= dataItem.timeEnd;
                updatedItem.checkedDays = dataItem.checkedDays;
                updatedItem.isVibrationAllowed = dataItem.isVibrationAllowed;
                Base db = new Base(getApplicationContext());
                db.update(updatedItem.id, updatedItem);
                adapter.notifyItemChanged(position);

            } else if(requestCode == REQUEST_CODE_ADD_DATA_ITEM) {
                Log.d("addItem", "addItemResult");
                addNewItem(result);
            }
        }
    }

    private void addNewItem(Intent result) {
        Data newDataItem = result.getParcelableExtra(Data.class.getCanonicalName());
        Base db = new Base(getApplicationContext());
        db.insert(newDataItem);
        data.add(newDataItem);
        int newItemPosition = adapter.getItemCount();
        adapter.notifyItemChanged(newItemPosition);
        recyclerView.scrollToPosition(newItemPosition-1);
    }

    public void onClickFloatingActionButton(View view) {
        Intent intent = new Intent(this, DataItem.class);
        intent.setAction(ACTION_ADD_ITEM);
        startActivityForResult(intent, REQUEST_CODE_ADD_DATA_ITEM);
        Log.d("myLogs", "onClickFloatingActionButton");
    }
}