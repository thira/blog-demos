/**
 * 
 */
package com.thira.examples.spring.async.web.dao;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.thira.examples.spring.async.web.model.Message;
import com.thira.examples.spring.async.web.model.MessageType;

/**
 * @author thiranjith.weerasin
 * 
 *         Created at 19/04/2013 12:49:59 PM
 * 
 */
@Repository
public final class DataRepository {

    private final Logger logger = Logger.getLogger(DataRepository.class);

    private final Set<String> users = new HashSet<String>();
    private final List<Message> messages = new CopyOnWriteArrayList<Message>();

    public Boolean addUser(String user, long timestamp) {
        boolean isNewUser = !users.contains(user);
        if (isNewUser) {
            users.add(user);
            messages.add(new Message(MessageType.LOGIN_MESSAGE, user, timestamp, ""));
        }

        return isNewUser;
    }

    public Boolean removeUser(String user, long timestamp) {
        boolean isRemoved = users.remove(user);
        if (isRemoved) {
            messages.add(new Message(MessageType.LOGOUT_MESSAGE, user, timestamp, ""));
        }
        return isRemoved;
    }

    public boolean addNewMessage(String user, String message, long timeStamp) {
        boolean hasUser = users.contains(user);
        if (hasUser) {
            Message msg = new Message(user, timeStamp, message);
            return messages.add(msg);
        }

        return hasUser;
    }

    /**
     * @param timeStamp
     * @return
     */
    public List<Message> getMessagesReceivedSince(long upToTimeStampInMillis) {
        Message msgToSearch = new Message(null, upToTimeStampInMillis, null);
        int insertionIndex = Math.abs(Collections.binarySearch(messages, msgToSearch) + 1);
        logger.debug(String.format("Messages from [%d ms] - idx %d to %d (size)", upToTimeStampInMillis, insertionIndex, messages.size()));
        if (insertionIndex == messages.size()) {
            // No messages since the last timestamp
            return Collections.<Message> emptyList();
        } else if (insertionIndex == 0) {
            // All the messages contained in the list are newer than the given timestamp
            return messages;
        } else {
            // return the messages that are newer than the timestamp
            // see Collections.binarySearch for definition of idx value
            return messages.subList(insertionIndex, messages.size());
        }
    }

}