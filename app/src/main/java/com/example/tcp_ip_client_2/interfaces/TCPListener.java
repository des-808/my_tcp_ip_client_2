package com.example.tcp_ip_client_2.interfaces;

public interface TCPListener {
	public void onTCPMessageRecieved(String message);
	public void onTCPConnectionStatusChanged(boolean isConnectedNow);

   public void onTCPMessageRecievedInt(Integer inMsgInt);
   public void onTCPMessageRecievedChar(char inMsgChar);


    void onTCPMessageRecievedByteBuffer(char[] inMsgByteBuffer, int count, int len);
}
