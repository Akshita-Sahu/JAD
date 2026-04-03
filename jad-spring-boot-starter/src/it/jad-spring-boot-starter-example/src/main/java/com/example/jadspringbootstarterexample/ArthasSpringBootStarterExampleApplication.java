package com.example.jadspringbootstarterexample;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JADSpringBootStarterExampleApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(JADSpringBootStarterExampleApplication.class, args);
		System.out.println("xxxxxxxxxxxxxxxxxx");
		TimeUnit.SECONDS.sleep(3);
		System.exit(0);
	}

}
