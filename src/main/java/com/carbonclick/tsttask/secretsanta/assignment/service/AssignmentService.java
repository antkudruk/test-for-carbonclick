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
        Set<Long> giversSet = new HashSet<>(participants);
        Set<Long> takersSet = new HashSet<>(participants);

        return participants.stream()
                .map(giver -> {
                    long taker = getSafeAnotherRandom(giver, takersSet, giversSet);
                    takersSet.remove(taker);
                    giversSet.remove(giver);
                    return AssignmentDto.builder()
                            .giverId(giver)
                            .takerId(taker)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // If just two participants remained, giver can't be equal to taker for a one participant
    private long getSafeAnotherRandom(long giver, Set<Long> takers, Set<Long> givers) {
        if(takers.size() == 2) {
            List<Long> takersInGivers = takers.stream()
                    .filter(givers::contains)
                    .collect(Collectors.toList());

            if(takersInGivers.size() == 1){
                if (takersInGivers.get(0) == giver) {
                    return takers.stream()
                            .filter(t -> t != giver)
                            .findFirst()
                            .orElseThrow(RuntimeException::new);
                } else {
                    return takersInGivers.get(0);
                }
            }
        }

        return getAnotherRandom(giver, takers);
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
        throw new RuntimeException("A software error has occured. Please, contact the developer.");
    }

    private int generateRandomNumber(int num) {
        double randomDouble = Math.random() * num;  // Random in range [0..num)
        return (int)Math.floor(randomDouble);       // 0, 1, ..., num-1
    }
}
