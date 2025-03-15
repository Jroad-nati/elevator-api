package com.elevator.elevator;

import com.elevator.elevator.model.Elevator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElevatorApplication {

	public static void main(String[] args) {

		System.out.println("Elevator system started");
		SpringApplication.run(ElevatorApplication.class, args);
	}

}
