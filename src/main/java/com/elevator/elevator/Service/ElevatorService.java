package com.elevator.elevator.Service;


import com.elevator.elevator.model.Elevator;
import com.elevator.elevator.model.ElevatorState;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Getter
@Service
@Slf4j
public class ElevatorService {

    private final static String DIRECTION_UP = "UP";
    private final static String DIRECTION_DOWN = "DOWN";

    private final List<Elevator> elevators;

    public ElevatorService(List<Elevator> elevators) {
        this.elevators = elevators;
    }


    public Elevator getNearestElevator(int floor, String direction) {
        // Choose the nearest optimal elevator and make available for the users
        Elevator nearestElevator = elevators.getFirst();
        int mdistance = Integer.MAX_VALUE;
        for (Elevator e : elevators) {
            int distance = Math.abs(e.getCurrentFloor() - floor);
            if (e.getElevatorState().equals(ElevatorState.IDLE) ||
                    ((direction.equalsIgnoreCase(DIRECTION_UP) && e.getCurrentFloor() <= floor) || (direction.equalsIgnoreCase(DIRECTION_DOWN) && e.getCurrentFloor() >= floor))) {
                if (distance <= mdistance) {
                    mdistance = distance;
                    nearestElevator = e;
                }
            }
        }
        //Set the state of the elevator according to the users direction and floor number
        ElevatorState elevatorState = direction.equalsIgnoreCase(DIRECTION_UP) ? ElevatorState.UP : ElevatorState.DOWN;
        nearestElevator.setElevatorState(elevatorState);
        nearestElevator.setCurrentFloor(floor);
        return nearestElevator;
    }

    public void enqueuePassengerStop(String elevatorId, int destination) {
        log.info("Elevator {} is in the queue and heading to floor {}", elevatorId, destination);
        Optional<Elevator> elevator = elevators.stream().filter(e -> e.getElevatorId().equals(elevatorId)).findFirst();
        if (elevator.isPresent() && elevator.get().getCurrentFloor() < destination) {
            elevator.get().getMoveUpQueue().add(destination);
        } else if (elevator.isPresent() && elevator.get().getCurrentFloor() > destination) {
            elevator.get().getMoveDownQueue().add(destination);
        }
    }


    public void moveElevator(String elevatorId) {
        Optional<Elevator> elevator = elevators.stream().filter(e -> e.getElevatorId().equals(elevatorId)).findFirst();
        if (elevator.isEmpty()) {
            throw new IllegalArgumentException("There is no elevator with this id");
        }
        //The elevator is moving first in the direction of where it is requested in the very first time
        if (elevator.get().getElevatorState().name().equalsIgnoreCase("up") && !elevator.get().getMoveUpQueue().isEmpty()) {
            moveElevatorUP(elevator.get());
        } else if (elevator.get().getElevatorState().name().equalsIgnoreCase("down") && !elevator.get().getMoveDownQueue().isEmpty()) {
            moveElevatorDOWN(elevator.get());
        }
    }


    private void moveElevatorUP(Elevator elevator) {
        while (!elevator.getMoveUpQueue().isEmpty()) {
            Integer stop = elevator.getMoveUpQueue().pollFirst();
            if(stop == null) continue;
            log.info("Elevator {} moving up to floor {}.", elevator.getElevatorId(), stop);
            elevator.setElevatorState(ElevatorState.UP);
            log.info("Elevator {} stopped at floor {}.", elevator.getElevatorId(), stop);
            elevator.setElevatorState(ElevatorState.STOPPED);
            elevator.setCurrentFloor(stop);
        }
        log.debug("Elevator down list", elevator.getMoveDownQueue());
        //Will continue moving down if there is any floor in the queue
        if (!elevator.getMoveDownQueue().isEmpty()) {
            moveElevatorDOWN(elevator);
        } else {
            log.info("Elevator {} is idle now and is waiting for passengers request.", elevator.getElevatorId());
            elevator.setElevatorState(ElevatorState.IDLE);
        }
    }



    private void moveElevatorDOWN(Elevator elevator) {
        while (!elevator.getMoveDownQueue().isEmpty()) {
            Integer stop = elevator.getMoveDownQueue().pollLast();
            if(stop == null) continue;
            log.info("Elevator {} moving down to floor {}.", elevator.getElevatorId(), stop);
            elevator.setElevatorState(ElevatorState.UP);
            log.info("Elevator {} stopped at floor {}.", elevator.getElevatorId(), stop);
            elevator.setElevatorState(ElevatorState.STOPPED);
            elevator.setCurrentFloor(stop);
        }

        // Will continue moving up if there is any floor in the queue
        if (!elevator.getMoveUpQueue().isEmpty()) {
            moveElevatorUP(elevator);
        } else {
            log.info("Elevator {} is idle now and is waiting for passengers request.", elevator.getElevatorId());
            elevator.setElevatorState(ElevatorState.IDLE);
        }
    }
}
