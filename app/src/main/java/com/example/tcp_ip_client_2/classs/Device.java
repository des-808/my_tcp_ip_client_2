package com.example.tcp_ip_client_2.classs;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Device  {
    private final List<Register> registers;
    private static final String DEVICE_PRINT = "Device print";
    public Device() {
        registers = new ArrayList<Register>();
    }
    public void addRegister(byte[] register_adr, byte[] value, String description) {
        registers.add(new Register(register_adr, value, description));
    }
    public byte[] readAddrRegister(int register_adress) {
        return new byte[]{(byte) (register_adress >> (8)), (byte) (register_adress >> (0))};
    }
    public byte[] readRegisterData(int register_adress) {
        byte[] register_adr = new byte[]{(byte) (register_adress >> (8)), (byte) (register_adress >> (0))};
        for (Register register : registers) {
            byte[]  reg_adr = register.getAddress();
            if ( reg_adr[0] == register_adr[0] && reg_adr[1] == register_adr[1] )  {
                return register.getValue();
            }
        }
        throw new IllegalArgumentException("Register with address " + register_adr + " not found");
    }
    public void writeRegisterData(byte[] register_adr, byte[] value) {
        for (Register register : registers) {
            byte[]  reg_adr = register.getAddress();
            if ( reg_adr[0] == register_adr[0] && reg_adr[1] == register_adr[1] )  {
                register.setValue(value);
                return;
            }
        }
        throw new IllegalArgumentException("Register with address " + register_adr + " not found");
    }
    public void writeRegisterData(int register_adr, int value) {
        for (Register register : registers) {
            byte[]  reg_adr = register.getAddress();
            if (reg_adr[1] == register_adr )  {
                byte[] valueint = new byte[]{(byte) (value >> (8)), (byte) (value >> (0))};
                register.setValue(valueint);
                return;
            }
        }
        throw new IllegalArgumentException("Register with address " + register_adr + " not found");
    }
    public void print() {
        for (Register register : registers) {
            byte[] value = register.getValue();
            byte[] reg_adr = register.getAddress();
            StringBuilder sb = new StringBuilder();
            StringBuilder sb_adr = new StringBuilder();
            for (int i = 0; i < value.length; i++) {
                if (i < value.length - 1) {
                    sb.append(String.format("0x%02x,", value[i]));
                } else {
                    sb.append(String.format("0x%02x", value[i]));
                }
            }
            for (int i = 0; i < reg_adr.length; i++) {
                if (i < reg_adr.length - 1) {
                    sb_adr.append(String.format("0x%02x,", reg_adr[i]));
                } else {
                    sb_adr.append(String.format("0x%02x", reg_adr[i]));
                }
            }
            String printString = ("Регистр:"+ sb_adr +" Значение:"+ sb +" description:"+register.getDescription()+"\n");//String printString = ("Регистр:"+sb_adr.toString()+" Значение:"+sb.toString()+" description:"+register.getDescription()+"\n");
            Log.d(DEVICE_PRINT, printString);
        }
    }

    private static class Register{
        private byte[] register_adr;
        private byte[] value;
        private String description;
        public Register(byte[] register_adr, byte[] value, String description) {
            this.register_adr = register_adr;
            this.value = value;
            this.description = description;
        }
        public byte[] getAddress() {
            return this.register_adr;
        }
        public byte[] getValue() {
            return this.value;
        }
        public void setValue(byte[] value) {
            this.value = value;
        }
        public String getDescription() {
            return this.description;
        }
    }
}
