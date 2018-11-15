package com.raedghazal.sqlitedatabaseviewerexample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DatabaseViewer {
    private Context context;
    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    private ArrayAdapter<String> arrayAdapter ;
    private AutoCompleteTextView actv;
    private int DATA = 0,COLUMNS = 1;

    public DatabaseViewer(Context context,SQLiteOpenHelper databaseHelper)
    {
        this.context = context;
        this.databaseHelper = databaseHelper;
    }

    protected View onCreate() {

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
    public void view()
    {
        // fill AutoCompleteTextView
        arrayAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, getAllTables());
        actv.setThreshold(1);
        actv.setAdapter(arrayAdapter);

        // fill RecyclerView on Table Selected
        actv.setOnItemClickListener((parent, view, position, id) -> {
            MyAdapter adapter = new MyAdapter(
                    getDataAndColumns(arrayAdapter.getItem(position)).get(COLUMNS)
                    ,getDataAndColumns(arrayAdapter.getItem(position)).get(DATA));
            RecyclerView.LayoutManager mLayoutManager= new LinearLayoutManager(context);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
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
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
    {
        private ArrayList<String> columnsList;
        private ArrayList<String[]> dataList;
        int columnsTextEms[];

        MyAdapter(ArrayList<String> columnsList, ArrayList<String[]> dataList) {
            this.columnsList = columnsList;
            this.dataList = dataList;
            //Add the Header
            String[] columns = new String[columnsList.size()];
            columns = columnsList.toArray(columns);
            dataList.add(0,columns);

            columnsTextEms = new int[columnsList.size()];
        }

        private LayerDrawable border()
        {
            GradientDrawable border = new GradientDrawable();
            border.setStroke(1, Color.DKGRAY);
            border.setGradientType(GradientDrawable.RECTANGLE);

            Drawable[] layers = {border};
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            layerDrawable.setLayerInset(0,1,-2,1,-2);
            return layerDrawable;
        }
        private int[] ems()
        {
            int[] ems = new int[columnsList.size()];
            for (int i=0;i<ems.length;i++)
            {
                int maxLength =0;
                for (int n = 0; n< dataList.size(); n++)
                {
                    if (dataList.get(n)[i].length()>maxLength)
                        maxLength = dataList.get(n)[i].length();
                }
                ems[i] = maxLength;
            }
            Log.e("ems",""+ems.length);
            return ems;
        }
        class MyViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout llHolder;

            //all the TextView(s)
            HashMap<String,TextView> tvs = new HashMap<>();

            MyViewHolder(View view) {
                super(view);
                int margin  = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, context.getResources().getDisplayMetrics());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                llHolder =(LinearLayout)itemView;// view.findViewById(R.id.llHolder);
                for (int i = 0; i< columnsList.size(); i++)
                {
                    tvs.put("tv"+i,new TextView(view.getContext()));
                    tvs.get("tv"+i).setLayoutParams(lp);
                    tvs.get("tv"+i).setLines(1);
                    tvs.get("tv"+i).setPaddingRelative(margin,0,margin,0);
                    tvs.get("tv"+i).setTextColor(Color.parseColor("#000000"));
                    llHolder.addView(tvs.get("tv"+i));
                }
            }
        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
            Context context = parent.getContext();
            LinearLayout ll = new LinearLayout(context);
            RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setLayoutParams(layoutParams);
            return new MyViewHolder(ll);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            for (int i = 0; i< columnsList.size(); i++)
            {
                if (position == 0)
                {
                    holder.tvs.get("tv"+i).setPaddingRelative(0,0,0,0);
                    holder.tvs.get("tv"+i).setTextColor(Color.parseColor("#ffffff"));
                    String txt = "."+ dataList.get(position)[i];
                    SpannableString ss = new SpannableString(txt);
                    RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2f);
                    ss.setSpan(relativeSizeSpan,0,1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    ss.setSpan(new ForegroundColorSpan(Color.RED),0,1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    columnsTextEms[i] = dataList.get(position)[i].length();
                    holder.llHolder.setBackgroundColor(Color.parseColor("#303F9F"));
                    holder.tvs.get("tv"+i).setText(ss);
                }
                else
                {
                    holder.tvs.get("tv"+i).setBackground(border());
                    holder.tvs.get("tv"+i).setText(dataList.get(position)[i]);
                }

                holder.tvs.get("tv"+i).setEms(ems()[i]/2 +2);
                holder.tvs.get("tv"+i).setOnClickListener(view -> Toast.makeText(view.getContext(),((TextView)view).getText(),Toast.LENGTH_SHORT).show());
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

    }

}
