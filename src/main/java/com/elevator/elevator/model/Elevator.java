package com.elevator.elevator.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.*;


@Getter
@Setter
@AllArgsConstructor
public class Elevator   {

    private String elevatorId ;
    private Integer currentFloor;
    private ElevatorState elevatorState;
    private  TreeSet<Integer> moveUpQueue ;  // I choose tree bc of it ordered and avoid duplicate stop
    private TreeSet<Integer> moveDownQueue;

    public Elevator(String id) {
        this.elevatorId = id;
        this.currentFloor = 0;
        this.elevatorState = ElevatorState.IDLE;
        this.moveUpQueue = new TreeSet<>();
        this.moveDownQueue = new TreeSet<>();
    }

    public void setElevatorInstanceState(Elevator elevator, ElevatorState elevatorState) {
       elevator.setElevatorState(elevatorState);
    }

    public void setElevatorInstanceState(Elevator elevator, ElevatorState elevatorState,int floor) {
        elevator.setElevatorState(elevatorState);
        elevator.setCurrentFloor(floor);
    }


}
