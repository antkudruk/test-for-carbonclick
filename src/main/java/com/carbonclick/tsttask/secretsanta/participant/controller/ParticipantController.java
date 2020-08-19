package com.carbonclick.tsttask.secretsanta.participant.controller;

import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantRepository participantRepository;

    @GetMapping
    public Page<ParticipantResponse> list(PageRequest pageable) {
        return participantRepository.list(pageable);
    }

    @GetMapping("/{id:\\d+}")
    public ParticipantResponse get(@PathVariable("id") long id) {
        return participantRepository.get(id);
    }

    @PostMapping
    @ResponseBody
    public ParticipantResponse save(@RequestBody ParticipantRequest request) {
        ParticipantResponse resp = participantRepository.save(request);
        return resp;
    }

    @PutMapping("/{id:\\d+}")
    @ResponseBody
    public ParticipantResponse update(@PathVariable("id") long id, @RequestBody ParticipantRequest request) {
        ParticipantResponse resp = participantRepository.update(id, request);
        return resp;
    }
}
