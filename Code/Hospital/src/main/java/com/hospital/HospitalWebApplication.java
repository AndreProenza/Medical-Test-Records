package com.hospital;

import org.jboss.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HospitalWebApplication {
	
    private static Logger log = Logger.getLogger(HospitalWebApplication.class);

    
    //TODO
    
    //iniciar a connecao http segura
	
	public static void main(String[] args) {
        log.info("Logger enabled: Entering main \n\n");
		SpringApplication.run(HospitalWebApplication.class, args);
        log.info("Exiting main");
	}

}
