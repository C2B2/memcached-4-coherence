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

import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import org.junit.Before;
import uk.co.c2b2.memcached.server.MemcachedServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

public abstract class AbstractIntegrationTest {

    private int DEFAULT_PORT = 10000;
    private String NAMED_CACHE = "myCache";

    protected MemcachedClient client;

    @Before
    public void setUp() throws Exception {
        startMemcachedServer();
        createClient();
    }

    protected void createClient() throws IOException {
        BinaryConnectionFactory connectionFactory = new BinaryConnectionFactory();
        client = new MemcachedClient(connectionFactory, Arrays.asList(new InetSocketAddress("localhost", getPort())));
    }

    protected int getPort() {
        return DEFAULT_PORT;
    }

    protected String getCacheName() {
        return NAMED_CACHE;
    }

    protected void startMemcachedServer() {
        MemcachedServer server = new MemcachedServer(getPort(), getCacheName());
        server.bootStrap();
    }
}
