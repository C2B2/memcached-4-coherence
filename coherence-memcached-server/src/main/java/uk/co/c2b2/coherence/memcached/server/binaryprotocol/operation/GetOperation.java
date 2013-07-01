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

import com.google.gson.Gson;
import com.tangosol.net.NamedCache;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
class GetOperation implements MemCacheOperation {

    private final NamedCache cache;

    GetOperation(NamedCache cache) {
        this.cache = cache;

    }

    @Override
    public MemcacheResponse doOperation(MemcacheRequest request) {


        MemcachedBinaryHeader responseHeader = new MemcachedBinaryHeader();
        responseHeader.setOpCode(OpCode.GET);
        MemcacheResponse result = null;

        // decode the key
        int keyLength = request.getHeader().getKeyLength();
        byte keyBytes[] = request.getData();
        String key = new String(keyBytes, Charset.defaultCharset());


        Object objectEntry = cache.get(key);
        if (objectEntry != null && objectEntry instanceof CacheEntry) {
            long casRequest = request.getHeader().getCas();
            CacheEntry entry = (CacheEntry) objectEntry;
            
            // cas check
            if (casRequest == 0 || casRequest == entry.getCas())
            {
                responseHeader.setExtraLength((byte) 4);
                responseHeader.setCas(entry.getCas());
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream daos = new DataOutputStream(baos);
                try {
                    daos.writeInt(entry.getFlags());
                    
                    if (keyRequired) {
                        daos.write(keyBytes);
                        responseHeader.setKeyLength((short)keyLength);
                    }
                    daos.write(entry.getValue());
                    result = new MemcacheResponse(responseHeader, baos.toByteArray());
                } catch (IOException ioe) {
                // no way
                }
            } else {
                responseHeader.setStatus(ResponseStatus.KEY_NOT_FOUND.status);
                result = new MemcacheResponse(responseHeader, null);
            }

        } else if (objectEntry == null) {
            responseHeader.setStatus(ResponseStatus.KEY_NOT_FOUND.status);
            result =  new MemcacheResponse(responseHeader, null);
        } else { // not null or not a CacheEntry // try to JSON serialize
            responseHeader.setStatus(ResponseStatus.NO_ERROR.status);
                responseHeader.setExtraLength((byte) 4);
                responseHeader.setCas(0);
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream daos = new DataOutputStream(baos);
                try {
                    daos.writeInt(0);
                    
                    if (keyRequired) {
                        daos.write(keyBytes);
                        responseHeader.setKeyLength((short)keyLength);
                    }
                    
                    daos.write(serializeToJSON(objectEntry));
                    result = new MemcacheResponse(responseHeader, baos.toByteArray());
                } catch (IOException ioe) {
                // no way
                }
        }
        return result;
    }
    
    private byte[] serializeToJSON(Object object) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(object);
        return jsonStr.getBytes();
    }
    
    protected boolean keyRequired = false;
}
