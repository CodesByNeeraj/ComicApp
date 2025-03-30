package com.example.norman_lee.comicapp.utils;
//this class enables safe sharing of data between background thread and main thread and this stores ur bitmaps
public class Container<T> {
    T value;

    public Container() {
        this.value = null;
    }

    public void set(T t) {
        this.value = t;
    }

    public T get() {
        return this.value;
    }
}