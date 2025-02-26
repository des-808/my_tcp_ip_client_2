package com.example.tcp_ip_client_2.classs;

public class SearchPortOnlinePortItems {
    private String name;
    private String ipadr;
    private String port;
    private int    ID;
    private boolean isPortOnline;

    public SearchPortOnlinePortItems(String ipadr, String port){
        this.ipadr = ipadr;
        this.port =  port;
    }

    public SearchPortOnlinePortItems(String port, boolean isPortOnline){
        this.port =  port;
        this.isPortOnline = isPortOnline;
    }

    public SearchPortOnlinePortItems(int ID, String name, String ipadr, String port){
        this.ID =  ID;
        this.name = name;
        this.ipadr = ipadr;
        this.port =  port;
    }
    public SearchPortOnlinePortItems() {}

    public String getIp_adr() {
        return ipadr;
    }
    public String getPort()  { return port; }
    public int    getID()        { return ID; }
    public void setIp_adr(String val)  {ipadr = val;}
    public void setPort(String val)   {port = val;}
    public void setID(int   val)         { ID = val; }
    

    public void setPortOnline(boolean b) {
        isPortOnline=b;
    }
    public String getPortOnline() {
        if(isPortOnline)return "Online";
        return "Offline";
    }
    public boolean isPortOnline() {
        return isPortOnline;
    }
}
