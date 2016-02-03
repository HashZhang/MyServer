package com.sf.hash.mycat.backend.sqlengine;

/**
 * Created by 862911 on 2016/2/2.
 */
public interface SQLQueryResultListener<T> {
    public void onResult(T result);
}
