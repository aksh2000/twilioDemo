package com.aksh.twilioDemo.web.controller;

import com.aksh.twilioDemo.service.TwilioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.aksh.twilioDemo.web.utils.ApiPath.ATTENDEE;
import static com.aksh.twilioDemo.web.utils.ApiPath.PRESENTER_ID;
import static com.aksh.twilioDemo.web.utils.ApiPath.ROOM;
import static com.aksh.twilioDemo.web.utils.ApiPath.ROOMS;
import static com.aksh.twilioDemo.web.utils.ApiPath.ROOM_ID;
import static com.aksh.twilioDemo.web.utils.ApiPath.ROOM_NAME;
import static com.aksh.twilioDemo.web.utils.ApiPath.TOKEN;
import static com.aksh.twilioDemo.web.utils.ApiPath.TWILIO;
import static com.aksh.twilioDemo.web.utils.ApiPath.USER_ID;

@RestController @CrossOrigin @RequestMapping(TWILIO) @Slf4j public class TwilioController {

    @Autowired private TwilioService twilioService;

    @PostMapping(ROOM + ROOM_NAME)
    public String createRoom(@PathVariable("roomName") String roomName) {
        try {
            return twilioService.createRoom(roomName);
        } catch (Exception e) {
            log.error("error while creating room {}", e);
            return "Failed to create room";
        }
    }

    @PutMapping(ROOM + ROOM_ID) public Boolean startRoom(@PathVariable("roomId") String roomId) {
        try {
            return twilioService.startRoom(roomId);
        } catch (Exception e) {
            log.error("error while starting room {}", e);
            return false;
        }
    }

    @DeleteMapping(ROOM + ROOM_ID) public Boolean endRoom(@PathVariable("roomId") String roomId) {
        try {
            return twilioService.completeRoom(roomId);
        } catch (Exception e) {
            log.error("error while ending room {}", e);
            return false;
        }
    }

    @PostMapping(ROOM + ROOM_ID + TOKEN + USER_ID)
    public String generateToken(@PathVariable("roomId") String roomId,
        @PathVariable("userId") String username) {
        try {
            return twilioService.generateToken(username, roomId);
        } catch (Exception e) {
            log.error("error while adding presenter {}", e);
            return "Failed to fetch presenter token";
        }
    }

    @GetMapping(ROOMS) public List<String> getActiveRooms() {
        try {
            return twilioService.getAllActiveRoomIds();
        } catch (Exception e) {
            log.error("error while fetching roomIds {}", e);
            return null;
        }
    }

    @PostMapping(ROOM + ROOM_ID + ATTENDEE + USER_ID + PRESENTER_ID)
    public Boolean addAttendee(@PathVariable("roomId") String roomId,
        @PathVariable("userId") String username,
        @PathVariable("presenterId") String presenterUsername) {
        try {
            return twilioService.addAttendeeToARoom(username, roomId, presenterUsername);
        } catch (Exception e) {
            log.error("error while adding attendee {}", e);
            return false;
        }
    }

}
