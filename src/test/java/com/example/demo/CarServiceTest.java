package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock( lenient = true)
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService carManagerService;

    @BeforeEach
    public void setUp() {
        Car c1 = new Car("ford", "fiesta");
        c1.setId(111L);

        Car c2 = new Car("seat", "ibiza");
        Car c3 = new Car("mercedes", "benz");

        List<Car> allCars = Arrays.asList(c1, c2, c3);

        Mockito.when(carRepository.findByMark(c1.getMark())).thenReturn(c1);
        Mockito.when(carRepository.findByMark(c2.getMark())).thenReturn(c2);
        Mockito.when(carRepository.findByMark("wrong_name")).thenReturn(null);
        Mockito.when(carRepository.findById(c1.getId())).thenReturn(Optional.of(c1));
        Mockito.when(carRepository.findAll()).thenReturn(allCars);
        Mockito.when(carRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    @Test
    public void whenValidMark_thenECarShouldBeFound() {
        String mark = "ford";
        Car found = carManagerService.getCarByMark(mark);

        assertThat(found.getMark()).isEqualTo(mark);
    }

    @Test
    public void whenInValidMark_thenCarShouldNotBeFound() {
        Car fromDb = carManagerService.getCarByMark("wrong_mark");
        assertThat(fromDb).isNull();

        verifyFindByMarkIsCalledOnce("wrong_mark");
    }

    @Test
    public void given3Cars_whengetAll_thenReturn3Records() {

        Car c1 = new Car("ford", "fiesta");
        Car c2 = new Car("seat", "ibiza");
        Car c3 = new Car("mercedes", "benz");

        List<Car> allCars = carManagerService.getAllCars();
        verifyFindAllEmployeesIsCalledOnce();
        assertThat(allCars).hasSize(3).extracting(Car::getMark).contains(c1.getMark(), c2.getMark(), c3.getMark());
    }

    @Test
    public void whenInValidId_thenCarShouldNotBeFound() {
        Car fromDb = carManagerService.getCarById(-99L);
        verifyFindByIdIsCalledOnce();
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenValidMark_thenCarShouldExist() {
        boolean doesCarExist = carManagerService.exists("ford");
        assertThat(doesCarExist).isEqualTo(true);

        verifyFindByMarkIsCalledOnce("ford");
    }

    @Test
    public void whenNonExistingMark_thenCarShouldNotExist() {
        boolean doesEmployeeExist = carManagerService.exists("some_mark");
        assertThat(doesEmployeeExist).isEqualTo(false);

        verifyFindByMarkIsCalledOnce("some_mark");
    }

    @Test
    public void whenValidId_thenCarShouldBeFound() {
        Car fromDb = carManagerService.getCarById(111L);
        assertThat(fromDb.getMark()).isEqualTo("ford");

        verifyFindByIdIsCalledOnce();
    }

    private void verifyFindByMarkIsCalledOnce(String mark) {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findByMark(mark);
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
    }

    private void verifyFindAllEmployeesIsCalledOnce() {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findAll();
    }
}
