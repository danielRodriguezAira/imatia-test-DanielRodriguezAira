package com.imatia.statemachine.shared;
public interface Result<T, E> {
    T getData();
    void setData(T data);
    E getError();
}
