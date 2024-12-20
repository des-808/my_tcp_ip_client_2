package com.example.tcp_ip_client_2.ui.preferences;

public enum Pages {
    PAGE1("Serial"),
    PAGE2("Terminal"),
    PAGE3("Receive"),
    PAGE4("Send"),
    PAGE5("Misc."),
    PAGE6("xz");

    private final String name;
    private  int id;

   /* public int getIdPage() {
        return id;
    }

    public void setIdPage(int id) {
        this.id = id;
    }*/


    Pages(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}

class PageId {
    private static int id;

    public static int getIdPage() {
        return id;
    }

    public static void setIdPage(int page) {
        id = page;
    }
}



