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
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.MemcacheRequest;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.MemcacheResponse;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.operation.MemCacheOperation;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.operation.OperationFactory;

public class MemcacheRequestToResponseDecoder extends OneToOneDecoder {

    private final OperationFactory operationFactory;

    public MemcacheRequestToResponseDecoder(OperationFactory operationFactory) {
        this.operationFactory = operationFactory;
    }

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        MemcacheRequest request = (MemcacheRequest)msg;

        MemCacheOperation operation = operationFactory.createOperation(request.getHeader().getOpCode());
        MemcacheResponse response = operation.doOperation(request);
        // always copy back the opaque
        response.getHeader().setOpaque(request.getHeader().getOpaque());

        return response;
    }


}
