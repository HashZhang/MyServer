package com.sf.hash.mycat.backend.heartbeat.detector;

import com.sf.hash.mycat.backend.sqlengine.SQLQueryResult;
import com.sf.hash.mycat.backend.sqlengine.SQLQueryResultListener;

import java.util.Map;

/**
 * Created by 862911 on 2016/2/3.
 */
public class MySQLDetector implements SQLQueryResultListener<SQLQueryResult<Map<String,String>>> {



    public void onResult(SQLQueryResult<Map<String, String>> result) {

    }
}
