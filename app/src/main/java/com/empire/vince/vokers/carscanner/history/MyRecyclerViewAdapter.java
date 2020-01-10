package com.empire.vince.vokers.carscanner.history;

/**
 * Created by maditsha on 9/9/2016.
 */


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.empire.vince.vokers.carscanner.R;
import com.empire.vince.vokers.carscanner.model.CarUtil;
import com.empire.vince.vokers.carscanner.model.Driver;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static MyClickListener myClickListener;
    private ArrayList<CarUtil> mDataset = new ArrayList<>();

    public MyRecyclerViewAdapter(ArrayList<CarUtil> myDataset) {
       // this.clearApplications();
        mDataset = myDataset;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }



    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.plateNo.setText(mDataset.get(position).getCarNoPlate());
        holder.route.setText(mDataset.get(position).getRoute());
        holder.cellNo.setText(mDataset.get(position).getCellNum());

        //.setTextColor(this.getResources().getColor(R.color.orange));

    }

    public void addItem(CarUtil dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView plateNo;
        TextView route;
        TextView cellNo;

        public DataObjectHolder(View itemView) {
            super(itemView);
            plateNo = (TextView) itemView.findViewById(R.id.textViewPlateNo);
            route = (TextView) itemView.findViewById(R.id.textRoute);
            cellNo = (TextView) itemView.findViewById(R.id.textCellNo);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }
}