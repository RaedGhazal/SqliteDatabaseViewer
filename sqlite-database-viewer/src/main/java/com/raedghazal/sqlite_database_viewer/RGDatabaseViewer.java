package com.raedghazal.sqlite_database_viewer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class RGDatabaseViewer {
    private Context context;
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    private ArrayAdapter<String> arrayAdapter ;
    private Spinner spinner;
    private int DATA = 0,COLUMNS = 1;

    public RGDatabaseViewer(Context context, SQLiteOpenHelper databaseHelper)
    {
        this.context = context;
        this.databaseHelper = databaseHelper;
    }

    public View getView()
    {
            LinearLayout parentLinearLayout = new LinearLayout(context);
            parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
            parentLinearLayout.setBackgroundColor(Color.LTGRAY);
            parentLinearLayout.setVisibility(View.VISIBLE);
            parentLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1,1));

            spinner = new Spinner(context);
            spinner.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));

            ScrollView scrollView1 = new ScrollView(context);
            scrollView1.setLayoutParams(new ScrollView.LayoutParams(-1, -1));

                HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
                horizontalScrollView.setLayoutParams(new HorizontalScrollView.LayoutParams(-1, -1));

                    recyclerView = new RecyclerView(context);
                    recyclerView.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));

            parentLinearLayout.addView(spinner);
            parentLinearLayout.addView(scrollView1);
                scrollView1.addView(horizontalScrollView);
                    horizontalScrollView.addView(recyclerView);
        view();

        return parentLinearLayout;
    }
    private void view()
    {
        // fill AutoCompleteTextView
        arrayAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, getAllTables());
        spinner.setAdapter(arrayAdapter);

        // fill RecyclerView on Table Selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RGRecyclerViewAdapter adapter = new RGRecyclerViewAdapter(context,
                        getDataAndColumns(arrayAdapter.getItem(position)).get(COLUMNS)
                        ,getDataAndColumns(arrayAdapter.getItem(position)).get(DATA));
                RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private ArrayList<String> getAllTables()
    {
        ArrayList<String> tablesList = new ArrayList<>();
        db = databaseHelper.getReadableDatabase();
        Cursor cursorGetTables=db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'",null);
        cursorGetTables.moveToFirst();
        if (cursorGetTables.getCount()>0)
            while (!cursorGetTables.isAfterLast())
            {
                tablesList.add(cursorGetTables.getString(0));
                cursorGetTables.moveToNext();
            }
        if (tablesList.get(0).equals("android_metadata")) tablesList.remove("android_metadata");
        cursorGetTables.close();
        return tablesList;
    }
    private ArrayList<ArrayList> getDataAndColumns(String tableName)
    {
        ArrayList<String[]> dataList = new ArrayList<>();
        ArrayList<String> columnsList;
        ArrayList<ArrayList> dataAndColumnsList = new ArrayList<>();
        dataList.clear();
        db = databaseHelper.getReadableDatabase();
        Cursor cursorGetData = db.rawQuery("select * from "+tableName, null);
        columnsList = new ArrayList<>(Arrays.asList(cursorGetData.getColumnNames()));

        cursorGetData.moveToFirst();
        if (cursorGetData.getCount() > 0) {
            dataList = new ArrayList<>(cursorGetData.getCount());
            while (!cursorGetData.isAfterLast()) {
                String[] strData = new String[columnsList.size()];

                for (int i = 0; i < cursorGetData.getColumnCount(); i++)
                    strData[i] = (cursorGetData.getString(i));
                dataList.add(strData);

                cursorGetData.moveToNext();
            }
        }
        dataAndColumnsList.add(dataList);
        dataAndColumnsList.add(columnsList);
        cursorGetData.close();
        return dataAndColumnsList;
    }


}
