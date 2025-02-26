package com.example.tcp_ip_client_2.classs;

import android.util.Log;

import java.nio.ByteBuffer;

public class Parser {
        private final Device device;
        private float val = 0;
        private final static String TAG_PARSER = "PARSER";

    public Parser(Device device) {
            this.device = device;
        }
        public Device getBuffer_Ressive(byte[] buffer_Transmitte ,byte[] buffer){
            if(!isValidChecksum(buffer)){
                log("Чек Сумма не совпадает");
                return null;
            }else {
                if(!parseFromBytes(buffer_Transmitte,buffer))
                {
                    return null;
                }
            }
            return device;
        }
        private boolean isValidChecksum(byte[] buffer) {
            byte[] checksum = new byte[]{ buffer[buffer.length-1],buffer[buffer.length-2]};
            byte[] calculatedChecksum = crc16(buffer, buffer.length-2);
            if(calculatedChecksum[0] != checksum[0] || calculatedChecksum[1] != checksum[1]){return false;}
            return true;
        }
        public boolean parseFromBytes(byte[] buffer_Transmitte , byte[] bytes) {
            int HEADER_SIZE = 2;// Адрес + Функция
            int ERROR_SIZE = 1;// Байт описания ошибки от ведомого устройства
            int FOOTER_SIZE = 2;// Контрольная сумма

            if (bytes.length < HEADER_SIZE + ERROR_SIZE + FOOTER_SIZE) {
                log("Недостаточно данных для парсинга или выходит за пределы массива");
                return false;
            }
            if (bytes[0] != buffer_Transmitte[0]) {
                log("Неверный адрес");
                return false;
            }
            if ((bytes[1] != buffer_Transmitte[1])&&((bytes[1]&0x80)==0)) {
                log("Отправленая функция не совпадает с функцией, которая принята");
                return false;
            }
            if ((bytes[1] != buffer_Transmitte[1])&&((bytes[1]&0x80)!=0)) {
                log(errorFunction(bytes[2])+"\r\nНеверная функция");
                return false;
            }
            try{
                int length;
                byte[] data;
                byte[] reg_adr = new byte[]{buffer_Transmitte[2],buffer_Transmitte[3]};
                switch (bytes[1])
                {
                    case FUNCTION_READING_GROUP_OF_REGISTERS:
                        length = bytes[2];
                        for( int i = 0,sdvig = 3; i < length;){
                            if((reg_adr[1]==0x0C)||(reg_adr[1]==0x0E)||(reg_adr[1]==0x10)){
                                data = new byte[4];
                                data[0] = bytes[sdvig+i];
                                data[1] = bytes[sdvig+i+1];
                                data[2] = bytes[sdvig+i+2];
                                data[3] = bytes[sdvig+i+3];
                                i+=0x04;
                                val = byteArrayToFloat(bytes);
                                device.writeRegisterData(reg_adr,data);
                                reg_adr[1]+=0x02;
                            }else{
                                data = new byte[2];
                                data[0] = bytes[sdvig+i];
                                data[1] = bytes[sdvig+i+1];
                                device.writeRegisterData(reg_adr,data);
                                i+=0x02;
                                reg_adr[1]+=0x01;
                            }
                        }
                        //device.print();
                        return true;
                    case FUNCTION_SET_REGISTER:

                        if(       buffer_Transmitte[buffer_Transmitte.length-2]==bytes[bytes.length-2]
                                &&buffer_Transmitte[buffer_Transmitte.length-1]==bytes[bytes.length-1]){
                            System.out.println("FUNCTION_SET_REGISTER:\r\n Запись проведена успешно");}
                        else{System.out.println("FUNCTION_SET_REGISTER:\r\nЗапись проведена не успешно (ЧТО-То Пошло не так)");}
                        return true;
                    case FUNCTION_SET_GROUP_REGISTERS:
                        byte[] adr = new byte[]{bytes[2],bytes[3]};
                        byte[] kolReg = new byte[]{bytes[4],bytes[5]};
                        byte[] transmit_adr = new byte[]{buffer_Transmitte[2],buffer_Transmitte[3]};
                        byte[] transmit_kolReg = new byte[]{buffer_Transmitte[4],buffer_Transmitte[5]};
                        if( adr[0] == transmit_adr[0] && adr[1] == transmit_adr[1] && kolReg[0] == transmit_kolReg[0] && kolReg[1] == transmit_kolReg[1] ){
                            log("FUNCTION_SET_GROUP_REGISTERS:\r\n Запись проведена успешно");
                            return true;
                        }else{
                            log("FUNCTION_SET_GROUP_REGISTERS:\r\n Запись проведена не без успешно (ЧТО-То Пошло не так)");
                        }
                        return false;
                    default:break;
                }
                return false;
            } catch (Exception e) {
                log("Ошибка при обработке данных: "  );//+e.getMessage();
                return false;
            }
        }
        private float byteArrayToFloat(byte[] bytes)
        {//=  2.3478f
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            return byteBuffer.getFloat();
        }
        private byte[] floatToByteArray(float v)
        {//=  2.3478f
            byte[] w = new byte[4];
            byte[] b = ByteBuffer.allocate(4).putFloat(v).array();
            w[0] = b[0]; // - w[0] – LW – LB1 =91
            w[1] = b[1]; // - w[1] – LW – HB1 =66
            w[2] = b[2]; // - w[2] – HW – LB2 =22
            w[3] = b[3]; // - w[3] – HW – HB2 =64
            return w;
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

        private void log(String msg) {
            Log.d(TAG_PARSER,msg);
           // System.out.println(msg);
        }

        private final byte FUNCTION_READING_GROUP_OF_REGISTERS = 0x03;//Функция 03h – чтение группы регистров.
        private final byte FUNCTION_SET_REGISTER = 0x06;//Функция 06h – установка регистра.
        private final byte FUNCTION_SET_GROUP_REGISTERS = 0x10;//Функция 10h – установка группы регистров.
        /**TODO таблица 4 – коды ошибок.*/
        private final byte ILLEGAL_FUNCTION = 0x01;
        private final byte ILLEGAL_DATA_ADDRESS = 0x02;
        private final byte ILLEGAL_DATA_VALUE  = 0x03;
        private final byte SLAVE_DEVICE_FAILURE = 0x04;
        private final byte NEGATIVE_ACKNOWLEDGE = 0x07;

    public String errorFunction(byte err) {
        String ILLEGAL_FUNCTION_STRING = "ERROR_PARSER: Принятый код функции не может быть обработан на ведомом устройстве";
        String ILLEGAL_DATA_ADDRESS_STRING = "ERROR_PARSER: Адрес данных указанный в запросе не доступен данному ведомому";
        String ILLEGAL_DATA_VALUE_STRING = "ERROR_PARSER: Величина содержащаяся в поле данных запроса является не допустимой величиной для ведомого";
        String SLAVE_DEVICE_FAILURE_STRING = "ERROR_PARSER: Пока ведомый пытался выполнить затребованное действие произошла не восстанавливаемая ошибка";
        String NEGATIVE_ACKNOWLEDGE_STRING = "ERROR_PARSER: Ведомый не может выполнить программную функцию, принятую в запросе";
        String UNKNOWN_ERROR = "ERROR_PARSER: UNKNOWN_ERROR";
        switch (err){
                case ILLEGAL_FUNCTION:
                    return ILLEGAL_FUNCTION_STRING;
                case ILLEGAL_DATA_ADDRESS:
                    return ILLEGAL_DATA_ADDRESS_STRING;
                case ILLEGAL_DATA_VALUE:
                    return ILLEGAL_DATA_VALUE_STRING;
                case SLAVE_DEVICE_FAILURE:
                    return SLAVE_DEVICE_FAILURE_STRING;
                case NEGATIVE_ACKNOWLEDGE:
                    return NEGATIVE_ACKNOWLEDGE_STRING;
                default:
                    return UNKNOWN_ERROR;
            }
        }
    }
