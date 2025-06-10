package com.sp.cof;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CardsOfFateApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsOfFateApplication.class, args);

		log.info("\n===================================="
			   + "\n                                    "
			   + "\n   CoF APP                          "
			   + "\n                                    "
			   + "\n===================================="
		);
	}

}
