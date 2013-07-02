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
package uk.co.c2b2.memcached.server;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author steve
 */
public class CacheEntry implements Serializable, PortableObject {
    
    public static final int VALUE = 0;
    public static final int CAS = 1;
    public static final int FLAGS =2;
    
    public CacheEntry(int flags, byte[] value, long cas) {
        this.value = value;
        this.cas = cas;
        this.flags = flags;
    }
    
    public int getFlags() {return this.flags;}
     

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public long getCas() {
        return cas;
    }

    public void setCas(long cas) {
        this.cas = cas;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        result.append(cas).append(',').append(flags).append(',').append(value).append('}');
        return result.toString();
    }
    
    public void readExternal(PofReader reader) throws IOException {
       value = reader.readByteArray(VALUE);
       cas = reader.readLong(CAS);
       flags = reader.readInt(FLAGS);
    }

    public void writeExternal(PofWriter writer) throws IOException {
        writer.writeByteArray(VALUE, value);
        writer.writeLong(CAS, cas);
        writer.writeInt(FLAGS, flags);
    }
    
    
    private byte value[];
    private long cas;
    private int flags;
}
