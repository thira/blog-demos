/**
 * 
 */
package com.thira.examples.spring.async.web.model;

/**
 * @author Thiranjith
 * 
 *         Created at 22/04/2013 8:56:49 AM
 * 
 */
public class Message implements Comparable<Message> {

    private final String user;
    private final long timeStamp;
    private final String message;
    private final MessageType messageType;

    public Message(MessageType messageType, String user, long timeStamp, String message) {
        this.user = user;
        this.timeStamp = timeStamp;
        this.message = message;
        this.messageType = messageType;
    }

    /**
     * Creats a User Message by default
     * 
     * @param user
     * @param timeStamp
     * @param message
     */
    public Message(String user, long timeStamp, String message) {
        this.user = user;
        this.timeStamp = timeStamp;
        this.message = message;
        this.messageType = MessageType.USER_MESSAGE;
    }

    public String getUser() {
        return user;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Message) && ((Message) obj).timeStamp == this.timeStamp;
    }

    @Override
    public int hashCode() {
        return (int) (timeStamp ^ (timeStamp >>> 32));
    }

    @Override
    public int compareTo(Message other) {
        return (this.timeStamp < other.timeStamp ? -1 : (this.timeStamp == other.timeStamp ? 0 : 1));
    }

}
