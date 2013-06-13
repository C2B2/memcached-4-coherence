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
import java.nio.charset.Charset;
import uk.co.c2b2.memcached.server.CacheEntry;

/**
 *
 * @author steve
 */
class AddOperation implements MemCacheOperation {

    @Override
    public MemcacheResponse doOperation(NamedCache cache, MemcacheRequest request) {
        
        MemcachedBinaryHeader responseHeader = new MemcachedBinaryHeader();
        responseHeader.setOpCode(OpCode.ADD);
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
            if (cache.get(key) == null) {
                cache.put(key, new CacheEntry(flags, value, 1), lexpiry);
                responseHeader.setCas(1);
            } else {
                responseHeader.setStatus(ResponseStatus.KEY_EXISTS.status);
            }
        } catch (IOException ioe) {
            // now way it's a bis
        }
        // build response

        return new MemcacheResponse(responseHeader, null);
    }
}
