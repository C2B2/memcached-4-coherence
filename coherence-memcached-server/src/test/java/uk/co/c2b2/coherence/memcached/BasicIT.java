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
package uk.co.c2b2.coherence.memcached;


import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.spy.memcached.CASMutation;
import net.spy.memcached.CASMutator;
import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.transcoders.SerializingTranscoder;
import net.spy.memcached.transcoders.Transcoder;
import org.junit.Test;

import static org.junit.Assert.*;
import static uk.co.c2b2.coherence.memcached.AbstractIntegrationTest.client;

public class BasicIT extends AbstractIntegrationTest {

    @Test
    public void testPutGet() throws Exception {
        String key = "myKey";
        String object = "String Instance 1";
        int expiration = 10000;

        boolean setResult = client.set(key, expiration, object).get();
        assertTrue(setResult);

        String result = (String)client.get(key);
        assertEquals(object, result);
    }
   
    @Test
    public void testGetBadKey() throws Exception {
        String key = "badKey";
        Object result = client.get(key);
        assertNull(result);
    }
    
    @Test
    public void testSetOverwrite() throws Exception {
        String overWriteKey = "overwriteKey";
        String value1 = "String Instance 1";
        String value2 = "String Instance 2";
        int expiration = 10000;
        
        // test initial set
        boolean setResult = client.set(overWriteKey, expiration, value1).get();
        assertTrue(setResult);       
        String result = (String)client.get(overWriteKey);
        assertEquals(result, value1);
        
        // now overwrite the key
        setResult = client.set(overWriteKey, expiration, value2).get();
        assertTrue(setResult);
        result = (String)client.get(overWriteKey);
        assertEquals(result, value2);
    }
    
    @Test 
    public void testAdd() throws Exception {
        String addKey = "addKey";
        String value = "AddValue";
        int expiration = 10000;
        
        boolean addResult = client.add(addKey, expiration, value).get();
        assertTrue(addResult); 
        String result = (String)client.get(addKey);
        assertEquals(result, value);
    }
    
    @Test 
    public void testDoubleAdd() throws Exception {
        String addKey = "doubleAddKey";
        String value = "AddValue";
        String value2 = "AddValue2";
        int expiration = 10000;
        
        boolean addResult = client.add(addKey, expiration, value).get();
        assertTrue(addResult); 
        String result = (String)client.get(addKey);
        assertEquals(result, value);
        
        // second add should fail as the key value already exists
        addResult = client.add(addKey, expiration, value).get();
        assertFalse(addResult); 
        result = (String)client.get(addKey);
        assertEquals(result, value);
    }
    
    @Test
    public void testReplace() throws Exception {
        String key = "testReplace";
        String value1 = "SetValue";
        String value2 = "ReplaceValue";
        int expiration = 10000;   
        
         // test initial set
        boolean setResult = client.set(key, expiration, value1).get();
        assertTrue(setResult);       
        String result = (String)client.get(key);
        assertEquals(result, value1);
        
        // test replace
        boolean replaceResult = client.replace(key, expiration, value2).get();
        assertTrue(replaceResult);       
        result = (String)client.get(key);
        assertEquals(result, value2);
    }
    
    @Test
    public void testReplaceWithoutSet() throws Exception {
        String key = "testReplaceWithoutSet";
        String value = "ReplaceValue";
        int expiration = 10000;   
        
        // test replace
        boolean replaceResult = client.replace(key, expiration, value).get();
        assertFalse(replaceResult);       
        String result = (String)client.get(key);
        assertNull(result);
    }
    
    @Test
    public void testDelete() throws Exception {
        String key = "testDelete";
        String value1 = "DeleteValue";
        int expiration = 10000;   
        
         // test initial set
        boolean setResult = client.set(key, expiration, value1).get();
        assertTrue(setResult);       
        String result = (String)client.get(key);
        assertEquals(result, value1);
        
        // test delete
        boolean replaceResult = client.delete(key).get();
        assertTrue(replaceResult);       
        result = (String)client.get(key);
        assertNull(result);
    }
    
    @Test
    public void testDeleteWithoutSet() throws Exception {
        String key = "testDeleteWithoutSet";
        String value = "DeleteValue"; 
        
        // test delete
        boolean deleteResult = client.delete(key).get();
        assertFalse(deleteResult);       
    }
    
    @Test
    public void testIncrement() throws Exception {
        String key = "testIncrement";
        long value = client.incr(key, 1, 1, 86000);
        assertEquals(1, value);
        value = client.incr(key, 20);
        assertEquals(21, value);
    }
    
    @Test
    public void testIncrementIncorrectKey() throws Exception {
        String key = "testIncrementIncorrectKey";
        long value = client.incr(key, 20);
        assertEquals(-1, value);       
    }
    
    @Test
    public void testDecrement() throws Exception {
        String key = "testDecrement";
        long value = client.decr(key, 1, 21, 86000);
        assertEquals(21, value);
        value = client.decr(key, 20);
        assertEquals(1, value);
    }
    
    @Test
    public void testDecrementIncorrectKey() throws Exception {
         String key = "testDecrementIncorrectKey";
        long value = client.decr(key, 20);
        assertEquals(-1, value);       
    }
    
    @Test
    public void testAppend() throws Exception {
       String key = "testAppend";
       String part1 = "Hello";
       String part2 = " World";
       client.set(key,10000,part1);
       String result = (String)client.get(key);
       assertEquals(result, part1);
       client.append(key, part2);
       result = (String)client.get(key);
       assertEquals(part1 + part2, result);
    }

    @Test
    public void testPrepend() throws Exception {
       String key = "testPrepend";
       String part1 = "Hello";
       String part2 = " World";
       client.set(key,10000,part1);
       String result = (String)client.get(key);
       assertEquals(result, part1);
       client.prepend(key, part2);
       result = (String)client.get(key);
       assertEquals(part2 + part1, result);
    }
    
    @Test public void testGetBulk() throws Exception {
        String key = "testGetBulk";
        String value = "Value";
        HashSet<String> keys = new HashSet<String>(100);
        for (int i = 0; i < 20; i++) {
            client.set(key+i, 10000, value + i);
            keys.add(key+i);
        }
        
        Map<String,Object> results = client.getBulk(keys);
         for (int i = 0; i < 20; i++) {
            String testValue = (String)results.get(key+i);
            assertEquals(value+i, testValue);
        }       
    }
    
    @Test public void testCAS() throws Exception {
        String key = "testCAS";
        Transcoder tc = new SerializingTranscoder();
        CASMutator<String> mutator=new CASMutator<String>(client, tc);
        CASMutation<String> mutation=new CASMutation<String>() {
        public String getNewValue(String current) {
            return current + current;
            }
        };

        // Do a mutation.
       String currentValue=mutator.cas(key, "Wibble", 10000, mutation);
       currentValue=mutator.cas(key, "Wibble", 10000, mutation);
       assertEquals("WibbleWibble", currentValue);
    }
    
    @Test public void testVersion() throws Exception {
        Map<SocketAddress, String> versions = client.getVersions();
        Set<SocketAddress> keys = versions.keySet();
        assertEquals(versions.get(keys.toArray()[0]),"1.0.0");
    }
}
