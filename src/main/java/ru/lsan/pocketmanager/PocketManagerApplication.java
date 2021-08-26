package ru.lsan.pocketmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"ru.lsan.pocketmanager"})
@ComponentScan
public class PocketManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocketManagerApplication.class, args);
	}

}
