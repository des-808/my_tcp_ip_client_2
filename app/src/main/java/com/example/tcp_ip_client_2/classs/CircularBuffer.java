package com.example.tcp_ip_client_2.classs;

public class CircularBuffer {
    private byte[] buffer;
    private int head;
    private int tail;
    private int size;

    public CircularBuffer(int size) {
        this.buffer = new byte[size];
        this.head = 0;
        this.tail = 0;
        this.size = size;
    }

    public void write(byte data) {
        buffer[tail] = data;
        tail = (tail + 1) % size;
        if (tail == head) {
            head = (head + 1) % size;
        }
    }

    public byte read() {
        if (head == tail) {
            return -1; // буфер пуст
        }
        byte data = buffer[head];
        head = (head + 1) % size;
        return data;
    }
}
/*TODO
 * Класс CircularBuffer представляет собой циклический буфер/очередь.
 * В этом примере, когда мы записываем данные в буфер, мы сначала
 * проверяем, не переполнен ли буфер. Если это так, мы перемещаем
 * указатель головы вперед, чтобы освободить место для новых
 * данных. Затем мы записываем данные в позицию, на которую
 * указывает указатель хвоста, и перемещаем указатель хвоста вперед.
 * Когда мы читаем данные из буфера, мы сначала проверяем, не
 * пуст ли буфер. Если это так, мы возвращаем -1. В противном случае
 * мы читаем данные из позиции, на которую указывает указатель
 * головы, и перемещаем указатель головы вперед.
*/