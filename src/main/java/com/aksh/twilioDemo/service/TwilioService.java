package com.aksh.twilioDemo.service;

import org.springframework.stereotype.Service;

public interface TwilioService {
    String createRoom(String roomName) throws Exception;

    Boolean completeRoom(String roomSid) throws Exception;

    Boolean startRoom(String roomSid) throws Exception;

    String generatePresenterToken(String userName, String roomSid) throws Exception;

    Boolean addAttendeeToARoom(String userName, String roomSid, String publisherUserName)
        throws Exception;
}
