package com.raedghazal.sqlite_database_viewer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;


public class RGRecyclerViewAdapter extends RecyclerView.Adapter<RGRecyclerViewAdapter.MyViewHolder>
{
    private ArrayList<String> headerList;
    private ArrayList<String[]> dataList;
    private Context context;

    public RGRecyclerViewAdapter(Context context, ArrayList<String> headerList, ArrayList<String[]> dataList) {
        this.context = context;
        this.headerList = headerList;
        this.dataList = dataList;
        if (!checkLengths())
            return;
        //Add the Header
        String[] header = new String[headerList.size()];
        for (int i = 0;i<headerList.size();i++)
            header[i] = headerList.get(i);
        dataList.add(0,header);
    }
    public boolean checkLengths()
    {
        int maxLength = headerList.size();
        for (int i = 0; i < dataList.size();i++)
        {
            if (dataList.get(i).length > maxLength)
            {
                Toast.makeText(context,"data length shouldn't be bigger than header length",Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
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
        int[] ems = new int[headerList.size()];
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
    class MyViewHolder extends RecyclerView.ViewHolder
    {

        private LinearLayout llHolder;
        HashMap<String,TextView> tvs = new HashMap<>();

        private MyViewHolder(View view) {
            super(view);

            int padding  = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, context.getResources().getDisplayMetrics());
            llHolder =(LinearLayout)itemView;
            for (int i = 0; i< headerList.size(); i++)
            {
                tvs.put("tv"+i,new TextView(view.getContext()));
                tvs.get("tv"+i).setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
                tvs.get("tv"+i).setLines(1);
                tvs.get("tv"+i).setPaddingRelative(padding,0,padding,0);
                tvs.get("tv"+i).setTextColor(Color.parseColor("#000000"));

                llHolder.addView(tvs.get("tv"+i));
            }
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LinearLayout ll = new LinearLayout(context);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(layoutParams);
        return new MyViewHolder(ll);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        for (int i = 0; i< headerList.size(); i++) {
            if (position == 0) {
                SpannableString ss = new SpannableString("." + dataList.get(position)[i]);
                ss.setSpan(new RelativeSizeSpan(2f), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                holder.tvs.get("tv" + i).setPaddingRelative(0, 0, 0, 0);
                holder.tvs.get("tv" + i).setTextColor(Color.parseColor("#ffffff"));

                holder.llHolder.setBackgroundColor(Color.parseColor("#303F9F"));
                holder.tvs.get("tv" + i).setText(ss);
            } else {
                holder.llHolder.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.tvs.get("tv" + i).setBackground(border());
                holder.tvs.get("tv" + i).setText(dataList.get(position)[i]);
            }

            holder.tvs.get("tv" + i).setEms(ems()[i] / 2 + 2);
            holder.tvs.get("tv" + i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

}