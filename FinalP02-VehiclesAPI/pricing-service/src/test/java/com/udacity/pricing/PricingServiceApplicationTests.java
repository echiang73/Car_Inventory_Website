package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricingServiceApplicationTests {

	private static final Long vehicleId = Long.valueOf(100);

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PriceRepository priceRepository;

	@Test
	public void getPriceByVehicleId() {
		priceRepository.save(new Price("USD", BigDecimal.valueOf(55000), vehicleId));
		Price repoPrice = priceRepository.findById(vehicleId).get();

		String url = "http://localhost:" + port + "/prices/" + vehicleId;
		ResponseEntity<Price> response = restTemplate.getForEntity(url, Price.class);

		assertEquals(response.getStatusCodeValue(), 200);
		assertNotEquals(response.getStatusCodeValue(), 400);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertNotEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
		assertNotNull(response.getBody());
		assertEquals(response.getBody().getCurrency(), repoPrice.getCurrency());
		assertEquals(response.getBody().getPrice(), repoPrice.getPrice());

		priceRepository.deleteById(vehicleId);
	}

}
