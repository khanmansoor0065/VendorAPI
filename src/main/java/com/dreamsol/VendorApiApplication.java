package com.dreamsol;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Vendor OPEN API",
				version = "1.0.0",
				description = "Vendor OPEN API documentation"
				),
		servers = @Server(
				url = "http://localhost:8080",
				description = "Vendor OPEN API"
				)
		)
public class VendorApiApplication {

	public static void main(String[] args) 
	{
		SpringApplication.run(VendorApiApplication.class, args);
	}

}
