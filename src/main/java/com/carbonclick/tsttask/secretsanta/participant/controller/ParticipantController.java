package com.carbonclick.tsttask.secretsanta.participant.controller;

import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantRepository participantRepository;

    @GetMapping
    public Page<ParticipantResponse> list(Pageable pageable) {
        return participantRepository.list(pageable);
    }
}
