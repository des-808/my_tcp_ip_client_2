package com.example.tcp_ip_client_2.classs;

public class SearchPortsDropDownItem {

        private String ip;
        private String name;

        public SearchPortsDropDownItem(String ip, String name) {
            this.ip = ip;
            this.name = name;
        }

        public String getIp_adr() {
            return ip;
        }

        public String getName() {
            return name;
        }
}
