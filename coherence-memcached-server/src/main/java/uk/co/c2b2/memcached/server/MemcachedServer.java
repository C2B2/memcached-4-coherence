/*
* C2B2, The Leading Independent Middleware Experts.
* Copyright 2013, C2B2 Consulting Limited.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package uk.co.c2b2.memcached.server;

import uk.co.c2b2.coherence.memcached.server.binaryprotocol.operation.OperationFactory;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.pipeline.MemcachedPipelineFactory;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * This class provided a memcached interface to a Coherence Cache. 
 * It can be used embedded in an existing cache or within a storage disabled member
 * to provide remote memcached access to a Coherence cache
 * @author steve
 */
public class MemcachedServer {
    
    /**
     * Runs a memcached server as a standalone process
     * @param args 
     */
    public static void main(String args[]) {
        if (args.length != 2 ) {
            System.err.println("Usage: <port> <namedcache>");
        }
        
        String namedCache = args[1];
        int port = Integer.parseInt(args[0]);
        
        MemcachedServer server = new MemcachedServer(port, namedCache);
        server.bootStrap();
    }
    
    public MemcachedServer(int port, NamedCache cache) {
        myPort = port;
        myNamedCache = cache.getCacheName();       
        myCache = cache;
    }
    
    public MemcachedServer(NamedCache cache) {
        myPort = 11211;
        myNamedCache = cache.getCacheName();       
        myCache = cache;
    }
    
    public MemcachedServer(String namedCache) {
        myPort = 11211;
        myNamedCache = namedCache;
    }
    
    public MemcachedServer(int port, String namedCache) {
        myPort = port;
        myNamedCache = namedCache;
    }
    
    public void bootStrap() {
        if (myCache == null) {
            bootStrapCache();
        }
        bootStrapSocketServer(); 
        if (myLogger.isLoggable(Level.INFO))
          myLogger.info("Bootstrapped Memcached server on port " + myPort + " backed by named cache " + myNamedCache);
    }

    public void shutdown() {
        channelFactory.releaseExternalResources();
        CacheFactory.shutdown();
    }
    
    public NamedCache getCache() {
        return myCache;
    }
    
    void bootStrapCache() {
        CacheFactory.ensureCluster();
        myCache = CacheFactory.getCache(myNamedCache);        
    }
    
    void bootStrapSocketServer() {
        OperationFactory operationFactory = new OperationFactory(myCache);

        channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);
        bootstrap.setPipelineFactory(new MemcachedPipelineFactory(operationFactory));
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.bind(new InetSocketAddress(myPort));
    }

    private Logger myLogger = Logger.getLogger(this.getClass().getName());
    private int myPort;
    private String myNamedCache;
    private NamedCache myCache;

    private ChannelFactory channelFactory;
}
