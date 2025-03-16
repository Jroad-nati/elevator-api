package com.elevator.elevator.configuration;


import com.elevator.elevator.model.Elevator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class StartElevator {
    @Bean
    public List<Elevator> elevators() {
        return Arrays.asList(
                new Elevator("A"),
                new Elevator("B"),
                new Elevator("C"),
                new Elevator("D"),
                new Elevator("E")
        );
    }

    // This queue will be used to store the elevators in the order of their priority
    // Did not finish full implementation
    @Bean
    public Queue<Elevator> elevatorQueue() {
        return new LinkedList<>(); // Using LinkedList as a FIFO queue
    }
}
