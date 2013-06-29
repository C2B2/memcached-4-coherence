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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.MemcacheResponse;

/**
 *
 * @author steve
 */
public class MemcachedBinaryProtocolEncoder extends OneToOneEncoder {

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
         
        if (msg instanceof MemcacheResponse) {
            MemcacheResponse response = (MemcacheResponse)msg;
            ChannelBuffer responseBuffer = ChannelBuffers.dynamicBuffer();
            response.writeToBuffer(responseBuffer);        
            return responseBuffer;
        } else {
            return msg;
        }
    }

}
