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

public class OperationFactory {
    public static MemCacheOperation createOperation(OpCode opCode) {
        MemCacheOperation operation;
        switch (opCode) {
            case GET:
                operation = new GetOperation();
                break;
            case SET:
                operation = new SetOperation();
                break;
            case ADD:
                operation = new AddOperation();
                break;
            case REPLACE:
                operation = new ReplaceOperation();
                break;
            case DELETE:
                operation = new DeleteOperation();
                break;
            case INCREMENT:
                operation = new IncrementOperation();
                break;
            case DECREMENT:
                operation = new DecrementOperation();
                break;
            case QUIT:
                operation = new QuitOperation();
                break;
            case FLUSH:
                operation = new FlushOperation();
                break;
            case GETQ:
                operation = new GetQOperation();
                break;
            case NOOP:
                operation = new NoOpOperation();
                break;
            case VERSION:
                operation = new VersionOperation();
                break;
            case GETK:
                operation = new GetKOperation();
                break;
            case GETKQ:
                operation = new GetKQOperation();
                break;
            case APPEND:
                operation = new AppendOperation();
                break;
            case PREPEND:
                operation = new PrependOperation();
                break;
            case STAT:
                operation = new StatOperation();
                break;
            case SETQ:
                operation = new SetQOperation();
                break;
            case ADDQ:
                operation = new AddQOperation();
                break;
            case REPLACEQ:
                operation = new ReplaceQOperation();
                break;
            case DELETEQ:
                operation = new DeleteQOperation();
                break;
            case INCREMENTQ:
                operation = new IncrementQOperation();
                break;
            case DECREMENTQ:
                operation = new DecrementQOperation();
                break;
            case QUITQ:
                operation = new QuitQOperation();
                break;
            case FLUSHQ:
                operation = new FlushQOperation();
                break;
            case APPENDQ:
                operation = new AppendQOperation();
                break;
            case PREPENDQ:
                operation = new PrependQOperation();
                break;
            default:
                operation = new NoOpOperation();
                break;
        }
        return operation;
    }

}
