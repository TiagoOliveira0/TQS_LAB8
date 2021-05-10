package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CarRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    public void whenFindFordCarByMark_thenReturnFordCar() {
        Car c1 = new Car("ford", "fiesta");
        entityManager.persistAndFlush(c1);

        Car found = carRepository.findByMark(c1.getMark());
        assertThat( found ).isEqualTo(c1);
    }

    @Test
    public void whenInvalidCarMark_thenReturnNull() {
        Car fromDb = carRepository.findByMark("Does Not Exist");
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenFindEmployedByExistingId_thenReturnEmployee() {
        Car c1 = new Car("test", "test");
        entityManager.persistAndFlush(c1);

        Car fromDb = (Car) carRepository.findById(c1.getId()).orElse(null);
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getModel()).isEqualTo( c1.getModel());
    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        Car fromDb = (Car) carRepository.findById(-111L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void givenSetOfCars_whenFindAll_thenReturnAllCars() {
        Car c1 = new Car("ford", "fiesta");
        Car c2 = new Car("seat", "ibiza");
        Car c3 = new Car("mercedes", "benz");

        entityManager.persist(c1);
        entityManager.persist(c2);
        entityManager.persist(c3);
        entityManager.flush();

        List<Car> allCars = carRepository.findAll();

        assertThat(allCars).hasSize(3).extracting(Car::getMark).containsOnly(c1.getMark(), c2.getMark(), c3.getMark());
    }
}
