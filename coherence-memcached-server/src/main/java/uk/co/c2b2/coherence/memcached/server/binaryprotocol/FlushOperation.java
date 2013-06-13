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
package uk.co.c2b2.coherence.memcached.server.binaryprotocol;

import com.tangosol.net.NamedCache;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Operation to flush the cache either immediately or after a delay
 *
 * @author steve
 */
class FlushOperation implements MemCacheOperation {

    @Override
    public MemcacheResponse doOperation(final NamedCache cache, MemcacheRequest request) {
        MemcachedBinaryHeader header = new MemcachedBinaryHeader();
        header.setOpCode(OpCode.FLUSH);
        int expiry = 0;
        if (4 == request.getHeader().getExtraLength()) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(request.getData());
                DataInputStream dis = new DataInputStream(bais);
                expiry = dis.readInt();

            } catch (IOException ex) {
                Logger.getLogger(FlushOperation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (expiry == 0) {
            cache.clear();
        } else {
            executorService.schedule(new Runnable() {

                @Override
                public void run() {
                    cache.clear();
                }
            }, expiry, TimeUnit.SECONDS);
        }

        MemcacheResponse result = new MemcacheResponse(header, null);
        result.getHeader().setStatus(ResponseStatus.NO_ERROR.status);
        return result;

    }
    
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
}
