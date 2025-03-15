package com.elevator.elevator.controller;


import com.elevator.elevator.Service.ElevatorService;
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

    @Autowired
    public ElevatorController(ElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }

    @GetMapping("/all")
    public List<Elevator> getElevatorStatus() {
        return elevatorService.getElevators();
    }


    @GetMapping("/get-elevator")
    public Elevator getElevator(@RequestParam int floor, @RequestParam String direction) {
        log.info("Elevator requested at floor " + floor + " going " + direction);
        return elevatorService.getNearestElevator(floor, direction);
    }


    @GetMapping("/enqueue-stop")
    public void enqueuePassengerStop(@RequestParam String elevatorId, @RequestParam int  destination) {
          elevatorService.enqueuePassengerStop(elevatorId, destination);
    }

    @GetMapping("/move-elevator")
    public void moveElevator(@RequestParam String elevatorId) {
        elevatorService.moveElevator(elevatorId);
    }

}
