package com.carbonclick.tsttask.secretsanta.assignment.controller;

import com.carbonclick.tsttask.secretsanta.assignment.controller.request.NewAssignmentRequest;
import com.carbonclick.tsttask.secretsanta.assignment.controller.response.AssignmentResponse;
import com.carbonclick.tsttask.secretsanta.assignment.controller.response.YearResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.AssignmentRepository;
import com.carbonclick.tsttask.secretsanta.assignment.repository.YearRepository;
import com.carbonclick.tsttask.secretsanta.assignment.service.AssignmentService;
import com.carbonclick.tsttask.secretsanta.base.page.Page;
import com.carbonclick.tsttask.secretsanta.base.page.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/year")
@RequiredArgsConstructor
@Validated
public class YearController {

    private final AssignmentService assignmentService;
    private final YearRepository yearRepository;
    private final AssignmentRepository assignmentRepository;

    @PostMapping("/generate")
    @ResponseBody
    public YearResponse generate(@Valid @RequestBody NewAssignmentRequest request) {
        return assignmentService.assign(request);
    }

    @GetMapping
    public Page<YearResponse> list(PageRequest pageRequest) {
        return yearRepository.list(pageRequest);
    }

    @GetMapping("/{id:\\d+}")
    public YearResponse get(@PathVariable Long id) {
        return yearRepository.get(id);
    }

    @GetMapping("/{id:\\d+}/assignment")
    public Page<AssignmentResponse> assignments(@PathVariable Long id, PageRequest pageRequest) {
        return assignmentRepository.page(id, pageRequest);
    }
}
