package com.gts.testgts;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import java.util.ArrayList;
import java.util.List;

public class StubMessageChannel implements SubscribableChannel {

    private final List<Message<byte[]>> messages = new ArrayList<>();

    private final List<MessageHandler> handlers = new ArrayList<>();


    public List<Message<byte[]>> getMessages() {
        return this.messages;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean send(Message<?> message) {
        this.messages.add((Message<byte[]>) message);
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean send(Message<?> message, long timeout) {
        this.messages.add((Message<byte[]>) message);
        return true;
    }

    @Override
    public boolean subscribe(MessageHandler handler) {
        this.handlers.add(handler);
        return true;
    }

    @Override
    public boolean unsubscribe(MessageHandler handler) {
        this.handlers.remove(handler);
        return true;
    }
}