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


import net.spy.memcached.internal.OperationFuture;
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
    
    
}
