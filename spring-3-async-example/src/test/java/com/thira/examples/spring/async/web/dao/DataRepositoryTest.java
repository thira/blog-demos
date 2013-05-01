/**
 * 
 */
package com.thira.examples.spring.async.web.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thira.examples.spring.async.web.model.Message;

/**
 * @author thiranjith.weerasin
 * 
 *         Created at 22/04/2013 9:15:36 AM
 * 
 */
public class DataRepositoryTest {

    private DataRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new DataRepository();
    }

    @Test
    public void testAddAndRemoveUser() {
        final String user1 = "user1";
        final String user2 = "user2";
        // 1. Add - unique names
        assertTrue(repository.addUser(user1, 1));
        assertTrue(repository.addUser(user2, 1));
        // 2. Add - different case
        assertTrue(repository.addUser(user1.toUpperCase(), 1));
        // 3. Add - Duplicates are ignored
        assertFalse(repository.addUser(user1, 1));

        // 4. Remove - Existing user
        assertTrue(repository.removeUser(user1, 1));
        // 4. Remove - Non-existing user
        assertFalse(repository.removeUser(user1, 1));
    }

    @Test
    public void testAddMessageForNonExistingUser() {
        final String user = "user";
        assertFalse(repository.removeUser(user, 1)); // pre-condition to ensure user doesn't exist

        assertFalse(repository.addNewMessage(user, "message", 1));
    }

    @Test
    public void testAddMessageForExistingUser() {
        final String user = "user";
        assertTrue(repository.addUser(user, 1));

        assertTrue(repository.addNewMessage(user, "message", 1));
        assertTrue(repository.addNewMessage(user, "message", 1)); // duplicates are accepted
    }

    @Test
    public void testGetMessagesReceivedWithNoMessages() {
        List<Message> result = repository.getMessagesReceivedSince(1);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetMessagesReceivedWithNoNewMessages() {
        final String user = "user";
        final String message = "user test message";
        // setup
        assertTrue(repository.addUser(user, 1));
        assertTrue(repository.addNewMessage(user, message, 2));

        List<Message> result = repository.getMessagesReceivedSince(3);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetMessagesReceivedWhereAllMessagesAreNewer() {
        final String user = "user";
        final String message = "user test message";
        // setup
        assertTrue(repository.addUser(user, 0));
        assertTrue(repository.addNewMessage(user, message, 2));
        assertTrue(repository.addNewMessage(user, message, 3));
        assertTrue(repository.addNewMessage(user, message, 4));

        List<Message> result = repository.getMessagesReceivedSince(1);

        assertEquals(3, result.size());
        assertEquals(new Message(user, 2, message), result.get(0));
        assertEquals(new Message(user, 3, message), result.get(1));
        assertEquals(new Message(user, 4, message), result.get(2));
    }

    @Test
    public void testGetMessagesReceivedWhereSomeMessagesAreNewer() {
        final String user = "user";
        final String message = "user test message";
        // setup
        assertTrue(repository.addUser(user, 0));
        assertTrue(repository.addNewMessage(user, message, 2));
        assertTrue(repository.addNewMessage(user, message, 4));
        assertTrue(repository.addNewMessage(user, message, 6));

        List<Message> result = repository.getMessagesReceivedSince(3);

        assertEquals(2, result.size());
        assertEquals(new Message(user, 4, message), result.get(0));
        assertEquals(new Message(user, 6, message), result.get(1));
    }
}
