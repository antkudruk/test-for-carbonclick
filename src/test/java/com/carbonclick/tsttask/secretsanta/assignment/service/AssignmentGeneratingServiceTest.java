package com.carbonclick.tsttask.secretsanta.assignment.service;

import com.carbonclick.tsttask.secretsanta.assignment.repository.dto.AssignmentDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class AssignmentGeneratingServiceTest {

    private final AssignmentGeneratingService assignmentGeneratingService = new AssignmentGeneratingService();

    @Test
    public void AssignmentTestOneParticipant() {
        Assertions.assertThrows(RuntimeException.class,
                () -> assignmentGeneratingService.assign(Collections.singleton(1L)));
    }

    @Test
    public void AssignmentTestTwoParticipants() {
        for(int i = 1; i < 10000; i++) {
            List<AssignmentDto> assignments = assignmentGeneratingService.assign(new HashSet<>(Arrays.asList(1L, 2L)));
            assertEquals(2, assignments.size());
            assertEquals(1, assignments.stream().filter(e -> e.getGiverId() == 1L).filter(e -> e.getTakerId() == 2L).count());
            assertEquals(1, assignments.stream().filter(e -> e.getGiverId() == 2L).filter(e -> e.getTakerId() == 1L).count());
        }
    }

    @Test
    public void AssignmentTestFiveParticipants() {
        for(int i = 1; i < 10000; i++) {
            List<AssignmentDto> assignments = assignmentGeneratingService.assign(new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L, 5L)));
            assertTrue(assignments.stream().noneMatch(e -> e.getGiverId() == e.getTakerId()));
        }
    }
}
