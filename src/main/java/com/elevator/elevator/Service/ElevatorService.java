package com.elevator.elevator.Service;


import com.elevator.elevator.model.Constant;
import com.elevator.elevator.model.Elevator;
import com.elevator.elevator.model.ElevatorState;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Getter
@Service
@Slf4j
public class ElevatorService {

    private final List<Elevator> elevators;
    private final LogService logService;

    public ElevatorService(List<Elevator> elevators, LogService logService) {
        this.elevators = elevators;
        this.logService = logService;
    }


    public Elevator getNearestOptimalElevator(int floor, String direction) {
        logService.clearLogs(); // clear out and store new logs for each elevator request
        Elevator nearestElevator = findElevator(floor, direction);
        if (nearestElevator != null) {
            ElevatorState elevatorState = direction.equalsIgnoreCase(Constant.DIRECTION_UP) ? ElevatorState.UP : ElevatorState.DOWN;
            nearestElevator.setElevatorInstanceState(nearestElevator, elevatorState, floor);
            return nearestElevator;
        }
        return elevators.stream().findFirst().get();  //must get one elevator
    }

    private Elevator findElevator(int floor, String direction) {
        Elevator nearestElevator = null;
        int minimumDistance = Integer.MAX_VALUE;
        for (Elevator elevator : elevators) {
            int distance = Math.abs(elevator.getCurrentFloor() - floor);
            if (isElevatorSuitableForDirection(elevator, direction, floor)) {
                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    nearestElevator = elevator;
                }
            }
        }
        return nearestElevator;
    }

    private boolean isElevatorSuitableForDirection(Elevator elevator, String direction, int floor) {
        //Assumption: take elevator which is free or in the direction of the request
        return elevator.getElevatorState().equals(ElevatorState.IDLE) ||
                ((direction.equalsIgnoreCase(Constant.DIRECTION_UP) && elevator.getCurrentFloor() <= floor) || (direction.equalsIgnoreCase(Constant.DIRECTION_DOWN) && elevator.getCurrentFloor() >= floor));
    }
}
