package com.example.tcp_ip_client_2.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.classs.SearchPortItems;

import java.util.ArrayList;


public class SearchPortAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private static ArrayList<SearchPortItems> data;

    public SearchPortAdapter(Context context, ArrayList<SearchPortItems>datanew){
        inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        data = datanew;
    }
    //public ChatsTitleAdapter(fragment_titles fragment_titles, ArrayList<adapter_listview> list) { }

    @Override
    public int getCount(){ return data.size(); }

    @Override
    public SearchPortItems getItem(int position){ return data.get(position); }

    //@Override
    public static SearchPortItems getItemPosition(int position){ return data.get(position); }

    @Override
    public long getItemId(int position){return 0;}



    @SuppressLint({"InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate( R.layout.row_search_port, null );
        }
        final SearchPortItems item = data.get( position );
        TextView textport = (TextView) view.findViewById( R.id.port );
        TextView textOnline = (TextView) view.findViewById( R.id.is_online );
        if(!item.isPortOnline())
            {
                textOnline.setTextColor(Color.RED);
            }
        else {
            textOnline.setTextColor(Color.GREEN);
        }
        textOnline.setText( item.getPortOnline() );
        textport.setText( item.getPort() );
        return view;
    }


}