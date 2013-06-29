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
package uk.co.c2b2.coherence.memcached.server.binaryprotocol.pipeline;

import com.tangosol.net.NamedCache;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.MemcacheRequest;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.MemcachedBinaryHeader;

/**
 *
 * @author steve
 */
public class MemcachedBinaryProtocolDecoder extends FrameDecoder {
    
    private static final int HEADER_SIZE = 24;
    
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        // check we have the 
        if (buffer.readableBytes() < HEADER_SIZE) {
            return null;
        }

        MemcachedBinaryHeader header = new MemcachedBinaryHeader(buffer);
        
        // ok we've read the header now wait for the body length
        if (buffer.readableBytes() < header.getBodyLength()) {
            return null;
        }
        byte data[] = new byte[header.getBodyLength()];
        buffer.readBytes(data);

        return new MemcacheRequest(header, data);

    }

}
