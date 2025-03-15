package com.elevator.elevator.configuration;


import com.elevator.elevator.model.Elevator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}
