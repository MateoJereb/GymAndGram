package com.isaiajereb.gymandgram.persistencia;

public interface OnResult<T> {
    void onSuccess(final T result);
    void onError(final Throwable exception);
}
