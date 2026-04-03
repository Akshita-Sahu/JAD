package com.example.jadspringboot3starterexample;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JADSpringBoot3StarterExampleApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(JADSpringBoot3StarterExampleApplication.class, args);
		System.out.println("xxxxxxxxxxxxxxxxxx");
		TimeUnit.SECONDS.sleep(3);
		System.exit(0);
	}

}
