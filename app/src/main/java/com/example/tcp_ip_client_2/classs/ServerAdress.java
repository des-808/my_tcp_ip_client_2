package com.example.tcp_ip_client_2.classs;

public class ServerAdress {
    private static  String name;
    private static  String ip;
    private static  String port;

    public static String getPort() {
        return port;
    }
    public static int getPortParseInt() {
        int portParseInt = -1;
        try{portParseInt = Integer.parseInt(port);}
        catch (Exception ignored) {}
        return portParseInt;
    }


    public static  void setPort(String port) {
        ServerAdress.port = port;
    }

    public static  String getIp() {
        return ip;
    }

    public static  void setIp(String ip) {
        ServerAdress.ip = ip;
    }

    public static  String getName() {
        return name;
    }

    public static  void setName(String name) {
        ServerAdress.name = name;
    }

    public ServerAdress(String name, String ip, String port) {
        ServerAdress.name = name;
        ServerAdress.ip = ip;
        ServerAdress.port = port;
    }


}
