package com.carbonclick.tsttask.secretsanta.participant.controller;

import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.request.ParticipantRequest;
import com.carbonclick.tsttask.secretsanta.participant.controller.response.ParticipantResponse;
import com.carbonclick.tsttask.secretsanta.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/participant")
@RequiredArgsConstructor
@Validated
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
    public ParticipantResponse save(@Valid @RequestBody ParticipantRequest request) {
        return participantRepository.save(request);
    }

    @PutMapping("/{id:\\d+}")
    @ResponseBody
    public ParticipantResponse update(@PathVariable("id") long id, @Valid @RequestBody ParticipantRequest request) {
        return participantRepository.update(id, request);
    }
}
