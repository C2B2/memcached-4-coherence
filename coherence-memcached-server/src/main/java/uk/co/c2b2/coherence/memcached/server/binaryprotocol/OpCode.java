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

/**
 *
 * @author steve
 */
public enum OpCode {
    
    GET((byte)0x00),
    SET((byte)0x01),
    ADD((byte)0x02),
    REPLACE((byte)0x03),
    DELETE((byte)0x04),
    INCREMENT((byte)0x05),
    DECREMENT((byte)0x06),
    QUIT((byte)0x07),
    FLUSH((byte)0x08),
    GETQ((byte)0x09),
    NOOP((byte)0x0a),
    VERSION((byte)0x0b),
    GETK((byte)0x0c),
    GETKQ((byte)0x0d),
    APPEND((byte)0x0e),
    PREPEND((byte)0x0f),
    STAT((byte)0x10),
    SETQ((byte)0x11),
    ADDQ((byte)0x12),
    REPLACEQ((byte)0x13),
    DELETEQ((byte)0x14),
    INCREMENTQ((byte)0x15),
    DECREMENTQ((byte)0x16),
    QUITQ((byte)0x17),
    FLUSHQ((byte)0x18),
    APPENDQ((byte)0x19),
    PREPENDQ((byte)0x1a),
    UNKNOWN((byte)0xff);

    private OpCode(byte code) {
        this.code = code;
    }
    
    public static OpCode fromByte(byte val) {
        switch(val) {
            case 0x00:
                return GET;
            case 0x01:
                return SET;
            case 0x02:
                return ADD;
            case 0x03:
                return REPLACE;
            case 0x04:
                return DELETE;
            case 0x05:
                return INCREMENT;
            case 0x06:
                return DECREMENT;
            case 0x07:
                return QUIT;
            case 0x08:
                return FLUSH;
            case 0x09:
                return GETQ;
            case 0x0a: 
                return NOOP;
            case 0x0b:
                return VERSION;
            case 0x0c:
                return GETK;
            case 0x0d:
                return GETKQ;
            case 0x0e:
                return APPEND;
            case 0x0f:
                return PREPEND;
            case 0x10:
                return STAT;
            case 0x11:
                return SETQ;
            case 0x12:
                return ADDQ;
            case 0x13:
                return REPLACEQ;
            case 0x14:
                return DELETEQ;
            case 0x15:
                return INCREMENTQ;
            case 0x16:
                return DECREMENTQ;
            case 0x17:
                return QUITQ;
            case 0x18:
                return FLUSHQ;
            case 0x19:
                return APPENDQ;
            case 0x1a:
                return PREPENDQ;
            default:
                return UNKNOWN;
        }
    }
    
    public byte toByte() {
        return code;
    }
    
    byte code;
    
}
