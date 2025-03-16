package com.elevator.elevator.Service;

import com.elevator.elevator.model.Constant;
import com.elevator.elevator.model.Elevator;
import com.elevator.elevator.model.ElevatorState;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
@Slf4j
public class ElevatorMotionService {

    private final ElevatorService elevatorService;
    private final List<Elevator> elevators;
    private final LogService logService;

    public ElevatorMotionService(ElevatorService elevatorService, List<Elevator> elevators, LogService logService) {
        this.elevatorService = elevatorService;
        this.elevators = elevators;
        this.logService = logService;
    }

    public void moveElevator(String elevatorId) {

        Optional<Elevator> elevator = elevators.stream().filter(e -> e.getElevatorId().equals(elevatorId)).findFirst();
        if (elevator.isEmpty()) {
            throw new IllegalArgumentException("There is no elevator with this id");
        }
        //Assumption: The elevator is moving first in the direction of where it is requested in the very first time
        if (elevator.get().getElevatorState().name().equalsIgnoreCase(Constant.DIRECTION_UP)) {
            moveElevatorUP(elevator.get());
        } else if (elevator.get().getElevatorState().name().equalsIgnoreCase(Constant.DIRECTION_DOWN)) {
            moveElevatorDOWN(elevator.get());
        }
    }


    private void moveElevatorUP(Elevator elevator) {
        while (!elevator.getMoveUpQueue().isEmpty()) {
            Integer stop = elevator.getMoveUpQueue().pollFirst();
            if (stop == null) continue;
            logService.addLogs(String.format("Elevator %s is moving up.", elevator.getElevatorId()));
            elevator.setElevatorInstanceState(elevator, ElevatorState.UP);
            logService.addLogs(String.format("Elevator %s stopped at floor %d.", elevator.getElevatorId(), stop));
            elevator.setElevatorInstanceState(elevator, ElevatorState.STOPPED, stop);
        }
        //Will continue moving down if there is any floor in the queue
        if (!elevator.getMoveDownQueue().isEmpty()) {
            moveElevatorDOWN(elevator);
        } else {
            logService.addLogs(String.format("Elevator %s is idle.", elevator.getElevatorId()));
            elevator.setElevatorInstanceState(elevator, ElevatorState.IDLE);
        }
    }


    private void moveElevatorDOWN(Elevator elevator) {
        while (!elevator.getMoveDownQueue().isEmpty()) {
            Integer stop = elevator.getMoveDownQueue().pollLast();
            if (stop == null) continue;
            logService.addLogs(String.format("Elevator %s is moving down.", elevator.getElevatorId()));
            elevator.setElevatorInstanceState(elevator, ElevatorState.DOWN);
            logService.addLogs(String.format("Elevator %s stopped at floor %d.", elevator.getElevatorId(), stop));
            elevator.setElevatorInstanceState(elevator, ElevatorState.STOPPED, stop);
        }
        // Will continue moving up if there is any floor in the queue
        if (!elevator.getMoveUpQueue().isEmpty()) {
            moveElevatorUP(elevator);
        } else {
            logService.addLogs(String.format("Elevator %s is idle.", elevator.getElevatorId()));
            elevator.setElevatorInstanceState(elevator, ElevatorState.IDLE);
        }
    }

}
