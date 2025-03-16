package com.elevator.elevator.Service;


import com.elevator.elevator.model.Elevator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Slf4j
public class QueueService {
    private final ElevatorService elevatorService;
    private final List<Elevator> elevators;
    private final LogService logService;

    public QueueService(ElevatorService elevatorService, List<Elevator> elevators, LogService logService) {
        this.elevatorService = elevatorService;
        this.elevators = elevators;
        this.logService = logService;
    }

    public void enqueuePassengerStop(String elevatorId, int destination) {

        logService.addLogs(String.format("Elevator %s is in the queue and heading to floor %s", elevatorId, destination));
        Optional<Elevator> elevator = elevators.stream().filter(e -> e.getElevatorId().equals(elevatorId)).findFirst();
        if (elevator.isPresent() && elevator.get().getCurrentFloor() < destination) {
            elevator.get().getMoveUpQueue().add(destination);
        } else if (elevator.isPresent() && elevator.get().getCurrentFloor() > destination) {
            elevator.get().getMoveDownQueue().add(destination);
        }
    }
}
