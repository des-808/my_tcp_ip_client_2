package com.example.tcp_ip_client_2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.classs.ServerListItem;

import java.util.ArrayList;

public class ServerListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private static ArrayList<ServerListItem>data;

    public ServerListAdapter(Context context, ArrayList<ServerListItem>data){
        inflater = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.data = data;
    }

    @Override
    public int getCount(){ return data.size(); }

    @Override
    public ServerListItem getItem(int position){ return data.get(position); }

    //@Override
    public static ServerListItem getItemPosition(int position){ return data.get(position); }

    @Override
    public long getItemId(int position){return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = inflater.inflate( R.layout.row_listview, null );
        }
        final ServerListItem item = data.get( position );

        ImageView connect_port_image = (ImageView) view.findViewById( (R.id.connect_port_image) );
        connect_port_image.setImageResource( R.drawable.yellow_dot7x7 );
        ImageView connect_ip_image = (ImageView) view.findViewById( (R.id.connect_ip_image) );
        connect_ip_image.setImageResource( R.drawable.yellow_dot7x7 );
        TextView textname = (TextView) view.findViewById( R.id.row_name );
        TextView textipadr = (TextView) view.findViewById( R.id.row_ipadress );
        TextView textport = (TextView) view.findViewById( R.id.row_port );

        if(item.getPortOnline())connect_port_image.setImageResource( R.drawable.green_dot7x7 );
        else connect_port_image.setImageResource( R.drawable.red_dot7x7 );

        if(item.getIpOnline())connect_ip_image.setImageResource( R.drawable.green_dot7x7 );
        else connect_ip_image.setImageResource( R.drawable.red_dot7x7 );

        textname.setText( item.getName() );
        textipadr.setText( item.getIp_adr() );
        textport.setText( item.getPort() );
        return view;
    }


}


