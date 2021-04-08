package com.aksh.twilioDemo.service;

import java.util.List;

public interface TwilioService {
    String createRoom(String roomName) throws Exception;

    Boolean completeRoom(String roomSid) throws Exception;

    Boolean startRoom(String roomSid) throws Exception;

    List<String> getAllActiveRoomIds() throws Exception;

    String generateToken(String userName, String roomSid) throws Exception;

    Boolean addAttendeeToARoom(String userName, String roomSid, String publisherUserName)
        throws Exception;
}
