package com.softserve.borda.controllers;

import com.softserve.borda.config.jwt.JwtConvertor;
import com.softserve.borda.dto.UserSimpleDTO;
import com.softserve.borda.entities.Invitation;
import com.softserve.borda.services.InvitationService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invitations")
@AllArgsConstructor
@Log
@CrossOrigin
public class InvitationController {

    //private final ModelMapper modelMapper;

    private final InvitationService invitationService;

    private final JwtConvertor jwtConvertor;

    @GetMapping
    public List<Invitation> getAllInvitation(
                                    @RequestHeader String authorization) {
        var user = jwtConvertor.getUserByJWT(authorization);
        //modelMapper.map(userDTO, user);
        return invitationService.getAllByUser(user);
    }
}
