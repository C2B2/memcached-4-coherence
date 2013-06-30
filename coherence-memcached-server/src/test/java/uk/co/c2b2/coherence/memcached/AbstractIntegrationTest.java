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
package uk.co.c2b2.coherence.memcached;

import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import uk.co.c2b2.memcached.server.MemcachedServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import org.junit.BeforeClass;


public abstract class AbstractIntegrationTest {

    private static int DEFAULT_PORT = 10000;
    private static String NAMED_CACHE = "myCache";
    private static int TIMEOUT = 10000;

    protected static MemcachedClient client;

    @BeforeClass
    static public void setUp() throws Exception {
        if (client == null) {
            startMemcachedServer();
            createClient();
        }
    }

    static protected void createClient() throws IOException {
        ConnectionFactory connectionFactory = new ConnectionFactoryBuilder().setOpTimeout(TIMEOUT).setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).build();
        client = new MemcachedClient(connectionFactory, Arrays.asList(new InetSocketAddress("localhost", getPort())));
    }

    static protected int getPort() {
        return DEFAULT_PORT;
    }

    static protected String getCacheName() {
        return NAMED_CACHE;
    }

    static protected void startMemcachedServer() {
        MemcachedServer server = new MemcachedServer(getPort(), getCacheName());
        server.bootStrap();
    }
}
