package com.aksh.twilioDemo.controller;

import com.aksh.twilioDemo.service.TwilioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @CrossOrigin @RequestMapping("/twilio") @Slf4j public class TwilioController {

    @Autowired private TwilioService twilioService;

    @PostMapping("/room/{roomName}")
    public String createRoom(@PathVariable("roomName") String roomName) {
        try {
            return twilioService.createRoom(roomName);
        } catch (Exception e) {
            log.error("error while creating room {}", e);
            return "Failed to create room";
        }
    }

    @PutMapping("/room/{roomId}") public Boolean startRoom(@PathVariable("roomId") String roomId) {
        try {
            return twilioService.startRoom(roomId);
        } catch (Exception e) {
            log.error("error while starting room {}", e);
            return false;
        }
    }

    @DeleteMapping("/room/{roomId}") public Boolean endRoom(@PathVariable("roomId") String roomId) {
        try {
            return twilioService.completeRoom(roomId);
        } catch (Exception e) {
            log.error("error while ending room {}", e);
            return false;
        }
    }

    @PostMapping("/room/{roomId}/presenter/{username}")
    public String addPresenter(@PathVariable("roomId") String roomId,
        @PathVariable("username") String username) {
        try {
            return twilioService.generatePresenterToken(username, roomId);
        } catch (Exception e) {
            log.error("error while adding presenter {}", e);
            return "Failed to fetch presenter token";
        }
    }

    @PostMapping("/room/{roomId}/attendee/{username}/{presenterUsername}")
    public Boolean addAttendee(@PathVariable("roomId") String roomId,
        @PathVariable("username") String username,
        @PathVariable("presenterUsername") String presenterUsername) {
        try {
            return twilioService.addAttendeeToARoom(username, roomId, presenterUsername);
        } catch (Exception e) {
            log.error("error while adding attendee {}", e);
            return false;
        }
    }

}
