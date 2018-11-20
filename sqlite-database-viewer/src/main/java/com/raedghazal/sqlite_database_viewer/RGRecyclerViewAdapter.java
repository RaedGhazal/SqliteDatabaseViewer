package com.raedghazal.sqlite_database_viewer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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
    private RecyclerView recyclerView;

    public RGRecyclerViewAdapter(Context context, ArrayList<String> headerList, ArrayList<String[]> dataList) {
        this.context = context;
        this.headerList = headerList;
        this.dataList = dataList;
        checkLengths();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    private boolean checkLengths()
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
        GradientDrawable sidesBorder = new GradientDrawable();
        sidesBorder.setStroke(1, Color.DKGRAY);
        sidesBorder.setGradientType(GradientDrawable.RECTANGLE);

        GradientDrawable bottomBorder = new GradientDrawable();
        bottomBorder.setStroke(1, Color.DKGRAY,10,10);

        Drawable[] layers = {sidesBorder,bottomBorder};
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setLayerInset(0,1,-2,1,-2);
        layerDrawable.setLayerInset(1,-2,-2,-2,1);
        return layerDrawable;
    }
    private LayerDrawable itemBorder(String color,int dashesColor)
    {
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.parseColor(color));
        if (dashesColor == 0) border.setStroke(1, Color.WHITE,10,10);
        if (dashesColor == 1) border.setStroke(1, Color.DKGRAY,10,10);
        //border.setGradientType(GradientDrawable.RECTANGLE);

        Drawable[] layers = {border};
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        layerDrawable.setLayerInset(0,-2,-2,-2,1);
        return layerDrawable;
    }
    private int[] dataLength()
    {
        int[] length = new int[headerList.size()];
        for (int i=0;i<length.length;i++)
        {
            int maxLength =0;
            for (int n = 0; n< dataList.size(); n++)
            {
                if (dataList.get(n)[i].length()>maxLength)
                    maxLength = dataList.get(n)[i].length();
            }
            length[i] = maxLength;
        }
        Log.e("dataLength",""+length.length);
        return length;
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
                tvs.get("tv"+i).setLayoutParams(new LinearLayout.LayoutParams(-2,120));
                tvs.get("tv"+i).setLines(1);
                tvs.get("tv"+i).setPaddingRelative(padding,0,padding,10);
                tvs.get("tv"+i).setGravity(Gravity.BOTTOM);
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position)
    {
        for (int i = 0; i< headerList.size(); i++) {
            if (holder.getAdapterPosition() == 0) {
                SpannableString ss = new SpannableString("." + headerList.get(i));
                ss.setSpan(new RelativeSizeSpan(2f), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                holder.tvs.get("tv" + i).setPaddingRelative(0, 0, 0, 0);
                holder.tvs.get("tv" + i).setTextColor(Color.parseColor("#ffffff"));

                holder.llHolder.setBackgroundColor(Color.parseColor("#303F9F"));
                holder.tvs.get("tv" + i).setText(ss);
            } else {
                holder.llHolder.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.tvs.get("tv" + i).setBackground(border()/*context.getResources().getDrawable(R.drawable.border)*//*border()*/);
                holder.tvs.get("tv" + i).setText(dataList.get(holder.getAdapterPosition()-1)[i]);
            }

            holder.tvs.get("tv" + i).setEms((dataLength()[i] / 2 )+ 2);
            holder.tvs.get("tv" + i).measure(0,0);
            /*holder.tvs.get("tv" + i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                }
            });*/
            //manage width
            {
                Point point = new Point();
                ((Activity)context).getWindow().getWindowManager().getDefaultDisplay().getSize(point);
                int screenWidth = point.x;
                int itemWidth = screenWidth/holder.llHolder.getChildCount();
                int minWidth  = holder.tvs.get("tv" + i).getMeasuredWidth();
                for (int x = 0;x<=i;x++)
                {
                    if (itemWidth > minWidth)
                        holder.tvs.get("tv" + i).setWidth(itemWidth);
                    else
                        holder.tvs.get("tv" + i).setWidth(minWidth);
                }
            }

        }

        if (holder.getAdapterPosition()>0)
            holder.llHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*RGRecyclerViewAdapter rgRecyclerViewAdapter = new RGRecyclerViewAdapter(context,headerList,dataList);
                recyclerView.setAdapter(rgRecyclerViewAdapter);*/
                    ArrayList<Detail> details = new ArrayList<>();
                    for (int i = 0; i<headerList.size();i++) {
                        Detail detail = new Detail();
                        detail.setHeader(headerList.get(i));
                        detail.setDetail(dataList.get(holder.getAdapterPosition()-1)[i]);
                        details.add(detail);
                    }
                    DetailsAdapter detailsAdapter = new DetailsAdapter(details);
                    recyclerView.setAdapter(detailsAdapter);

                }
            });


    }
    @Override
    public int getItemCount()
    {
        return dataList.size()+1;
    }


    private class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder>
    {
        private ArrayList<Detail> details;

        DetailsAdapter(ArrayList<Detail> details) {
            this.details = details;

            Detail top =new Detail();
            top.setHeader(" Back ");
            top.setDetail("");
            details.add(0,top);
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            private LinearLayout llHolder;
            private TextView tvHeader,tvDetail;

            private MyViewHolder(View view) {
                super(view);

                int padding  = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, context.getResources().getDisplayMetrics());
                llHolder =(LinearLayout)itemView;

                Point point = new Point();
                ((Activity)context).getWindow().getWindowManager().getDefaultDisplay().getSize(point);
                int screenWidth = point.x;

                tvHeader = new TextView(context);
                tvHeader.setLayoutParams(new LinearLayout.LayoutParams(-1,120,1));
                tvHeader.setTextColor(Color.parseColor("#FFFFFF"));
                tvHeader.setGravity(Gravity.BOTTOM);
                tvHeader.setPadding(padding,0,padding,10);
                tvHeader.setWidth(screenWidth/2);

                tvDetail = new TextView(context);
                tvDetail.setLayoutParams(new LinearLayout.LayoutParams(-1,120,1));
                tvDetail.setTextColor(Color.parseColor("#000000"));
                tvDetail.setGravity(Gravity.BOTTOM);
                tvDetail.setPadding(padding,0,padding,10);
                tvDetail.setWidth(screenWidth/2);
                llHolder.addView(tvHeader);
                llHolder.addView(tvDetail);
            }
        }

        @NonNull
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
            Detail detail = details.get(position);

            if (position == 0)
            {
                SpannableString content = new SpannableString(detail.getHeader());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                content.setSpan(new ForegroundColorSpan(Color.parseColor("#303F9F")), 0, content.length(), 0);

                holder.tvHeader.setText(content);
                holder.tvDetail.setText(detail.getDetail());
                holder.tvHeader.setTextSize(25);
                holder.tvHeader.setGravity(Gravity.BOTTOM|Gravity.START);
                holder.llHolder.setBackgroundColor(Color.LTGRAY);
                holder.llHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerView.setAdapter(new RGRecyclerViewAdapter(context,headerList,dataList));
                    }
                });
            }else {
                holder.tvHeader.setText(detail.getHeader());
                holder.tvDetail.setText(detail.getDetail());
                holder.tvHeader.setBackground(itemBorder("#303F9F", 0));
                holder.tvDetail.setBackground(itemBorder("#FFFFFF", 1));
            }
        }

        @Override
        public int getItemCount()
        {
            return details.size();
        }
    }
    private class Detail
    {
        private String Header;
        private String Detail;

        public String getDetail() {
            return Detail;
        }

        public void setDetail(String detail) {
            Detail = detail;
        }

        public String getHeader() {
            return Header;
        }

        public void setHeader(String header) {
            Header = header;
        }
    }
}