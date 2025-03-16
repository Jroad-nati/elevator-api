package com.elevator.elevator.controller;


import com.elevator.elevator.Service.ElevatorMotionService;
import com.elevator.elevator.Service.ElevatorService;
import com.elevator.elevator.Service.LogService;
import com.elevator.elevator.Service.QueueService;
import com.elevator.elevator.model.Elevator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elevator")
@Slf4j
public class ElevatorController {
    private final ElevatorService elevatorService;
    private final QueueService queueService;
    private final ElevatorMotionService elevatorMotionService ;
    private final LogService logService;

    @Autowired
    public ElevatorController(ElevatorService elevatorService, QueueService queueService, ElevatorMotionService elevatorMotionService, LogService logService) {
        this.elevatorService = elevatorService;
        this.queueService = queueService;
        this.elevatorMotionService = elevatorMotionService;
        this.logService = logService;
    }

    //Possible to use RestTemplate and create request id.But  it is overkill and need more time
    // I didn't implement for error handling
    // This project handle only one elevator request at a time

    @GetMapping("/all")
    public List<Elevator> getElevatorStatus() {
        log.info("Getting all elevators");
        return elevatorService.getElevators();
    }


    @GetMapping("/get-elevator")
    public Elevator getElevator(@RequestParam int floor, @RequestParam String direction) {
        log.info("Elevator requested at floor " + floor + " going " + direction);
        return elevatorService.getNearestOptimalElevator(floor, direction);
    }


    @GetMapping("/enqueue-stop")
    public void enqueuePassengerStop(@RequestParam String elevatorId, @RequestParam int  destination) {
       log.info("Enqueueing stop for elevator " + elevatorId + " to floor " + destination);
        queueService.enqueuePassengerStop(elevatorId, destination);
    }

    @GetMapping("/move-elevator")
    public void moveElevator(@RequestParam String elevatorId) {
        log.info("Moving elevator " + elevatorId);
        elevatorMotionService.moveElevator(elevatorId);
    }


    @GetMapping("/elevator-logs")
    public List<String> getLogs() {
        log.info("Getting logs");
        return  logService.getLogs();
    }

}
