package com.example.tcp_ip_client_2.classs;

import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MipI{
    public Device device = new Device();
    public byte[] buffer_Transmitte;
    private byte[] address;
    //private float val = 0;
    public Parser parser = new Parser(device);
    static MessageSender messageSender = new MessageSender();

    public MipI(){
        initDefaultMipRegisters();
        address = device.readRegisterData(0x01);
    }

    public byte[] getAddress() {
        return address;
    }

    public void setAddress(byte[] address) {
        this.address = address;
    }
    public void setAddress(int address) {
        this.address[0] = (byte) (address >> (8));
        this.address[1] = (byte) (address >> (0));
    }

    public byte[] searchAddressDevice(int addr){
        //01 03 00 00 00 01 84 0A
        byte[] byte_address = new byte[2];
        byte_address[0] = (byte) (addr >> (8));
        byte_address[1] = (byte) (addr >> (0));
        byte[] registerAdr = new byte[]{0x00, 0x00};
        byte[] byte_data = new byte[]{0x00, 0x01};
        return buffer_Transmitte = messageSender.sendMessageWord(byte_address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr,byte_data );
    }
    public byte[] sendReadTypeDevice(){
        address = device.readRegisterData(0x01);
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, new byte[]{0x00, 0x00}, new byte[]{0x00, 0x01});
    }
    public byte[] sendReadAddress(){
        address = device.readRegisterData(0x01);
        byte[] byte_data = new byte[]{0x00, 0x01};//регистр 0x01
        byte[] registerAdr = new byte[]{0x00, 0x01};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr, byte_data);
    }
    public byte[] sendWriteAddress(int old_address, int new_address){
        // 01 06 00 01 00 0B 99 CD
        byte[] byte_new_address = new byte[2];
        byte_new_address[0] = (byte) (new_address >> (8));
        byte_new_address[1] = (byte) (new_address >> (0));
        byte[] adr = new byte[]{(byte) (old_address >> (8)), (byte) (old_address >> (0))};
        byte[] registerAdr = new byte[]{0x00, 0x01};
        return buffer_Transmitte = messageSender.sendMessageWord(adr[1], FUNCTION_SET_REGISTER, registerAdr, byte_new_address);
    }
    public byte[] sendWriteBoudRate(int boudrate){
        address = device.readRegisterData(0x01);
        byte[] byte_boudrate = new byte[2];
        byte_boudrate[0] = (byte) 0x00;
        switch(boudrate){
            case 1:  byte_boudrate[1] = 0x01;break;  /*1200: */
            case 2:  byte_boudrate[1] = 0x02;break;  /*2400: */
            case 3:  byte_boudrate[1] = 0x03;break;  /*4800: */
            case 4:  byte_boudrate[1] = 0x04;break;  /*9600: */
            case 5: byte_boudrate[1] = 0x05;break;  /*14400:*/
            case 6: byte_boudrate[1] = 0x06;break;  /*19200: */
            default: byte_boudrate[1] = 0x04;break;
        }
        byte[] registerAdr = new byte[]{0x00, 0x02};
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_SET_REGISTER, registerAdr, byte_boudrate);
    }

    public byte[] sendReadBoudRate(){
        address = device.readRegisterData(0x01);
        byte[] registerAdr = new byte[]{0x00, 0x02}; //регистр 0x02
        byte[] byte_boudrate = new byte[]{0x00,0x01};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr, byte_boudrate);
    }
    public byte[] sendReadZoneStatus(int zone){
        address = device.readRegisterData(0x01);
        zone+=0x02;//смещение по адресам
        byte[] byte_zone = new byte[2];
        byte_zone[0] = (byte) (zone >> (8));
        byte_zone[1] = (byte) (zone >> (0));
        byte[] registerAdr = new byte[]{byte_zone[0], byte_zone[1]};//регистр 0x03,0x04,0x05
        byte[] byte_data = new byte[]{0x00, 0x01};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr,byte_data );
    }
    public byte[] sendReadUpsStatus(int inputUps){//статус источника питания по входам 1, 2
        address = device.readRegisterData(0x01);
        inputUps+=5;//смещение по адресам
        byte[] byte_inputUps = new byte[2];
        byte_inputUps[0] = (byte) (inputUps >> (8));
        byte_inputUps[1] = (byte) (inputUps >> (0));
        byte[] registerAdr = new byte[]{byte_inputUps[0], byte_inputUps[1]};//статус источника питания по входам 1, 2
        byte[] byte_data = new byte[]{0x00, 0x01};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr,byte_data );
    }
    public byte[] sendReadCableLengthInAlarm(int zone){
        address = device.readRegisterData(0x01);
        zone+=7;//смещение по адресам
        byte[] byte_zone = new byte[2];
        byte_zone[0] = (byte) (zone >> (8));
        byte_zone[1] = (byte) (zone >> (0));
        byte[] registerAdr = new byte[]{byte_zone[0], byte_zone[1]};//длина кабеля ШС1..3 в режиме ТРЕВОГА
        byte[] byte_data = new byte[]{0x00, 0x01};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr,byte_data );
    }
    public byte[] sendReadRelayOutputStatusNORMAL_ALARM(){
        address = device.readRegisterData(0x01);
        byte[] registerAdr = new byte[]{0x00, 0x0b};//статус релейных выходов НОРМА/ТРЕВОГА 0x0b
        byte[] byte_data = new byte[]{0x00, 0x01};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr,byte_data );
    }
    public byte[] sendWriteZonePogonResitance(int shs, float resistance){
        address = device.readRegisterData(0x01);
        //01 10 00 0C 00 02 04 3F 1E B8 52 6C 15//шс1 0.620 Oм
        //01 10 00 0E 00 02 04 3F 1E B8 52 ED CC//шс2 0.620 Oм
        //01 10 00 10 00 02 04 3F 1E B8 52 6D 4C//шс3 0.620 Oм
        byte[] registerAdr = new byte[]{0x00,0x00};
        switch(shs){
            case 1:
                registerAdr[1] = 0x0C;
                break;
            case 2:
                registerAdr[1] =  0x0E;
                break;
            case 3:
                registerAdr[1] =  0x10;
                break;
        }
        return buffer_Transmitte = messageSender.sendMessageFloat(address[1],FUNCTION_SET_GROUP_REGISTERS,registerAdr,resistance);
    }
    public byte[] sendReadZonePogonResitance(int shs){
        address = device.readRegisterData(0x01);
        //01 10 00 0C 00 02 04 3F 1E B8 52 6C 15//шс1 0.620 Oм
        //01 10 00 0E 00 02 04 3F 1E B8 52 ED CC//шс2 0.620 Oм
        //01 10 00 10 00 02 04 3F 1E B8 52 6D 4C//шс3 0.620 Oм
        byte[] registerAdr = new byte[]{0x00,0x00};
        switch(shs){
            case 1:
                registerAdr[1] = 0x0C;
                break;
            case 2:
                registerAdr[1] =  0x0E;
                break;
            case 3:
                registerAdr[1] =  0x10;
                break;
        }
        return buffer_Transmitte = messageSender.sendMessageFloat(address[1],FUNCTION_READING_GROUP_OF_REGISTERS,registerAdr,0x0002);
    }
    public byte[] sendReadOperatingModeRelayOutputsNORMAL(int rele){
        address = device.readRegisterData(0x01);
        byte[] registerAdr = new byte[]{0x00,0x00};
        switch(rele){
            case 1:
                registerAdr[1] = 0x12;
                break;
            case 2:
                registerAdr[1] =  0x13;
                break;
            case 3:
                registerAdr[1] =  0x14;
                break;
        }
        byte[] byte_data = new byte[]{0x00, 0x01};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1],FUNCTION_READING_GROUP_OF_REGISTERS,registerAdr,byte_data);
    }
    /**TODO
     * ncOrNo = true/false
     * true = Разомкнуто
     * false = Замкнуто
     * */
    public byte[] sendWriteOperatingModeRelayOutputsNORMAL(int rele,boolean ncOrNo){
        address = device.readRegisterData(0x01);
        byte[] byte_ncOrNo = new byte[2];byte_ncOrNo[0] = (byte) 0x00;
        if(ncOrNo){byte_ncOrNo[1] = (byte) 0xFF;}
        else{byte_ncOrNo[1] = (byte) 0x00;}

        byte[] registerAdr = new byte[2];
        registerAdr[0] = 0x00;
        switch(rele){
            case 1:
                registerAdr[1] = 0x12;
                break;
            case 2:
                registerAdr[1] = 0x13;
                break;
            case 3:
                registerAdr[1] = 0x14;
                break;
            default:
                registerAdr[1] = 0x00;
                break;
        }
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_SET_REGISTER, registerAdr, byte_ncOrNo);
    }
    public byte[] sendReadRecordingAlarmsNotices(){//фиксация тревожных извещений
        address = device.readRegisterData(0x01);
        byte[] registerAdr = new byte[]{0x00, 0x15};//регистр 0x0015
        byte[] byte_data = new byte[]{0x00, 0x01};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr,byte_data );
    }
    public byte[] sendWriteSoundResetAlarm(){//сброс звуковой сигнализации
        address = device.readRegisterData(0x01);
        byte[] registerAdr = new byte[]{0x00, 0x16};//регистр 0x0016
        byte[] byte_data = new byte[]{(byte)0xA5, 0x5A};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_SET_REGISTER, registerAdr,byte_data );
    }
    public byte[] sendWriteSoundResetAlarmAll(){//сброс звуковой сигнализации
        address = device.readRegisterData(0x01);
        byte[] registerAdr = new byte[]{0x00, 0x00};//регистр 0x0000
        byte[] byte_data = new byte[]{(byte)0xA5, 0x5A};//1 байт
        byte adr = 0x00;
        return buffer_Transmitte = messageSender.sendMessageWord(adr, FUNCTION_SET_REGISTER, registerAdr,byte_data );
    }
    public byte[] sendWriteAlarmResetNotices(){//сброс тревожных извещений
        address = device.readRegisterData(0x01);
        byte[] registerAdr = new byte[]{0x00, 0x17};//регистр 0x0017
        byte[] byte_data = new byte[]{(byte)0xAA, 0x55};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_SET_REGISTER, registerAdr,byte_data );
    }
    public byte[] sendWriteRegistrationOfLengthThermalCableAlarmCondition(boolean tmp){
        address = device.readRegisterData(0x01);
        byte[] byte_tmp = new byte[2];byte_tmp[0] = (byte) 0x00;
        if(tmp){byte_tmp[1] = (byte) 0xFF;}
        else{byte_tmp[1] = (byte) 0x00;}
        byte[] registerAdr = new byte[]{0x00, 0x18};
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_SET_REGISTER, registerAdr, byte_tmp);
    }
    public byte[] sendReadRegistrationOfLengthThermalCableAlarmCondition(){
        address = device.readRegisterData(0x01);
        byte[] byte_tmp = new byte[]{0x00, 0x01};//1 байт
        byte[] registerAdr = new byte[]{0x00, 0x18};
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr, byte_tmp);
    }
    public byte[] sendWriteZoneLength(int shs, int length){
        address = device.readRegisterData(0x01);
        //01 06 00 19 00 0A D8 0A//шс1 10метров
        //01 06 00 1A 00 0A 28 0A//шс2 10метров
        //01 06 00 1B 00 0A 79 CA//шс3 10метров
        byte[] registerAdr = new byte[2];
        registerAdr[0] = 0x00;
        switch(shs){
            case 1:
                registerAdr[1] = 0x19;
                break;
            case 2:
                registerAdr[1] =  0x1A;
                break;
            case 3:
                registerAdr[1] =  0x1B;
                break;
        }
        byte[] byte_length = new byte[2];
        byte_length[0] = (byte) (length >> (8));
        byte_length[1] = (byte) (length >> (0));
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_SET_REGISTER, registerAdr, byte_length);
    }
    public byte[] sendReadZoneLength(int shs){
        address = device.readRegisterData(0x01);
        //01 06 00 19 00 0A D8 0A//шс1 10метров
        //01 06 00 1A 00 0A 28 0A//шс2 10метров
        //01 06 00 1B 00 0A 79 CA//шс3 10метров
        byte[] registerAdr = new byte[2];
        registerAdr[0] = 0x00;
        switch(shs){
            case 1:
                registerAdr[1] = 0x19;
                break;
            case 2:
                registerAdr[1] =  0x1A;
                break;
            case 3:
                registerAdr[1] =  0x1B;
                break;
        }
        byte[] byte_length = new byte[]{0x00,0x01};//1 байт
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr, byte_length);
    }
    /**TODO
     * temp_compensation = true/false
     * true = термокомпенсация включена
     * false = термокомпенсация выключена
     * */
    public byte[] sendWriteTemperatureCompensation(boolean temp_compensation){
        address = device.readRegisterData(0x01);
        byte[] byte_temp_comp = new byte[2];byte_temp_comp[0] = (byte) 0x00;
        if(temp_compensation){byte_temp_comp[1] = (byte) 0xFF;}
        else{byte_temp_comp[1] = (byte) 0x00;}
        byte[] registerAdr = new byte[]{0x00, 0x1C};
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_SET_REGISTER, registerAdr, byte_temp_comp);
    }
    public byte[] sendReadTemperatureCompensation(){
        address = device.readRegisterData(0x01);
        byte[] byte_temp_comp = new byte[]{0x00, 0x01};
        byte[] registerAdr = new byte[]{0x00, 0x1C};
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr, byte_temp_comp);
    }

    public byte[] sendWriteYahontPui(boolean tmp){
        address = device.readRegisterData(0x01);
        byte[] byte_tmp = new byte[2];byte_tmp[0] = (byte) 0x00;
        if(tmp){byte_tmp[1] = (byte) 0xFF;}
        else{byte_tmp[1] = (byte) 0x00;}
        byte[] registerAdr = new byte[]{0x00, 0x1D};
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_SET_REGISTER, registerAdr, byte_tmp);
    }
    public byte[] sendReadYahontPui(){
        address = device.readRegisterData(0x01);
        byte[] byte_temp_comp = new byte[]{0x00, 0x01};
        byte[] registerAdr = new byte[]{0x00, 0x1D};
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr, byte_temp_comp);
    }
    public byte[] sendWriteCalibrationRegisterMeasuringPathModule(int tmp){
        address = device.readRegisterData(0x01);
        byte[] byte_tmp = new byte[2];
        byte_tmp[0] = (byte) (tmp >> (8));
        byte_tmp[1] = (byte) (tmp >> (0));
        byte[] registerAdr = new byte[]{0x00, 0x50};
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_SET_REGISTER, registerAdr, byte_tmp);
    }
    public byte[] sendReadCalibrationCoefficients(int shs){
        address = device.readRegisterData(0x01);
        byte[] registerAdr = new byte[]{0x00,0x00};
        switch(shs){
            case 1:
                registerAdr[1] = 0x51;
                break;
            case 2:
                registerAdr[1] =  0x53;
                break;
            case 3:
                registerAdr[1] =  0x55;
                break;
        }
        byte[] byte_length = new byte[]{0x00,0x02};
        return buffer_Transmitte = messageSender.sendMessageWord(address[1],FUNCTION_READING_GROUP_OF_REGISTERS,registerAdr,byte_length);
    }

    public byte[] sendReadAllParametrs(){
        address = device.readRegisterData(0x01);
        byte[] byte_all_parametrs = new byte[]{0x00, 0x1E};
        byte[] registerAdr = new byte[]{0x00, 0x00};
        return buffer_Transmitte = messageSender.sendMessageWord(address[1], FUNCTION_READING_GROUP_OF_REGISTERS, registerAdr, byte_all_parametrs);
    }
    /**TODO таблица 1*/
    private final byte FLOAT = 4;// байта -1e-37 . . . 1e+37 вещественное с плавающей точкой
    private final byte WORD = 2;// байта 0 . . . 65535 беззнаковое целое
    /**TODO таблица 3 – коды функций.*/
    private final byte FUNCTION_READING_GROUP_OF_REGISTERS = 0x03;//Функция 03h – чтение группы регистров.
    private final byte FUNCTION_SET_REGISTER = 0x06;//Функция 06h – установка регистра.
    private final byte FUNCTION_SET_GROUP_REGISTERS = 0x10;//Функция 10h – установка группы регистров.
    /*public byte[] addrRegister(int register_adress) {
        byte[] register_adr = new byte[]{(byte) (register_adress >> (8)), (byte) (register_adress >> (0))};
        return register_adr;
    }*/
//01 06 00 83 41 97 1D 4A;мип 2
//01 06 00 00 00 14 9D 53
    public String print(byte[] buffer)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buffer.length; i++) {
            if (i < buffer.length - 1) {
                sb.append(String.format("%02x ", buffer[i]));
            } else {
                sb.append(String.format("%02x", buffer[i]));
            }
        }
        Log.d("MipI print", sb.toString());
        //System.out.println(sb.toString());
        return sb.toString();
    }
    /*{
    byte[] floatBuffer = floatToByteArray(2.3478f);
    print(floatBuffer);
        System.out.println(byteArrayToFloat(floatBuffer));
    }*/
    public void initDefaultMipRegisters()
    {
        device.addRegister(new byte[]{0x00,0x00},new byte[]{(byte)0x00,(byte)0x14},"Дескриптор устройства");
        device.addRegister(new byte[]{0x00,0x01},new byte[]{(byte)0x00,(byte)0xF7},"Адресс устройства");
        device.addRegister(new byte[]{0x00,0x02},new byte[]{(byte)0x00,(byte)0x04},"Скорость передачи");
        device.addRegister(new byte[]{0x00,0x03},new byte[]{(byte)0x00,(byte)0x13},"Статус шлейфа ШС1");
        device.addRegister(new byte[]{0x00,0x04},new byte[]{(byte)0x00,(byte)0x23},"Статус шлейфа ШС2");
        device.addRegister(new byte[]{0x00,0x05},new byte[]{(byte)0x00,(byte)0x30},"Статус шлейфа ШС3");
        device.addRegister(new byte[]{0x00,0x06},new byte[]{(byte)0x00,(byte)0x43},"Статус источника питания по входам 1");
        device.addRegister(new byte[]{0x00,0x07},new byte[]{(byte)0x00,(byte)0x53},"Статус источника питания по входам 2");
        device.addRegister(new byte[]{0x00,0x08},new byte[]{(byte)0xFF,(byte)0xFF},"Длина кабеля ШС1 в режиме ТРЕВОГА");
        device.addRegister(new byte[]{0x00,0x09},new byte[]{(byte)0xFF,(byte)0xFF},"Длина кабеля ШС2 в режиме ТРЕВОГА");
        device.addRegister(new byte[]{0x00,0x0A},new byte[]{(byte)0xFF,(byte)0xFF},"Длина кабеля ШС3 в режиме ТРЕВОГА");
        device.addRegister(new byte[]{0x00,0x0B},new byte[]{(byte)0x00,(byte)0x03},"Статус релейных выходов НОРМА/ТРЕВОГА");
        device.addRegister(new byte[]{0x00,0x0C},new byte[]{(byte)0x3F,(byte)0x1E,(byte)0xB8,(byte)0x52},"Погонное сопротивление кабеля : ШС1  float");
        device.addRegister(new byte[]{0x00,0x0E},new byte[]{(byte)0x3F,(byte)0x1E,(byte)0xB9,(byte)0x53},"Погонное сопротивление кабеля : ШС2  float");
        device.addRegister(new byte[]{0x00,0x10},new byte[]{(byte)0x3F,(byte)0x1E,(byte)0xBA,(byte)0x54},"Погонное сопротивление кабеля : ШС3  float");
        device.addRegister(new byte[]{0x00,0x12},new byte[]{(byte)0x00,(byte)0x00},"Режим работы релейных выходов НОРМА1");
        device.addRegister(new byte[]{0x00,0x13},new byte[]{(byte)0x00,(byte)0x00},"Режим работы релейных выходов НОРМА2");
        device.addRegister(new byte[]{0x00,0x14},new byte[]{(byte)0x00,(byte)0x00},"Режим работы релейных выходов НОРМА3");
        device.addRegister(new byte[]{0x00,0x15},new byte[]{(byte)0x00,(byte)0x00},"Фиксация тревожных извещений");
        device.addRegister(new byte[]{0x00,0x16},new byte[]{(byte)0x00,(byte)0x00},"Сброс звуковой сигнализации");
        device.addRegister(new byte[]{0x00,0x17},new byte[]{(byte)0x00,(byte)0x00},"Сброс тревожных извещений");
        device.addRegister(new byte[]{0x00,0x18},new byte[]{(byte)0x00,(byte)0x00},"Фиксация индикации длины термокабеля в режиме ТРЕВОГА");
        device.addRegister(new byte[]{0x00,0x19},new byte[]{(byte)0x00,(byte)0x00},"Длина кабеля ШС1");
        device.addRegister(new byte[]{0x00,0x1A},new byte[]{(byte)0x00,(byte)0x00},"Длина кабеля ШС2");
        device.addRegister(new byte[]{0x00,0x1B},new byte[]{(byte)0x00,(byte)0x00},"Длина кабеля ШС3");
        device.addRegister(new byte[]{0x00,0x1C},new byte[]{(byte)0x00,(byte)0xFF},"Термокомпенсация");
        device.addRegister(new byte[]{0x00,0x1D},new byte[]{(byte)0x00,(byte)0x00},"Работа с ЯХОНТ-ПУИ");
    }
}
//////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////






