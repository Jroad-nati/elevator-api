package com.elevator.elevator.Service;

import com.elevator.elevator.model.Elevator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class ElevatorQueueManager {
    private final Queue<Elevator> elevatorQueue;

    @Autowired
    public ElevatorQueueManager(Queue<Elevator> elevatorQueue) {
        this.elevatorQueue = elevatorQueue;
    }
    public void addElevator(Elevator elevator) {
        this.elevatorQueue.add(elevator);
    }

    public Elevator getElevator() {
        return this.elevatorQueue.poll();
    }
}
