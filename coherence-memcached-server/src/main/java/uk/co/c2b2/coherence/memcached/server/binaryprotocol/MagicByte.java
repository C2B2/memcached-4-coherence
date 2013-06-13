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
public enum MagicByte {
    
    REQUEST((byte)0x80),
    RESPONSE((byte)0x81),
    BAD((byte)0xff);

    private MagicByte(byte magicByte) {
        this.magicByte = magicByte;
    }
    
    public static MagicByte fromByte(byte val) {
        MagicByte result = BAD;
        if (val == 0x80) {
            result = REQUEST;
        } else if (val == 0x81) {
            result = RESPONSE;
        }
        return result;
    }
    
    
    byte magicByte;
    
}
