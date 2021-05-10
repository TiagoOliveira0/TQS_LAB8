package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarController {

    @Autowired
    private CarManagerService carManagerService;

    public CarController(CarManagerService carManagerService){
        this.carManagerService=carManagerService;
    }

    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(Car c){
        HttpStatus status = HttpStatus.OK;
        Car saved = carManagerService.save(c);
        return new ResponseEntity<>(saved, status);
    }

    @GetMapping("/cars")
    public List<Car> getAllEmployees() {
        return carManagerService.getAllCars();
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(Car c) {
        HttpStatus status = HttpStatus.OK;
        Car saved = carManagerService.getCarById(c.getId());
        return new ResponseEntity<>(saved, status);
    }

}
