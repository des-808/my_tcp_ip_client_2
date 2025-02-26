package com.example.tcp_ip_client_2.ui.bluetooth;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tcp_ip_client_2.R;

import java.util.ArrayList;


public class DeviceListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private static ArrayList<adapter_listview> data;
    private com.example.tcp_ip_client_2.ui.bluetooth.find_bluetooth_row find_bluetooth_row;
    public DeviceListAdapter(Context context, ArrayList<adapter_listview>data){
        inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.data = data;
    }

    @Override
    public int getCount(){ return data.size(); }

    @Override
    public adapter_listview getItem(int position){ return data.get(position); }

    public static adapter_listview getItemPosition(int position){ return data.get(position); }

    @Override
    public long getItemId(int position){return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate( R.layout.row_devicelist, null );
        }
        find_bluetooth_row p = find_bluetooth_row;
        final adapter_listview item = data.get( position );
        //((ImageView) view.findViewById(R.id.image_online)).setImageResource(p.image);
        TextView textonline = (TextView) view.findViewById( R.id.row_online );

        TextView textname = (TextView) view.findViewById( R.id.row_name );
        TextView textmac = (TextView) view.findViewById( R.id.row_mac );

        //textonline.setText( item.getImageOnline() );
        textonline.setText( item.getTextOnline() );
        textname.setText( item.getTextName() );
        textmac.setText( item.getTextMac() );
        return view;
    }


}
