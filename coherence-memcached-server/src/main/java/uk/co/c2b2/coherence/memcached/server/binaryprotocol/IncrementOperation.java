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
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import uk.co.c2b2.memcached.server.CacheEntry;

/**
 *
 * @author steve
 */
class IncrementOperation implements MemCacheOperation {

    @Override
    public MemcacheResponse doOperation(NamedCache cache, MemcacheRequest request) {
        long cas = 1;
        MemcachedBinaryHeader responseHeader = new MemcachedBinaryHeader();
        byte returnArray[] = null;

        try {
        MemcachedBinaryHeader header = request.getHeader();
        ByteArrayInputStream bis = new ByteArrayInputStream(request.getData());
        DataInputStream dis = new DataInputStream(bis);
        long delta = dis.readLong();
        long initial = dis.readLong();
        long returnVal = initial;
        int expiration = dis.readInt();
        byte keyArray[] = new byte[header.getKeyLength()];
        dis.read(keyArray);
        String key = new String(keyArray, Charset.defaultCharset());

        Object object = cache.get(key);
        if (object != null && object instanceof CacheEntry) {
            CacheEntry entry = (CacheEntry)object;
            cas = entry.getCas();
            if (request.getHeader().getCas() == cas || request.getHeader().getCas() == 0) {
                cas++;
                long counter = ByteBuffer.wrap(entry.getValue()).getLong();
                counter += delta;
                returnVal = counter;
                ByteBuffer buff = ByteBuffer.allocate(8);
                buff.putLong(counter);
                returnArray = buff.array();
                cache.put(key, new CacheEntry(header.getOpaque(),buff.array(),cas), expiration);
                responseHeader.setStatus(ResponseStatus.NO_ERROR.status);
            } else {
                responseHeader.setStatus(ResponseStatus.KEY_EXISTS.status);
                cas = 0;
            }
        } else if (object == null) {
            if (expiration != 0xffffffff) {
                ByteBuffer buff = ByteBuffer.allocate(8);
                buff.putLong(initial);
                cache.put(key, new CacheEntry(header.getOpaque(),buff.array(),cas), expiration);
                responseHeader.setStatus(ResponseStatus.NO_ERROR.status);
                returnArray = buff.array();
            } else {
                responseHeader.setStatus(ResponseStatus.KEY_NOT_FOUND.status);
            }
        }


        } catch (IOException ioe) {
            // now way it's a bis
        }        
        // build response
        responseHeader.setOpCode(OpCode.INCREMENT);
        responseHeader.setCas(cas);

        return new MemcacheResponse(responseHeader, returnArray);

    }


    
}
