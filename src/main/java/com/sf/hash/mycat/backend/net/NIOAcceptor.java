package com.sf.hash.mycat.backend.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

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

    public NIOAcceptor(int port, ServerSocketChannel serverChannel, NIOReactorPool reactorPool) throws IOException {
        this.port = port;
        this.selector = Selector.open();
        this.serverChannel = serverChannel;
        this.reactorPool = reactorPool;
    }

    @Override
    public void run() {

    }
}
