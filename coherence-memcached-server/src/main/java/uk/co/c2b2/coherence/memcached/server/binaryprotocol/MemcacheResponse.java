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

import org.jboss.netty.buffer.ChannelBuffer;

/**
 *
 * @author steve
 */
public class MemcacheResponse {

    public MemcacheResponse(MemcachedBinaryHeader header, byte body[]) {
        myHeader = header;
        myData = body;
        myQueue = false;
        myDiscard = false;
        if (myData != null) {
            header.setBodyLength(myData.length);
        }
    }
    
    public MemcachedBinaryHeader getHeader() {
        return myHeader;
    }
    
    public void setQueue(boolean value) {
        myQueue = value;
    }
    
    public void setDiscard(boolean value) {
        myDiscard = value;
    }
    
    public void writeToBuffer(ChannelBuffer buffer) {
        myHeader.write(buffer);
        if (myData != null) {
            buffer.writeBytes(myData);
        }
    }
    
    boolean isQueue() {
        return myQueue;
    }
    
    boolean isDiscard() {
        return myDiscard;
    }
    
    private MemcachedBinaryHeader myHeader;
    private byte[] myData;
    private boolean myQueue;
    private boolean myDiscard;
}
