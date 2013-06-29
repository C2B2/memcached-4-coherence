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
public class MemcachedBinaryHeader {
    
    public MemcachedBinaryHeader() {
        this(MagicByte.RESPONSE);
    }

    public MemcachedBinaryHeader(MagicByte magic) { 
        this.magic = magic;
    }

    public MemcachedBinaryHeader(ChannelBuffer buffer) {
        read(buffer);
    }
    
    public void write(ChannelBuffer buffer) {
        buffer.writeByte(magic.magicByte);
        buffer.writeByte(opCode.toByte());
        buffer.writeShort(keyLength);
        buffer.writeByte(extraLength);
        buffer.writeByte(dataType);
        buffer.writeShort(reserved);
        buffer.writeInt(bodyLength);
        buffer.writeInt(opaque);
        buffer.writeLong(cas);
    }
    
    public void read(ChannelBuffer buffer) {
        magic = MagicByte.fromByte(buffer.readByte());
        opCode = OpCode.fromByte(buffer.readByte());
        keyLength = buffer.readShort();
        extraLength = buffer.readByte();
        dataType = buffer.readByte();
        reserved = buffer.readShort();
        bodyLength = buffer.readInt();
        opaque = buffer.readInt();
        cas = buffer.readLong();
    }

    public void setOpCode(OpCode opCode) {
        this.opCode = opCode;
    }

    public void setKeyLength(short keyLength) {
        this.keyLength = keyLength;
    }

    public void setExtraLength(byte extraLength) {
        this.extraLength = extraLength;
    }

    public void setDataType(byte dataType) {
        this.dataType = dataType;
    }

    public void setReserved(short reserved) {
        this.reserved = reserved;
    }
    
    public void setStatus(short status) {
        this.reserved = status;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public void setOpaque(int opaque) {
        this.opaque = opaque;
    }

    public void setCas(long cas) {
        this.cas = cas;
    }
    
    
    
    boolean isRequest() {
        return magic == MagicByte.REQUEST;
    }
    
    

    public MagicByte getMagic() {
        return magic;
    }

    public OpCode getOpCode() {
        return opCode;
    }

    public short getKeyLength() {
        return keyLength;
    }

    public byte getExtraLength() {
        return extraLength;
    }

    public byte getDataType() {
        return dataType;
    }

    public short getReserved() {
        return reserved;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public int getOpaque() {
        return opaque;
    }

    public long getCas() {
        return cas;
    }
    
    
    
    MagicByte magic;
    OpCode opCode;
    short keyLength;
    byte extraLength;
    byte dataType;
    short reserved;
    int bodyLength;
    int opaque;
    long cas;
}
