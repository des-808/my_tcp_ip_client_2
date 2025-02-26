package com.example.tcp_ip_client_2.classs;

public final class Swith_Terminal {

    private static boolean terminal = false;
    private static boolean nord = false;
    private static boolean lin = false;

    public static boolean isTerminal() {
        return terminal;
    }

    public static void setTerminal() {
        Swith_Terminal.terminal = true;
        Swith_Terminal.nord = false;
        Swith_Terminal.lin = false;
    }

    public static boolean isNord() {
        return nord;
    }

    public static void setNord() {
        Swith_Terminal.nord = true;
        Swith_Terminal.terminal = false;
        Swith_Terminal.lin = false;
    }

    public static boolean isLin() {
        return lin;
    }

    public static void setLin() {
        Swith_Terminal.lin = true;
        Swith_Terminal.terminal = false;
        Swith_Terminal.nord = false;
    }




}
