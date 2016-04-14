
package com.toughegg.andytools.http.method;

/**
 * 整个框架异常的基类
 */
@SuppressWarnings("serial")
public class SKYunHttpException extends Exception {
    public final NetworkResponse networkResponse;

    public SKYunHttpException() {
        networkResponse = null;
    }

    public SKYunHttpException(NetworkResponse response) {
        networkResponse = response;
    }

    public SKYunHttpException(String exceptionMessage) {
        super(exceptionMessage);
        networkResponse = null;
    }

    public SKYunHttpException(String exceptionMessage, NetworkResponse response) {
        super(exceptionMessage);
        networkResponse = response;
    }

    public SKYunHttpException(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
        networkResponse = null;
    }

    public SKYunHttpException(Throwable cause) {
        super(cause);
        networkResponse = null;
    }
}
