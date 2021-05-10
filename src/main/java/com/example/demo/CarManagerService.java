package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CarManagerService {

    @Autowired
    private CarRepository carRepository;

    public Car save(Car c){
        return carRepository.save(c);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarDetails(long l){
        return carRepository.findById(l);
    }

    public Car getCarByMark(String mark) {
        return carRepository.findByMark(mark);
    }

    public Car getCarById(long l) {
        return (Car) carRepository.findById(l).orElse(null);
    }

    public boolean exists(String mark) {
        if (carRepository.findByMark(mark) != null) {
            return true;
        }
        return false;
    }
}
