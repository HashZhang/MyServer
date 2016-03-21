package com.sf.hash.mycat.backend.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

/**
 * Created by 862911 on 2016/2/3.
 */
public class NIOAcceptor extends Thread{
    private static final Logger LOGGER = LoggerFactory.getLogger(NIOAcceptor.class);
    private final int port;
    private final Selector selector;
    private final ServerSocketChannel serverChannel;
    private final NIOReactorPool reactorPool;

    private long acceptCount;

    public NIOAcceptor(String name, String bindIp,int port, NIOReactorPool reactorPool) throws IOException {
        super.setName(name);
        this.port = port;
        this.selector = Selector.open();
        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.configureBlocking(false);
        /** 设置TCP属性 */
        this.serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        this.serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 1024 * 16 * 2);
        // backlog=100
        this.serverChannel.bind(new InetSocketAddress(bindIp, port), 100);
        this.serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        this.reactorPool = reactorPool;
    }

    @Override
    public void run() {
        final Selector selector = this.selector;
        while(true){
            try {
                selector.select(500L);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for(SelectionKey selectionKey:selectionKeys){
                    if(selectionKey.isValid()&&selectionKey.isAcceptable()){
//                        accept();
                    }
                }
            } catch (IOException e) {
                LOGGER.warn(getName(),e);
            }
        }
    }
}
