package com.telefonicatech.cmdbChile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CmdbChileApplication {

	public static void main(String[] args) {
        SpringApplication.run(CmdbChileApplication.class, args);
	}

}
