package com.example.tcp_ip_client_2.classs;



import static com.example.tcp_ip_client_2.ui.bluetooth.KeyboardFragment.bytesToHex;

import android.util.Log;

import java.nio.charset.Charset;

public class Nord {
    public Packet_To_BtMessage packet_To_BtMessage = new Packet_To_BtMessage();

    public static final byte startInd1 = (byte)0xFE;//-2;//0xFE;дежурный режим клавиатуры
    public static final byte startInd2 = (byte)0xF9;//-7;//0xF9;режим настроек прибора
    public static final byte stopInd0 = (byte)0x00;//Старт прибора
    public static final byte stopInd1 = (byte)0x2E;
    public static final byte stopInd2 = (byte)0x0C;//когда всё в норме
    public static final byte stopInd3 = (byte)0x04;//снятие с охраны кодом техника когда нету неисправности
    public static final byte stopInd4 = (byte)0x06;//снятие с охраны кодом техника когда есть неисправность
    public static final byte stopInd5 = (byte)0x02;//снятие с охраны кодом техника когда есть неисправность при откл 220 и откл акб. появляется при предпрогр епром
    public static final byte stopInd6 = (byte)0x0E;//при откл акб
    public static final byte stopInd7 = (byte)0x08;//при откл 220
    public static final byte stopInd8 = (byte)0x0A;//при откл 220 и откл акб
    public static final byte stopInd9 =(byte)0xF8;//-8;//0xF8;
    public  byte   startBitPacket     = (byte)0x24;//$ //0xF1
    public  byte   stopBitPacket      = (byte)0x3B;//; //0xF3
//    public  byte   startBitPacket     = (byte)0xF1;//0x24
//    public  byte   stopBitPacket      = (byte)0xF3;//0x3B
    Charset cset = Charset.forName("CP866");
    private StringBuilder sb = new StringBuilder();
    boolean startbit = true;
    private byte[] buffer_packet;
    public byte[] getBuffer_packet(){return buffer_packet;}
    public void setBuffer_packet(byte[] buffer, int bytesArray){buffer_packet = new byte[bytesArray]; buffer_packet = buffer;}
    private byte simvolSicurity      ;
    private byte simvolKaretki       ;
    private byte simvolOblPost       ;
    private byte simvolNeispravnosti ;
    private String keyboardStr1;
    private String keyboardStr2;
    private String keyboardStr3;


    public String getKeyboardStr3() {return keyboardStr3;}
    public String getKeyboardStr2() {return keyboardStr2;}
    public String getKeyboardStr1() {return keyboardStr1;}

    private byte[] crc = new byte[2];

    public byte[] getCrc() {
        return crc;
    }
    public byte[] crcCalculator(byte[] buffer, int bufferLength){
        packet_To_BtMessage.setMatchCrc(buffer,bufferLength);
        crc = packet_To_BtMessage.getCrc() ;
        return crc;
    }



    public boolean ressiveBuffer(byte[] buffer, int bytesArray) {
        Log.d("PACKET",bytesToHex( buffer));
        setBuffer_packet(buffer, bytesArray);
        buffer_packet = packet_To_BtMessage.reseivedPacket(buffer,bytesArray);
        if(buffer_packet==null){return false;}
        parsePacket();
        return true;
    }
    public boolean parsePacket(){

            simvolSicurity = buffer_packet[33];
            simvolKaretki = buffer_packet[34];
            simvolOblPost = buffer_packet[39];
            simvolNeispravnosti = buffer_packet[40];

            //text1               = new String(bufferNord,5,12,cset);


        return true;
    }

    public byte getSimvolSicurity() {
        return simvolSicurity;
    }

    public void setSimvolSicurity(byte simvolSicurity) {
        this.simvolSicurity = simvolSicurity;
    }

    public byte getSimvolKaretki() {
        return simvolKaretki;
    }

    public void setSimvolKaretki(byte simvolKaretki) {
        this.simvolKaretki = simvolKaretki;
    }

    public byte getSimvolOblPost() {
        return simvolOblPost;
    }

    public void setSimvolOblPost(byte simvolOblPost) {
        this.simvolOblPost = simvolOblPost;
    }

    public byte getSimvolNeispravnosti() {
        return simvolNeispravnosti;
    }

    public void setSimvolNeispravnosti(byte simvolNeispravnosti) {
        this.simvolNeispravnosti = simvolNeispravnosti;
    }
}

