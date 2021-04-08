package com.aksh.twilioDemo.service.impl;

import com.aksh.twilioDemo.service.TwilioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VideoGrant;
import com.twilio.rest.video.v1.Room;
import com.twilio.rest.video.v1.room.Participant;
import com.twilio.rest.video.v1.room.participant.SubscribeRules;
import com.twilio.type.SubscribeRule;
import com.twilio.type.SubscribeRulesUpdate;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.aksh.twilioDemo.config.TwilioConfig.ACCOUNT_SID;
import static com.aksh.twilioDemo.config.TwilioConfig.API_KEY;
import static com.aksh.twilioDemo.config.TwilioConfig.API_SECRET;
import static com.aksh.twilioDemo.config.TwilioConfig.AUTH_TOKEN;

@Service public class TwilioServiceImpl implements TwilioService {

    @Override public String createRoom(String roomName) throws Exception {

        if (StringUtils.isEmpty(roomName))
            throw new Exception("Room Name not provided");

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Room room = Room.creator().setRecordParticipantsOnConnect(true).setType(Room.RoomType.GROUP)
            .setUniqueName(roomName).create();

        // SID : The unique string that Twilio creates to identify the Room resource.
        return room.getSid();
    }


    @Override public Boolean completeRoom(String roomSid) throws Exception {

        /*
            Updates the Room's status to completed
            All connected Participants will be immediately disconnected from the Room.
         */

        if (StringUtils.isEmpty(roomSid))
            throw new Exception("Room SID not provided");

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Room room = Room.updater(roomSid, Room.RoomStatus.COMPLETED).update();

        return true;

    }

    @Override public Boolean startRoom(String roomSid) throws Exception {

        // waiting room concept should be implemented

        if (StringUtils.isEmpty(roomSid))
            throw new Exception("Room SID not provided");

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Room.updater(roomSid, Room.RoomStatus.IN_PROGRESS).update();

        return true;
    }

    @Override public List<String> getAllActiveRoomIds() throws Exception {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        List<String> roomIds = new ArrayList<>();

        ResourceSet<Room> inProgressRooms = Room.reader()
            .setStatus(Room.RoomStatus.IN_PROGRESS).limit(20).read();

        for(Room record : inProgressRooms) {
            roomIds.add(record.getSid() + " " + record.getUniqueName());
        }
        return roomIds; // return list later
    }

    @Override public String generateToken(String userName, String roomSid)
        throws Exception {

         /*
        The front end will need to connect to Twilio to access the streaming
        and sharing features of the Programmable Video service.
        To connect to the Twilio servers, the clients need to authenticate with
        an access token.
        */

        if (StringUtils.isEmpty(userName))
            throw new Exception("Username is not provided");


        if (StringUtils.isEmpty(roomSid))
            throw new Exception("Room Sid is not provided");

        final VideoGrant videoGrant = new VideoGrant();

        /*
        We use the VideoGrant class to provision the token
        with access to the video service.
        The roomSid argument limits the token to a specific room
         */

        videoGrant.setRoom(roomSid);

        /*
        The Twilio package  provides the AccessToken class,
        which is used to create access tokens.
        We pass account_sid, api_key, api_secret, and the identity as arguments
         */

        AccessToken token =
            new AccessToken.Builder(ACCOUNT_SID, API_KEY, API_SECRET).identity(userName)
                .grant(videoGrant).build();

        return token.toJwt();
    }

    @Override
    public Boolean addAttendeeToARoom(String userName, String roomSid, String publisherUserName)
        throws Exception {

        /*
        Throw exception if the roomSid provided is not Active or rooms status is not IN-PROGRESS
         */

        if (StringUtils.isEmpty(userName))
            throw new Exception("Username is not provided");

        if (StringUtils.isEmpty(roomSid))
            throw new Exception("Room Sid is not provided");

        if (StringUtils.isEmpty(publisherUserName))
            throw new Exception("Publisher Username is not provided");

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        Participant participant = Participant.updater(roomSid, userName).update();

        SubscribeRulesUpdate rules = new SubscribeRulesUpdate(Lists.newArrayList(
            SubscribeRule.builder().withType(SubscribeRule.Type.INCLUDE)
                .withPublisher(publisherUserName).build()));

        SubscribeRules subscribeRules = SubscribeRules.updater(roomSid, userName)
            .setRules(new ObjectMapper().convertValue(rules, Map.class)).update();

        return true;
    }
}
