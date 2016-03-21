package com.sf.hash.zk.client;

import org.apache.zookeeper.*;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by 862911 on 2016/3/21.
 */
public class Worker extends ZKClient implements Runnable {
    ConcurrentLinkedQueue<String> tasks = new ConcurrentLinkedQueue<String>();

    public Worker(){
        super();
        connect();
        LOGGER.info("Connection complete!");
        createWorker();
    }

    private void createWorker() {
        zooKeeper.create("/worker-" + num, ("/worker-" + num).getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,createWorkerCB,"/worker-" + num);
    }

    private AsyncCallback.StringCallback createWorkerCB = new AsyncCallback.StringCallback() {
        public void processResult(int rc, String path, Object ctx, String name) {
            switch(KeeperException.Code.get(rc)){
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK:
                    LOGGER.info("Worker node created!");
                    break;
                case NODEEXISTS:
                    LOGGER.error("Worker node exists!");
                    break;
                default:
                    LOGGER.error("Something is really wrong!");
            }
        }
    };

    public void getTasks() {
        zooKeeper.getChildren("/worker-" + num, tasksWatcher, getTasksCB, "/worker-" + num);
    }

    private Watcher tasksWatcher = new Watcher() {
        public void process(WatchedEvent event) {
            if (event.getPath().equals("/worker-" + num) && event.getType() == Event.EventType.NodeChildrenChanged) {
                getTasks();
            }
        }
    };

    private AsyncCallback.ChildrenCallback getTasksCB = new AsyncCallback.ChildrenCallback() {
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK:
                    reassignTasks(children);
                    break;
                case NONODE:
                    LOGGER.debug("Worker node not exists!");
                    break;
                default:
                    LOGGER.error("Get Task Children Failed!");
            }
        }
    };

    private void reassignTasks(List<String> children) {
        for (String task : tasks) {
            if (!children.contains(task)) {
                tasks.remove(task);
            }
        }
        for (String task : children) {
            if (!tasks.contains(task)) {
                tasks.add(task);
            }
        }
    }

    public void run() {
        //getTasks();
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Worker()).start();
        Thread.sleep(100000);
    }
}
