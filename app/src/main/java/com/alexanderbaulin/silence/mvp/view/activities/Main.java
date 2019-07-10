/*
* Silence for Android OS
* Copyright 2018 Alexander Baulin
* Contacts: alexander.baulin.github@yandex.ru
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.alexanderbaulin.silence.mvp.view.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alexanderbaulin.silence.mvp.interfaces.Presenter;
import com.alexanderbaulin.silence.mvp.model.DataItem;
import com.alexanderbaulin.silence.MyApp;
import com.alexanderbaulin.silence.silence.R;
import com.alexanderbaulin.silence.mvp.view.adapters.RecycleAdapter;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.alexanderbaulin.silence.Constants.*;


public class Main extends AppCompatActivity implements RecycleAdapter.OnLongClickListener, RecycleAdapter.OnItemClickListener {

    private MenuItem remove;
    private FloatingActionButton btnFloatingAction;
    private RecycleAdapter adapter;
    private RecyclerView recyclerView;
    private LinkedList<DataItem> data;
    private Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        presenter = new com.alexanderbaulin.silence.mvp.presenter.Presenter();

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
        data = presenter.getData();
        adapter = new RecycleAdapter(this, data);
        adapter.setOnClickItemListener(this);
        adapter.setOnLongItemListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(!MyApp.isNotificationPolicyAccessGranted()) {
            MyApp.requestNotificationAccess();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        saveSelectedItemPositions(outState);
        saveMultiSelectionMode(outState, adapter.isMultiSelection());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        restoreSelectedItemPositions(savedInstanceState);
        restoreMultiSelectionMode(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (adapter.isMultiSelection()) {
            adapter.setMultiSelection(false);
            setSingleSelectionUI();
            adapter.notifyDataSetChanged();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        remove = menu.findItem(R.id.action_remove);
        setUI();
        return true;
    }

    private void setUI() {
        if (adapter.isMultiSelection()) {
            setMultiSelectionUI();
        } else {
            setSingleSelectionUI();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                adapter.setMultiSelection(false);
                setSingleSelectionUI();
                return true;
            case R.id.action_remove:
                for (DataItem dataItem : data) {
                    presenter.cancelAlarm(dataItem, data.indexOf(dataItem));
                }
                adapter.removeSelectedItems();
                adapter.setMultiSelection(false);
                for (DataItem dataItem : data) {
                    if (dataItem.isAlarmOn)
                        presenter.startAlarm(dataItem, data.indexOf(dataItem));
                }
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
        setToolbarTitle(MyApp.getAppContext().getString(R.string.app_name));
        remove.setVisible(false);
        setNavigationButton(false);
        setFloatingActionButton(true);
    }

    private void setMultiSelectionUI() {
        setStatusBarColor(R.color.colorPrimaryDarkMultiselect);
        setToolbarColor(R.color.colorAccent);
        setToolbarTitle(String.valueOf(adapter.getSelectedItemsCount()));
        remove.setVisible(true);
        setNavigationButton(true);
        setFloatingActionButton(false);
    }

    void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void restoreMultiSelectionMode(Bundle savedInstanceState) {
        boolean isMultiSelection = savedInstanceState.getBoolean(MULTI_SELECTION);
        adapter.setMultiSelection(isMultiSelection);
    }

    private void saveSelectedItemPositions(Bundle outState) {
        ArrayList<Integer> selectedItems = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            DataItem dataItem = data.get(i);
            if (dataItem.isSelected) selectedItems.add(i);
        }
        outState.putIntegerArrayList(SELECTED_ITEMS, selectedItems);
    }

    private void saveMultiSelectionMode(Bundle outState, boolean b) {
        outState.putBoolean(MULTI_SELECTION, b);
    }

    private void restoreSelectedItemPositions(Bundle savedInstanceState) {
        ArrayList<Integer> selectedPositions = savedInstanceState.getIntegerArrayList(SELECTED_ITEMS);
        for (int i = 0; i < selectedPositions.size(); i++) {
            int position = selectedPositions.get(i);
            data.get(position).isSelected = true;
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

    private void setFloatingActionButton(boolean isVisible) {
        if (isVisible)
            btnFloatingAction.setVisibility(View.VISIBLE);
        else
            btnFloatingAction.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemLongClick(View itemView, int position) {
        setUI();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (!adapter.isMultiSelection()) {
            Intent intent = new Intent(this, DataActivity.class);
            DataItem item = data.get(position);
            intent.putExtra(DataItem.class.getCanonicalName(), item);
            intent.putExtra(UPDATED_POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_DATA_ITEM);
        } else {
            setToolbarTitle(String.valueOf(adapter.getSelectedItemsCount()));
        }
        if (adapter.getSelectedItemsCount() == 0) {
            adapter.setMultiSelection(false);
            setSingleSelectionUI();
            adapter.notifyDataSetChanged();
            // setUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_UPDATE_DATA_ITEM) {
                DataItem dataItem = result.getParcelableExtra(DataItem.class.getCanonicalName());
                int position = result.getIntExtra(UPDATED_POSITION, -1);
                update(dataItem, position);
            } else if (requestCode == REQUEST_CODE_ADD_DATA_ITEM) {
                DataItem newItem = result.getParcelableExtra(DataItem.class.getCanonicalName());
                add(newItem);
            }
        }
    }

    private void update(DataItem newItem, int position) {
        DataItem updatedItem = data.get(position);
        newItem.id = updatedItem.id;
        newItem.isAlarmOn = updatedItem.isAlarmOn;

        adapter.update(newItem, position);
        presenter.cancelAlarm(updatedItem, position);
        presenter.update(newItem, position);
    }

    private void add(DataItem item) {
        adapter.add(item);
        presenter.add(item, data.indexOf(item));
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    public void onClickFloatingActionButton(View view) {
        if(!MyApp.isNotificationPolicyAccessGranted()) {
            MyApp.requestNotificationAccess();
            return;
        }
        Intent intent = new Intent(this, DataActivity.class);
        intent.setAction(ACTION_ADD_ITEM);
        startActivityForResult(intent, REQUEST_CODE_ADD_DATA_ITEM);
    }
}
