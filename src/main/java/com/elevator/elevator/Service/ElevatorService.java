package com.elevator.elevator.Service;


import com.elevator.elevator.model.Elevator;
import com.elevator.elevator.model.ElevatorState;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter
@Service
@Slf4j
public class ElevatorService {

    private final static String DIRECTION_UP = "UP";
    private final static String DIRECTION_DOWN = "DOWN";
    private final static List<String> logs= new ArrayList<>(); //Used only to show the steps in the client when the elevator is moving

    private final List<Elevator> elevators;

    public ElevatorService(List<Elevator> elevators) {
        this.elevators = elevators;
    }


    public Elevator getNearestElevator(int floor, String direction) {
        // Choose the nearest optimal elevator and make available for the users
        Elevator nearestElevator = findNearestOptimalElevator(floor, direction);
        // If a nearest elevator is found, set its state and floor accordingly
        if (nearestElevator != null) {
            setElevatorState(nearestElevator, direction, floor);
        }
        return nearestElevator;
    }

    private Elevator findNearestOptimalElevator(int floor, String direction) {
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
        return nearestElevator ;
    }

    private boolean isElevatorSuitableForDirection(Elevator elevator, String direction, int floor) {
        //Assumption: take elevator which is free or in the direction of the request
        //It is possible to make more efficient  usage.
        return elevator.getElevatorState().equals(ElevatorState.IDLE) ||
                ((direction.equalsIgnoreCase(DIRECTION_UP) && elevator.getCurrentFloor() <= floor) || (direction.equalsIgnoreCase(DIRECTION_DOWN) && elevator.getCurrentFloor() >= floor));
    }

    private void setElevatorState(Elevator elevator, String direction, int floor) {
        ElevatorState elevatorState = direction.equalsIgnoreCase(DIRECTION_UP) ? ElevatorState.UP : ElevatorState.DOWN;
        elevator.setElevatorState(elevatorState);
        elevator.setCurrentFloor(floor);
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


    public List<String> moveElevator(String elevatorId) {
        logs.clear();
        Optional<Elevator> elevator = elevators.stream().filter(e -> e.getElevatorId().equals(elevatorId)).findFirst();
        if (elevator.isEmpty()) {
            throw new IllegalArgumentException("There is no elevator with this id");
        }
        //Assumption: The elevator is moving first in the direction of where it is requested in the very first time
        if (elevator.get().getElevatorState().name().equalsIgnoreCase("up") && !elevator.get().getMoveUpQueue().isEmpty()) {
            moveElevatorUP(elevator.get());
        } else if (elevator.get().getElevatorState().name().equalsIgnoreCase("down") && !elevator.get().getMoveDownQueue().isEmpty()) {
            moveElevatorDOWN(elevator.get());
        }
        return logs;
    }


    //Need more refactoring and break down
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
