package com.example.tcp_ip_client_2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tcp_ip_client_2.R;
import com.example.tcp_ip_client_2.classs.SearchPortsDropDownItem;

import java.util.List;

public class SearchPortsItemAdapter extends ArrayAdapter<SearchPortsDropDownItem> {
        public SearchPortsItemAdapter(Context context, List<SearchPortsDropDownItem> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SearchPortsDropDownItem item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_search_ports_item, parent, false);
            }
            TextView search_port_item_ip_adress = (TextView) convertView.findViewById(R.id.search_port_item_ip_adress);
            search_port_item_ip_adress.setText(item.getIp_adr());
            TextView search_port_item_name = (TextView) convertView.findViewById(R.id.search_port_item_name);
            search_port_item_name.setText(item.getName());
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            SearchPortsDropDownItem item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_search_ports_item, parent, false);
            }
            TextView search_port_item_ip_adress = (TextView) convertView.findViewById(R.id.search_port_item_ip_adress);
            search_port_item_ip_adress.setText(item.getIp_adr());
            TextView search_port_item_name = (TextView) convertView.findViewById(R.id.search_port_item_name);
            search_port_item_name.setText(item.getName());
            return convertView;
        }
    }



