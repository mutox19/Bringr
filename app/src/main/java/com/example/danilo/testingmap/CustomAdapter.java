package com.example.danilo.testingmap;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.List;

/**
 * Created by Danilo on 2016-04-30.
 */
public class CustomAdapter extends BaseAdapter{

    private static List<ordersDB>searchList;
    private LayoutInflater worksLayoutInflater;
    private Context mcontext;

    public CustomAdapter(Context context, List<ordersDB> results) {
        super();
        mcontext = context;
        worksLayoutInflater = LayoutInflater.from(context);
        searchList = results;
    }

    @Override
    public int getCount() {
        return searchList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        ViewHolder holder;

        if(v == null)
        {
            v = worksLayoutInflater.inflate(R.layout.worksavailableitems,parent, false);
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


        holder.txtName.setText(searchList.get(position).getCustomerName());
        holder.txtCityState.setText(searchList.get(position).getProvince());
        holder.txtPhone.setText(searchList.get(position).getCity());

        return  v;

    }

    private static class ViewHolder {
        TextView txtName;
        TextView txtCityState;
        TextView txtPhone;
    }

    /*
    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        String orderNum = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_1));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_2));
        //String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_3));
        //String placeTogo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_4));
        String Address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_5));
        //String whatTheyWant = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_6));

        TextView orderN = (TextView) view.findViewById(R.id.txtOrderNumber);
        orderN.setText(orderNum);

        TextView customerName = (TextView) view.findViewById(R.id.txtOrderName);
        customerName.setText(name);

        TextView location = (TextView) view.findViewById(R.id.txtOrderLoc);
        location.setText(Address);



    }*/
}
