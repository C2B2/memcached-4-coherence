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
class SetOperation implements MemCacheOperation {

    private final NamedCache cache;

    SetOperation(NamedCache cache) {
        this.cache = cache;
    }

    @Override
    public MemcacheResponse doOperation(MemcacheRequest request) {
        long cas = 1;
        MemcachedBinaryHeader responseHeader = new MemcachedBinaryHeader();

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
        Object object = cache.get(key);
        if (object != null && object instanceof CacheEntry) {
            CacheEntry entry = (CacheEntry)object;
            cas = entry.getCas();
            if (request.getHeader().getCas() == cas || request.getHeader().getCas() == 0) {
                cas++;
                cache.put(key, new CacheEntry(flags,value,cas), lexpiry);
                responseHeader.setStatus(ResponseStatus.NO_ERROR.status);
            } else {
                responseHeader.setStatus(ResponseStatus.KEY_EXISTS.status);
                cas = 0;
            }
        } else if (object == null) {
            cache.put(key, new CacheEntry(flags,value,cas), lexpiry);
        }


        } catch (IOException ioe) {
            // now way it's a bis
        }        
        // build response
        responseHeader.setOpCode(OpCode.SET);
        responseHeader.setCas(cas);

        return new MemcacheResponse(responseHeader, null);

    }
    
}
