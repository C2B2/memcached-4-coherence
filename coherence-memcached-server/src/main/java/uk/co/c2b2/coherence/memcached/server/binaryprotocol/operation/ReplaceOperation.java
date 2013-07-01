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
package uk.co.c2b2.coherence.memcached.server.binaryprotocol.operation;

import com.tangosol.net.NamedCache;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import uk.co.c2b2.coherence.memcached.server.binaryprotocol.MemcacheRequest;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.MemcacheResponse;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.MemcachedBinaryHeader;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.OpCode;
import uk.co.c2b2.memcached.server.CacheEntry;

/**
 *
 * @author steve
 */
class ReplaceOperation implements MemCacheOperation {

    private final NamedCache cache;

    ReplaceOperation(NamedCache cache) {
        this.cache = cache;
    }

    @Override
    public MemcacheResponse doOperation(MemcacheRequest request) {

        MemcachedBinaryHeader responseHeader = new MemcachedBinaryHeader();
        responseHeader.setOpCode(OpCode.REPLACE);
        try {
            MemcachedBinaryHeader header = request.getHeader();
            ByteArrayInputStream bis = new ByteArrayInputStream(request.getData());
            DataInputStream dis = new DataInputStream(bis);
            int flags = dis.readInt();
            int expiry = dis.readInt();
            byte keyArray[] = new byte[header.getKeyLength()];
            dis.read(keyArray);
            String key = new String(keyArray, Charset.defaultCharset());
            byte value[] = new byte[header.getBodyLength() - header.getExtraLength() - header.getKeyLength()];
            dis.read(value);

            long lexpiry = expiry * 1000;
            Object cacheObject = cache.get(key);
            if (cacheObject != null) {
                // cas check
                long requestCas = request.getHeader().getCas();
                if (cacheObject instanceof CacheEntry) {
                    CacheEntry entry = (CacheEntry) cacheObject;
                    if (requestCas == 0 || requestCas == entry.getCas()) {
                        long newCas = entry.getCas() + 1;
                        cache.put(key, new CacheEntry(flags, value, newCas), lexpiry);
                        responseHeader.setCas(newCas);
                    } else {
                        responseHeader.setStatus(ResponseStatus.KEY_EXISTS.status);
                    }
                } else {
                    responseHeader.setStatus(ResponseStatus.KEY_EXISTS.status);
                }
            } else {
                responseHeader.setStatus(ResponseStatus.KEY_NOT_FOUND.status);
            }
        } catch (IOException ioe) {
            // now way it's a bis
        }
        // build response

        return new MemcacheResponse(responseHeader, null);

    }
}
