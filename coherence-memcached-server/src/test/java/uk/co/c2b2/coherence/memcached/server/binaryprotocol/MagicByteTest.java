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

import org.junit.Test;
import static org.junit.Assert.*;

public class MagicByteTest {

    @Test
    public void testRequestMagicByte() {
        MagicByte requestMagicByte = MagicByte.fromByte((byte)0x80);
        assertEquals(MagicByte.REQUEST, requestMagicByte);
    }

    @Test
    public void testResponseMagicByte() {
        MagicByte responseMagicByte = MagicByte.fromByte((byte)0x81);
        assertEquals(MagicByte.RESPONSE, responseMagicByte);
    }
}
