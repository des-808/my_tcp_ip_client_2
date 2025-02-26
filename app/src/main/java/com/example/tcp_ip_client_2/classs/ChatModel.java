package com.example.tcp_ip_client_2.classs;

public class ChatModel {


    private long id;
    private long position;
    //private String table_name = "";
    private String message_in = "";
    private String message_time = "";
    private String message_out = "";
    private boolean outgoing; // true = outgoing, false = incoming
    private boolean clicked=false;

    public ChatModel() { }

    public ChatModel(boolean outgoing) {
        this.outgoing = outgoing;
    }

    public long getPosition() {
        return position;
    }
    public boolean setClicked(boolean click){
        if(click&&!clicked){clicked=true;}
        this.clicked = click;
        return clicked;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public ChatModel(long position) {
        this.position = position;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public ChatModel(int id, boolean outgoing, String  messageIn, String timeMessage, String messageOut) {
        this.id = id;
        this.outgoing = outgoing;
        this.message_in = messageIn;
        this.message_time = timeMessage;
        this.message_out = messageOut;
    }

    public ChatModel(String message, String timeMessage, boolean outgoing) {
        this.outgoing = outgoing;
        if (outgoing) {
            this.message_out = message;
            this.message_time = timeMessage;
            //this.outgoing = outgoing;//true;
        }else {
            this.message_in = message;
            this.message_time = timeMessage;
            //this.outgoing = outgoing;//false;
        }
    }

    public ChatModel(int id, String message, String timeMessage, boolean outgoing) {
        this.id = id;
        this.outgoing = outgoing;
        if (outgoing) {
            this.message_out = message;
            this.message_time = timeMessage;
            //his.outgoing = outgoing;//true;
        }else {
            this.message_in = message;
            this.message_time = timeMessage;
            //this.outgoing = outgoing;//false;
        }
    }


    public String getMessage_in() {
        return message_in;
    }
    public void setMessage_in(String msg_in) {
        message_in = msg_in;
    }
    public String getMessage_time() {
        return message_time;
    }
    public void setMessage_time(String data_in_msg) {
        message_time = data_in_msg;
    }
    public String getMessage_out() {
        return message_out;
    }
    public void setMessage_out(String msg_out) {
            message_out = msg_out;
        }

    public void setOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }
    public boolean isOutgoing() {return outgoing;}
    public boolean getOutgoing() {return outgoing;}


}


///////////////////////////////////////////////////