package com.example.tcp_ip_client_2.interfaces;

// Определение интерфейса делегата
public interface OnMessageReceivedListener {
    void onMessageReceived(byte[] message);
}
