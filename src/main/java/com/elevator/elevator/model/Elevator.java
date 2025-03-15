package com.elevator.elevator.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.*;


@Getter
@Setter
@AllArgsConstructor
public class Elevator   {

    private static final int MAX_FLOOR = 10;
    private String elevatorId ;
    private int currentFloor;
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


}
