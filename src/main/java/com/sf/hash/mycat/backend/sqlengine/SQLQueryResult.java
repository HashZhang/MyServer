package com.sf.hash.mycat.backend.sqlengine;

/**
 * Created by 862911 on 2016/2/2.
 */
public class SQLQueryResult<T> {
    //用来生成sql查询的结果
    private final T result;
    private final boolean success;
    private final String dataNode;	// dataNode or database name
    private String tableName;
    //可能存在没有dataNode的情况
    public SQLQueryResult(T result, boolean success) {
        super();
        this.result = result;
        this.success = success;
        this.dataNode = null;
    }

    public SQLQueryResult(T result, boolean success, String dataNode) {
        this.result = result;
        this.success = success;
        this.dataNode = dataNode;
    }

    public T getResult() {
        return result;
    }
    public boolean isSuccess() {
        return success;
    }
    public String getDataNode() {
        return dataNode;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
