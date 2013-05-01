/**
 * 
 */
package com.thira.examples.spring.async.web.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.thira.examples.spring.async.web.dao.DataRepository;
import com.thira.examples.spring.async.web.model.Message;

/**
 * 
 * @author Thiranjith
 * 
 *         Created at 19/04/2013 12:50:37 PM
 * 
 */
@Controller
@RequestMapping("/async")
public class AsyncController {

    private static final Logger logger = Logger.getLogger(AsyncController.class);

    @Autowired
    private DataRepository dataRespository;

    private final Map<DeferredResult<List<Message>>, Long> dataRequests = new ConcurrentHashMap<DeferredResult<List<Message>>, Long>();

    @RequestMapping(method = RequestMethod.GET)
    public String loadPage() {
        return "async";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Boolean loginUser(String user) {
        logger.debug(String.format("loginUser: %s.", user));
        boolean isSuccess = dataRespository.addUser(user, System.currentTimeMillis());

        notifyListeners();

        return isSuccess;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Boolean logoutUser(String user) {
        logger.debug(String.format("logoutUser: %s.", user));
        boolean isSuccess = dataRespository.removeUser(user, System.currentTimeMillis());

        notifyListeners();

        return isSuccess;
    }

    /**
     * Async method that handles long polls sent by the client. DeferredResult will
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    @ResponseBody
    public DeferredResult<List<Message>> displayMessages(@RequestParam
    final String user) {
        logger.info(String.format("displayMessages [user: %s]", user));

        final long timeStamp = System.currentTimeMillis();
        final DeferredResult<List<Message>> deferredResult = createNewDeferredResultAndAddToMap(timeStamp);

        // Notify immediately if there are data to be displayed
        List<Message> data = dataRespository.getMessagesReceivedSince(timeStamp);
        if (!data.isEmpty()) {
            deferredResult.setResult(data);
        }

        return deferredResult;
    }

    /**
     * Handler when a user sends a message to the system.
     * 
     * @param user
     * @param message
     */
    @RequestMapping(value = "/chat", method = RequestMethod.POST)
    @ResponseBody
    public void receivedMessageSentByUser(@RequestParam
    String user, @RequestParam
    String message) {
        logger.info(String.format("receivedMessageSentByUser [%s] message: '%s'", user, message));
        final long timeStamp = System.currentTimeMillis();
        dataRespository.addNewMessage(user, message, timeStamp);

        notifyListeners();
    }

    private DeferredResult<List<Message>> createNewDeferredResultAndAddToMap(long timeStamp) {
        // 1. Create a new DeferredResult
        final DeferredResult<List<Message>> deferredResult = new DeferredResult<List<Message>>(null, Collections.emptyList());

        // 2. Let the defferedResult notify the client when it is completed (i.e. when a user logs in, logs out or sends a message)
        deferredResult.onCompletion(new Runnable() {

            @Override
            public void run() {
                dataRequests.remove(deferredResult);
            }
        });

        // 3. Add to the map
        this.dataRequests.put(deferredResult, timeStamp);

        return deferredResult;
    }

    private void notifyListeners() {
        logger.info("receivedMessageSentByUser: about to notify " + dataRequests.size() + " data requests...");
        // Update all deferredResults
        for (Entry<DeferredResult<List<Message>>, Long> entry : this.dataRequests.entrySet()) {
            List<Message> messages = this.dataRespository.getMessagesReceivedSince(entry.getValue());
            entry.getKey().setResult(messages);
        }

        logger.info("postData: done notifying dataRequests: " + dataRequests.size());
    }

}
