package com.elevator.elevator.Service;

import com.elevator.elevator.model.Elevator;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class ElevatorQueueManager {
    private final Queue<Elevator> elevatorQueue;  //To handle elevators request in FIFO-Get

    public ElevatorQueueManager(Queue<Elevator> elevatorQueue) {
        this.elevatorQueue = new LinkedList<>();
    }

    public void addElevator(Elevator elevator) {
        this.elevatorQueue.add(elevator);
    }

    public Elevator getElevator() {
        return this.elevatorQueue.poll();
    }
}
