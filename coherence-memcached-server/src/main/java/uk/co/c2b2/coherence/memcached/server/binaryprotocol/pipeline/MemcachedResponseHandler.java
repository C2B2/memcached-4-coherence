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

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.MemcacheResponse;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.OpCode;

/**
 *
 * @author steve
 */
public class MemcachedResponseHandler extends SimpleChannelUpstreamHandler {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        MemcacheResponse response = (MemcacheResponse) e.getMessage();

        write(ctx.getChannel(), response);
    }

    private void write(Channel channel, MemcacheResponse response) {

        // check if we have a quit
        if (response.getHeader().getOpCode() == OpCode.QUIT || response.getHeader().getOpCode() == OpCode.QUITQ) {
            try {
                // wait to write the quit response then close the channel
                if (!response.isDiscard()) {
                    Channels.write(channel, response).await();
                }
                Channels.close(channel);
            } catch (InterruptedException ex) {
                Channels.close(channel);
            }
        } else {
            if (!response.isDiscard()) {
                Channels.write(channel, response);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        super.exceptionCaught(ctx, e); //To change body of generated methods, choose Tools | Templates.
    }
}
