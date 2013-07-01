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

import com.tangosol.net.NamedCache;
import uk.co.c2b2.coherence.memcached.server.binaryprotocol.OpCode;

public class OperationFactory {

    private final NamedCache myCache;

    public OperationFactory(NamedCache namedCache) {
        this.myCache = namedCache;
    }

    public MemCacheOperation createOperation(OpCode opCode) {
        MemCacheOperation operation;
        switch (opCode) {
            case GET:
                operation = new GetOperation(myCache);
                break;
            case SET:
                operation = new SetOperation(myCache);
                break;
            case ADD:
                operation = new AddOperation(myCache);
                break;
            case REPLACE:
                operation = new ReplaceOperation(myCache);
                break;
            case DELETE:
                operation = new DeleteOperation(myCache);
                break;
            case INCREMENT:
                operation = new IncrementOperation(myCache);
                break;
            case DECREMENT:
                operation = new DecrementOperation(myCache);
                break;
            case QUIT:
                operation = new QuitOperation(myCache);
                break;
            case FLUSH:
                operation = new FlushOperation(myCache);
                break;
            case GETQ:
                operation = new GetQOperation(myCache);
                break;
            case NOOP:
                operation = new NoOpOperation(myCache);
                break;
            case VERSION:
                operation = new VersionOperation(myCache);
                break;
            case GETK:
                operation = new GetKOperation(myCache);
                break;
            case GETKQ:
                operation = new GetKQOperation(myCache);
                break;
            case APPEND:
                operation = new AppendOperation(myCache);
                break;
            case PREPEND:
                operation = new PrependOperation(myCache);
                break;
            case STAT:
                operation = new StatOperation(myCache);
                break;
            case SETQ:
                operation = new SetQOperation(myCache);
                break;
            case ADDQ:
                operation = new AddQOperation(myCache);
                break;
            case REPLACEQ:
                operation = new ReplaceQOperation(myCache);
                break;
            case DELETEQ:
                operation = new DeleteQOperation(myCache);
                break;
            case INCREMENTQ:
                operation = new IncrementQOperation(myCache);
                break;
            case DECREMENTQ:
                operation = new DecrementQOperation(myCache);
                break;
            case QUITQ:
                operation = new QuitQOperation(myCache);
                break;
            case FLUSHQ:
                operation = new FlushQOperation(myCache);
                break;
            case APPENDQ:
                operation = new AppendQOperation(myCache);
                break;
            case PREPENDQ:
                operation = new PrependQOperation(myCache);
                break;
            default:
                operation = new NoOpOperation(myCache);
                break;
        }
        return operation;
    }

}
