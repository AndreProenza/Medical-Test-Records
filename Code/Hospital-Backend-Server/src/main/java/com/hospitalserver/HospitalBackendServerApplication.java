package com.hospitalserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hospitalserver.server.Server;

@SpringBootApplication
public class HospitalBackendServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalBackendServerApplication.class, args);
		Server.run();
	}

}
