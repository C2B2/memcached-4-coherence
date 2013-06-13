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
public enum ResponseStatus {
    
    NO_ERROR((short)0x0000),
    KEY_NOT_FOUND((short)0x0001),
    KEY_EXISTS((short)0x0002),
    VALUE_TOO_LARGE((short)0x0003),
    INVALID_ARGUMENTS((short)0x0004),
    ITEM_NOT_STORED((short)0x0005),
    NON_NUMERIC((short)0x0006),
    VBUCKET_BELONGS_TO_ANOTHER_SERVER((short)0x0007),
    AUTH_ERROR((short)0x0008),
    AUTH_CONTINUE((short)0x0009),
    UNKNOWN_COMMAND((short)0x0081),
    OUT_OF_MEMORY((short)0x0082),
    NOT_SUPPORTED((short)0x0083),
    INTERNAL_ERROR((short)0x0084),
    BUSY((short)0x0085),
    TEMP_FALIURE((short)0x0086);

    private ResponseStatus(short status) {
        this.status = status;
    }
    
    short status;
}
