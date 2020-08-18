package com.carbonclick.tsttask.secretsanta.assignment.service;


import com.carbonclick.tsttask.secretsanta.assignment.repository.dto.AssignmentDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AssignmentService {
    public List<AssignmentDto> assign(Set<Long> participants) {
        Set<Long> takersSet = new HashSet<>(participants);

        return participants.stream()
                .map(giver -> {
                    long taker = getAnotherRandom(giver, takersSet);
                    takersSet.remove(taker);
                    return AssignmentDto.builder()
                            .giverId(giver)
                            .takerId(taker)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // If just two participants remained, giver can't be equal to taker for a one participant
    private long getSafeAnotherRandom(long giver, Set<Long> takers) {
        if(takers.size() == 2) {
            // TODO:
        } else {
            getAnotherRandom(giver, takers);
        }
    }

    private long getAnotherRandom(long giver, Set<Long> takers) {
        int index = generateRandomNumber(takers.size() - 1);    // Random index, excluding giver
        for(long taker : takers) {
            if(taker != giver) {
                if(index == 0) {
                    return taker;
                } else {
                    index--;
                }
            }
        }
        throw new RuntimeException("A software error has ocured. Please, contact the developer.");
    }

    private int generateRandomNumber(int num) {
        double randomDouble = Math.random() * num;  // Random in range [0..num)
        return (int)Math.floor(randomDouble);       // 0, 1, ..., num-1
    }
}
