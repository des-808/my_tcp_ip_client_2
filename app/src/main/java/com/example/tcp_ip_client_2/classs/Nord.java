package com.example.tcp_ip_client_2.classs;

import android.util.Log;

import com.example.tcp_ip_client_2.ui.bluetooth.Constants;

import java.io.IOException;

public class Nord {


    public static final byte startInd1 = (byte)0xFE;//-2;//0xFE;дежурный режим клавиатуры
    public static final byte startInd2 = (byte)0xF9;//-7;//0xF9;режим настроек прибора
    //    public static final byte stopIndStartPrib = (byte)0x00;//
//    public static final byte stopInd1 = (byte)0x2E;
//    public static final byte stopInd2 = (byte)0x0C;//когда всё в норме
//    public static final byte stopInd3 = (byte)0x04;//снятие с охраны кодом техника когда нету неисправности
//    public static final byte stopInd4 = (byte)0x06;//снятие с охраны кодом техника когда есть неисправность
//    public static final byte stopInd5 = (byte)0x02;//снятие с охраны кодом техника когда есть неисправность при откл 220 и откл акб. появляется при предпрогр епром
//    public static final byte stopInd6 = (byte)0x0E;//при откл акб
//    public static final byte stopInd7 = (byte)0x08;//при откл 220
//    public static final byte stopInd8 = (byte)0x0A;//при откл 220 и откл акб
//    public static final byte stopInd9 =(byte)0xF8;//-8;//0xF8;
    public  byte   startBitPacket     = (byte)0x24;//$ //0xF1
    public  byte   stopBitPacket      = (byte)0x3B;//; //0xF3
//    public  byte   startBitPacket     = (byte)0xF1;//0x24
//    public  byte   stopBitPacket      = (byte)0xF3;//0x3B

    int sdvig = 0;
    boolean startbit = true;


    byte[] bufferNord = new byte[1024];
    public byte[] getBuffer(){return bufferNord;}

    public boolean ressiveBuffer(byte[] buffer,int bytesArray) {
        for (int i = 0; i < bytesArray; i++) {
            if (startbit) {
                if ((buffer[i] == startInd1) || (buffer[i] == startInd2)) {
                    bufferNord[i+sdvig] = buffer[i];
                    sdvig++;
                    startbit = false;
                }
            } else {

                if ((buffer[i] == 40)) {//((buffer[bytesArray] == stopInd)) &&
                    //bufferNord = buffer;
                    sdvig = 0;
                    startbit = true;
                    return true;
                } else bufferNord[i+sdvig] = buffer[i];
            }
        }
        return false;

        /*if (Swith_Terminal.isTerminal()) {
            // Read from the InputStream
     //       buffer[bytesArray] = (byte) mmInStream.read();
            // Send the obtained bytes to the UI Activity
            if (startbit) {
                if (buffer[bytesArray] == startBitPacket) {
                    bytesArray = 1;
                    startbit = false;
                }
            } else {
                if (buffer[bytesArray] == stopBitPacket) {
                    bytesArray = 0;
                    startbit = true;
       //             mHandler.obtainMessage(Constants.MESSAGE_READ, bytesArray, -1, buffer).sendToTarget();
                } else bytesArray++;

            }

        }*/

        /*if (Swith_Terminal.isLin()) {

            // Read from the InputStream
       //     buffer[bytesArray] = (byte) mmInStream.read();
            // Send the obtained bytes to the UI Activity
            if (startbit) {
                if (buffer[bytesArray] == startBitPacket) {
                    bytesArray = 1;
                    startbit = false;
                }
            } else {
                if (buffer[bytesArray] == stopBitPacket) {
                    bytesArray = 0;
                    startbit = true;
        //            mHandler.obtainMessage(Constants.MESSAGE_READ, bytesArray, -1, buffer).sendToTarget();
                } else bytesArray++;

            }


        }*/
    }


}
