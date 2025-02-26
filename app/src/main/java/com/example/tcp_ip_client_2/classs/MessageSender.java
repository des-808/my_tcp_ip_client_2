package com.example.tcp_ip_client_2.classs;

import java.nio.ByteBuffer;

public class MessageSender {
        public MessageSender() {}
        private byte[] floatToByteArray(float v) {//=  2.3478f
            byte[] w = new byte[4];
            byte[] b = ByteBuffer.allocate(4).putFloat(v).array();
            w[0] = b[0]; // - w[0] – LW – LB1 =91
            w[1] = b[1]; // - w[1] – LW – HB1 =66
            w[2] = b[2]; // - w[2] – HW – LB2 =22
            w[3] = b[3]; // - w[3] – HW – HB2 =64
            return w;
        }
        public byte[] sendMessageFloat(byte address, byte function, byte[] registers_adr, float value){
            byte[] bufferOut = new byte[13];
            byte[] data = floatToByteArray(value);
            byte[] crc16;

            bufferOut[0] = address;
            bufferOut[1] = function;
            System.arraycopy(registers_adr, 0, bufferOut, 2, 2);
            //bufferOut[2] = registers_adr[0];
            //bufferOut[3] = registers_adr[1];
            bufferOut[4] = 0x00;//количество регистров старший байт
            bufferOut[5] = 0x02;//количество регистров младий байт
            bufferOut[6] = (byte) data.length;//количество байт в data
            System.arraycopy(data, 0, bufferOut, 7, 4);
            /*bufferOut[7] = data[0];
            bufferOut[8] = data[1];
            bufferOut[9] = data[2];
            bufferOut[10] = data[3];*/
            crc16 = matchCrc16(bufferOut);
            bufferOut[11] = crc16[1];
            bufferOut[12] = crc16[0];
            return bufferOut;
        }
        public byte[] sendMessageWord(byte address, byte function, byte[] registers_adr, byte[] data){
            byte[] bufferOut = new byte[8];
            bufferOut[0] = address;
            bufferOut[1] = function;
            System.arraycopy(registers_adr, 0, bufferOut, 2, 2);
            System.arraycopy(data, 0, bufferOut, 4, 2);
            /*bufferOut[2] = registers_adr[0];
            bufferOut[3] = registers_adr[1];
            bufferOut[4] = data[0];
            bufferOut[5] = data[1];*/
            byte[] crc16 = matchCrc16(bufferOut);
            bufferOut[6] = crc16[1];
            bufferOut[7] = crc16[0];
            return bufferOut;
        }

        private byte[] matchCrc16(byte[] buf){
            return  crc16(buf, buf.length-2);
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
    }
