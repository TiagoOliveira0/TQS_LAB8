package com.example.demo;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "application-integrationtest.properties")
public class CarControllerApiTest {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    public void resetDb() {
        carRepository.deleteAll();
    }

    @Test
    public void whenValidInput_thenCreateCar() {
        Car c1 = new Car("toyota", "yaris");

        ResponseEntity<Car> entity = restTemplate.postForEntity("/cars", c1, Car.class);

        List<Car> found = carRepository.findAll();
        assertThat(found).isEqualTo("toyota");
    }

    @Test
    public void givenCars_whenGetCars_thenStatus200()  {
        createTestEmployee("toyota", "yaris");
        createTestEmployee("renault", "clio");


        ResponseEntity<List<Car>> response = restTemplate
                .exchange("/cars", HttpMethod.GET, null, new ParameterizedTypeReference<List<Car>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(Car::getMark).containsExactly("toyota", "renault");

    }


    private void createTestEmployee(String mark, String model) {
        Car c = new Car(mark, model);
        carRepository.saveAndFlush(c);
    }
}
