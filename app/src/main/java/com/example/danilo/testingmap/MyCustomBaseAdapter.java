package com.example.danilo.testingmap;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Danilo on 2016-09-03.
 */
public class MyCustomBaseAdapter extends BaseAdapter {

    List<ordersDB> searchArrayList;

    private LayoutInflater mInflater;
    //private LayoutInflater worksLayoutInflater;
    private Context mcontext;

    public MyCustomBaseAdapter(Context context, List<ordersDB> results) {
        super();
        this.mcontext = context;
        this.searchArrayList = results;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return searchArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        ViewHolder holder;

        if(v == null)
        {
            v = mInflater.inflate(R.layout.worksavailableitems,parent, false);
            holder = new ViewHolder();
            holder.txtName = (TextView) v.findViewById(R.id.txtOrderName);
            holder.txtCityState = (TextView) v.findViewById(R.id.txtOrderLoc);
            holder.txtPhone = (TextView) v.findViewById(R.id.txtOrderNumber);

            v.setTag(holder);

        }
        else
        {
            holder = (ViewHolder) v.getTag();
        }


        holder.txtName.setText(searchArrayList.get(position).getCustomerName());
        holder.txtCityState.setText(searchArrayList.get(position).getProvince());
        holder.txtPhone.setText(searchArrayList.get(position).getCity());

        return  v;

    }
    private static class ViewHolder {
        TextView txtName;
        TextView txtCityState;
        TextView txtPhone;
    }
}




        /*
        v =  mInflater.inflate(R.layout.worksavailableitems,parent,false);
        String orderNum = searchArrayList.get(position).getUserId();

        String name = searchArrayList.get(position).getCustomerName().toString();
        Log.d("orderNumber", "getView: " + orderNum + " " + name);
        //String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_3));
        //String placeTogo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_4));
        String Address = searchArrayList.get(position).getCity().toString();
        //String whatTheyWant = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_6));

        TextView orderN = (TextView) v.findViewById(R.id.txtOrderNumber);
        orderN.setText(orderNum);

        TextView customerName = (TextView) v.findViewById(R.id.txtOrderName);
        customerName.setText(name);

        TextView location = (TextView) v.findViewById(R.id.txtOrderLoc);
        location.setText(Address);*/