package com.sf.hash.zk.client;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 862911 on 2016/3/21.
 */
public abstract class ZKClient {
    protected static final String connnectHost =
            "10.202.44.206:2181,10.202.44.206:2182,10.202.44.206:2183,10.202.44.206:2184,10.202.44.206:2185";
    protected static final Logger LOGGER = Logger.getLogger(ZKClient.class);
    protected ZooKeeper zooKeeper = null;

    protected AtomicInteger count = new AtomicInteger(0);
    protected int num;

    private CountDownLatch connectLatch = new CountDownLatch(1);

    public ZKClient(){
        num = count.addAndGet(1);
    }

    public ZooKeeper connect(){
        try {
            zooKeeper = new ZooKeeper(connnectHost, 15000, ConnectedWatcher);
            if(ZooKeeper.States.CONNECTING == zooKeeper.getState()){
                try {
                    connectLatch.await();
                } catch (InterruptedException e) {
                    LOGGER.error("Error happened while awaiting!");
                }
            }
        } catch (IOException e) {
            LOGGER.error("ZK configuration is wrong!");
        }
        return zooKeeper;
    }

    private Watcher ConnectedWatcher = new Watcher() {
        public void process(WatchedEvent event) {
            if (event.getState() == Event.KeeperState.SyncConnected) {
                connectLatch.countDown();
            }
        }
    };

    public void closeAndClear(){
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            LOGGER.error("Encountering an error when closing ZK "+num);
        } finally{
            count.decrementAndGet();
        }
    }
}
