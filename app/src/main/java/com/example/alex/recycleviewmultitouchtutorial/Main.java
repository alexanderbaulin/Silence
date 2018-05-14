package com.example.alex.recycleviewmultitouchtutorial;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class Main extends AppCompatActivity implements RecycleAdapter.OnLongClickListener, RecycleAdapter.OnItemClickListener {
    private LinkedList<Information> data;
    private RecycleAdapter adapter;
    private MenuItem remove;
    private FloatingActionButton btnFloatingAction;
    private RecyclerView recyclerView;

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
        adapter.notifyDataSetChanged();
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

    private LinkedList<Information> getData() {
        Information[] data = {
                new Information("15 30 - 17 30", "пн вт ср чт пт"),
                new Information("15 30 - 17 30", "пн вт ср чт пт"),
                new Information("15 30 - 17 30", "пн вт ср чт пт"),
                new Information("15 30 - 17 30", "пн вт ср чт пт")
        };
        LinkedList<Information> result = new LinkedList<>();
        Collections.addAll(result, data);
        return result;
    }

    @Override
    public void onItemLongClick(View itemView, int position) {
        setUI();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if(!adapter.isMultiSelection()) {
            Intent intent = new Intent(this, UpdateItemActivity.class);
            Information info = data.get(position);
            intent.putExtra(Information.class.getCanonicalName(), info);
            intent.putExtra("updatedPosition", position);
            startActivityForResult(intent, 1);
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
        /*
        if(resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Information information = result.getParcelableExtra(Information.class.getCanonicalName());
                int position = result.getIntExtra("updatedPosition", -1);
                data.get(position).text = information.text;
                adapter.notifyItemChanged(position);
                Log.d("myLogs", "from request " + information.text);
            } else if(requestCode == 2) {
                Information information = result.getParcelableExtra(Information.class.getCanonicalName());
                data.add(information);
                int size = adapter.getItemCount();
                recyclerView.scrollToPosition(size-1);
                adapter.notifyItemChanged(size);
                Log.d("myLogs", "size = " + size);
            }
        }
        */
    }

    public void onClickFloatingActionButton(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivityForResult(intent, 2);
        Log.d("myLogs", "onClickFloatingActionButton");
    }
}
