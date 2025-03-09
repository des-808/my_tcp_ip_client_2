package com.example.tcp_ip_client_2.classs;

import android.util.Log;

import java.nio.ByteBuffer;

public class Packet_To_BtMessage {
    public static final int dlinnaMassiva = 64;


    private byte[] buffer = new byte[133];
    private final byte startBitPacket = (byte)0x24;
    private byte lengthPacket = (byte)0x00;
    private byte commandBitPacket = (byte)0x00;
    private byte[] byteBuferr = new byte[dlinnaMassiva];
    private byte[] crc = new byte[2];
    //private final byte stopBitPacket = (byte)0x3B;

    final int START_BITS = 3;
    final int CRC_BITS = 2;
    private final int STOP_BIT = 1;

    public static byte data     = (byte)0x01;
    public  byte   startBit     = (byte)0x24;//0xF1
    public  byte   stopBit      = (byte)0x3B;//0xF3

    public byte[] getBuffer(){return buffer;}
    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }
    public byte getLengthPacket() {
        return lengthPacket;
    }
    public void setLengthPacket(byte lengthPacket) {
        this.lengthPacket = lengthPacket;
    }
    public byte getCommandBitPacket() {
        return commandBitPacket;
    }
    public void setCommandBitPacket(byte commandBitPacket) {
        this.commandBitPacket = commandBitPacket;
    }
    public byte[] getData() {
        return byteBuferr;
    }
    public void setData(byte[] data) {
        this.byteBuferr = data;
    }
    public void setMatchCrc(byte[] crc, int length) {
        this.crc=crc16(crc,length);
    }
    public byte[] getCrc() {
        return crc;
    }
    public void setCrc(byte[] crc) {
        this.crc = crc;
    }

    /*public byte[] bufferSetMessage(byte commandElseDataFlag, byte[] byteBufIn ) {
        byte[] bytebuf  = new byte[dlinnaMassiva];  // максимальная длинна 64 байта
        int dlinnaByteBuf = 0;//byteBufIn.length;
        for (int x = 0;x < 64; ++x){
            if ((byteBufIn[x] == 0)&&(byteBufIn[x+1] == 0)&&(byteBufIn[x+2] == 0)&&(byteBufIn[x+3] == 0)&&(byteBufIn[x+4] == 0)){
                dlinnaByteBuf=x;
                break;
            }
        }
        int dlinnaSoobsheniya = 3+dlinnaByteBuf+2+1;
        //Charset cset = Charset.forName("CP866");
        //String readMessage = new String(readBuf, cset);// построить строку из допустимых байтов в буфере в кодировке cp866
        bytebuf[0] = startBit;
        bytebuf[1] = (byte)dlinnaSoobsheniya;
        bytebuf[2] = commandElseDataFlag;
        int z = 3;
        int crc = 32;
        for(int i = 0; i<dlinnaByteBuf; ++i){
            bytebuf[z] = byteBufIn[i]  ;
            z++;
        }
        bytebuf[z] = (byte)crc;z++;
        bytebuf[z] = (byte)crc;z++;
        bytebuf[z] = stopBit;
        //z = 0;
        return bytebuf;
    }*/

    public byte[] bufferSetMessage(byte commandElseDataFlag, byte[] byteBufIn, int lengthByteBufIn ) {
        int dlinnaSoobsheniya = START_BITS+lengthByteBufIn+ (CRC_BITS+STOP_BIT);
        byte[] bytebuf  = new byte[dlinnaSoobsheniya];  // максимальная длинна 64 байта
        //Charset cset = Charset.forName("CP866");
        //String readMessage = new String(readBuf, cset);// построить строку из допустимых байтов в буфере в кодировке cp866
        bytebuf[0] = startBit;
        bytebuf[1] = (byte)dlinnaSoobsheniya;
        bytebuf[2] = commandElseDataFlag;
        int z = 3;
        for(int i = 0; i<lengthByteBufIn; ++i){
            bytebuf[z] = byteBufIn[i]  ;
            z++;
        }
        byte[] crc =  crc16(bytebuf,dlinnaSoobsheniya- (CRC_BITS+STOP_BIT));
        bytebuf[z] = crc[1];z++;
        bytebuf[z] = crc[0];z++;
        bytebuf[z] = stopBit;
        Log.d("BYTEBUF", bytesToHex( bytebuf));
        return bytebuf;
    }

    /*public byte[] bufferSetMessage(byte commandElseDataFlag, String stringBufIn) {
        byte[] bytebuf  = new byte[dlinnaMassiva];  // максимальная длинна 64 байта
        int dlinnaStroki = stringBufIn.length();
        int dlinnaSoobsheniya = 3+dlinnaStroki+2+1;
        //Charset cset = Charset.forName("CP866");
        bytebuf[0] = startBit;
        bytebuf[1] = (byte)dlinnaSoobsheniya;
        bytebuf[2] = commandElseDataFlag;
        int j = 0;
        int z = 3;
        byte[] buter = stringBufIn.getBytes();
        for(int i = 0;i<dlinnaStroki; ++i){
            bytebuf[z] = buter[j]  ;
            j++;
            z++;
        }
        byte[] crc16 = crc16(bytebuf, dlinnaSoobsheniya-3);
        bytebuf[z] = crc16[1];z++;
        bytebuf[z] = crc16[0];z++;
        bytebuf[z] = stopBit;z++;
        j = 0;z = 0;
        return bytebuf;
    }*/

    private byte[] matchCrc16(byte[] buf){
        return  crc16(buf, buf.length- (CRC_BITS+STOP_BIT));
    }
    private byte[] crc16(byte[] buf, int count) {
        int crc = 0xFFFF;
        for (int i = 0; i < count; i++) {
            crc ^= buf[i] & 0xFF;
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x0001) == 0) {
                    crc >>= 1;
                } else {
                    crc >>= 1;
                    crc ^= 0xA001;
                }
            }
        }
        return ByteBuffer.allocate(2).putShort((short) crc).array();
    }
    int lengthMessage = 0;
    byte[] bytesArrayMessage;
    public byte[] reseivedPacket(byte[] bytebuf , int dlinnaSoobsheniya){
        if(dlinnaSoobsheniya < 47) {return null;}
        setBuffer(bytebuf);
        setDlinnaSoobsheniya(dlinnaSoobsheniya);
        //Log.d("reseivedPacket->bytebuf",bytesToHex( bytebuf));
        lengthMessage = buffer[1];
        if (lengthMessage == dlinnaSoobsheniya) {
            setMatchCrc(buffer, lengthMessage-(CRC_BITS +STOP_BIT));
            Log.d("CRC",bytesToHex( crc));
            if (
                    (crc[0] == buffer[dlinnaSoobsheniya-3] && crc[1] == buffer[dlinnaSoobsheniya-2])
            ) {
                bytesArrayMessage = new byte[lengthMessage-(START_BITS+ CRC_BITS +STOP_BIT)];
                for (int i = 3, j = 0; i < lengthMessage- (CRC_BITS+STOP_BIT); i++,j++) {
                    bytesArrayMessage[j] = buffer[i];
                }
            }
        }
        return bytesArrayMessage;
    }

    private void setDlinnaSoobsheniya(int dlinnaSoobsheniya) {
        this.lengthPacket = (byte) ((byte)dlinnaSoobsheniya&0xff);
    }

    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    private String bytesToHex(byte[] buf) {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

}
