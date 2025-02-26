package com.example.tcp_ip_client_2.classs;

import com.example.tcp_ip_client_2.interfaces.OnMessageReceivedListener;

// Класс, который будет использовать делегат
public class ResivedBufferDelegate {
    private OnMessageReceivedListener onMessageReceivedListener;

    // Установка делегата
    public void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
        this.onMessageReceivedListener = listener;
    }

    // Функция, которая вызывается при получении сообщения
    public void onMessageReceived(byte[] message) {
        if (onMessageReceivedListener != null) {
            onMessageReceivedListener.onMessageReceived(message);
        }
    }
}
