package com.example.tcp_ip_client_2.ui.bluetooth;

public class adapter_listview {

    private String textOnline;
    private String textName;
    private String textMac;
    private int    ID;
    private int    imageOnline;

    public adapter_listview(String textonline,String textname,String textmac){
        this.textOnline =  textonline;
        this.textName = textname;
        this.textMac =  textmac;
    }

    public adapter_listview() { }
    public int getImageOnline() { return imageOnline; }
    public String getTextOnline()  { return textOnline; }
    public String getTextName() { return textName; }
    public String getTextMac()  { return textMac; }
    public int    getID()        { return ID; }

    public void setTextOnline(String val)   {textOnline = val;}
    public void setTextName(String val)  {textName = val;}
    public void setTextMac(String val)   {textMac = val;}

    public void setID(int   val)         { ID = val; }


}