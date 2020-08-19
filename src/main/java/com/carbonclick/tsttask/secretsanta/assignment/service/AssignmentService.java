package com.carbonclick.tsttask.secretsanta.assignment.service;

import com.carbonclick.tsttask.secretsanta.assignment.controller.request.NewAssignmentRequest;
import com.carbonclick.tsttask.secretsanta.assignment.controller.response.YearResponse;
import com.carbonclick.tsttask.secretsanta.assignment.repository.YearRepository;
import com.carbonclick.tsttask.secretsanta.assignment.repository.dto.AssignmentDto;
import com.carbonclick.tsttask.secretsanta.assignment.repository.dto.YearDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentGeneratingService assignmentGeneratingService;
    private final YearRepository yearRepository;

    public YearResponse assign(NewAssignmentRequest request) {
        List<AssignmentDto> newAssignments = assignmentGeneratingService.assign(request.getParticipants());
        YearDto yearDto = YearDto.builder()
                .title(request.getTitle())
                .assignments(newAssignments)
                .build();

        return yearRepository.create(yearDto);
    }
}
