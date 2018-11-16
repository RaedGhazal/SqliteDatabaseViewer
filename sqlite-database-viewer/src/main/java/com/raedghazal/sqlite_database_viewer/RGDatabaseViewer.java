package com.raedghazal.sqlite_database_viewer;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RGDatabaseViewer {
    private Context context;
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    private ArrayAdapter<String> arrayAdapter ;
    private AutoCompleteTextView actv;
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
        parentLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));

        actv = new AutoCompleteTextView(context);
        actv.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        actv.setTextSize(20);

        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(-1, -1));

        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
        horizontalScrollView.setLayoutParams(new HorizontalScrollView.LayoutParams(-1, -1));

        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));


        horizontalScrollView.addView(recyclerView);
        scrollView.addView(horizontalScrollView);
        parentLinearLayout.addView(actv);
        parentLinearLayout.addView(scrollView);

        //setContentView(parentLinearLayout, new LinearLayout.LayoutParams(-1, -1));
        view();
        return parentLinearLayout;
    }
    private void view()
    {
        // fill AutoCompleteTextView
        arrayAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, getAllTables());
        actv.setThreshold(1);
        actv.setAdapter(arrayAdapter);

        // fill RecyclerView on Table Selected
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RGAdapter adapter = new RGAdapter(context,
                        (String[])getDataAndColumns(arrayAdapter.getItem(position)).get(COLUMNS).toArray()
                        ,getDataAndColumns(arrayAdapter.getItem(position)).get(DATA));
                RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
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
        cursorGetTables.close();
        return tablesList;
    }
    private ArrayList<ArrayList> getDataAndColumns(String tableName)
    {
        ArrayList<String[]> dataList = new ArrayList<>();
        ArrayList<String> columnsList;
        ArrayList<ArrayList> dataAndColumnsList = new ArrayList<>();
        db = databaseHelper.getReadableDatabase();
        Cursor cursorGetData = db.rawQuery("select * from "+tableName, null);
        columnsList = new ArrayList<>(Arrays.asList(cursorGetData.getColumnNames()));

        cursorGetData.moveToFirst();
        if (cursorGetData.getCount() > 0)
            while (!cursorGetData.isAfterLast())
            {
                String[] strData =new String[columnsList.size()];

                for (int i = 0;i<cursorGetData.getColumnCount();i++)
                    strData[i] = (cursorGetData.getString(i));
                dataList.add(strData);

                cursorGetData.moveToNext();
            }

        dataAndColumnsList.add(dataList);
        dataAndColumnsList.add(columnsList);
        cursorGetData.close();
        return dataAndColumnsList;
    }


}
