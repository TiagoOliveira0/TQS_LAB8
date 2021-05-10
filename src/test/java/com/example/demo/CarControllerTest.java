package com.example.demo;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CarManagerService carManagerService;

    @Test
    public void whenPostCar_thenCreateCar( ) throws Exception {
        Car c1 = new Car("ford", "fiesta");

        when( carManagerService.save(Mockito.any()) ).thenReturn(c1);

        mvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(c1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mark", is("ford")));

        verify(carManagerService, times(1)).save(Mockito.any());

    }

    @Test
    public void givencars_whenGetcars_thenReturnJsonArray() throws Exception {
        Car c1 = new Car("ford", "fiesta");
        Car c2 = new Car("seat", "ibiza");
        Car c3 = new Car("mercedes", "benz");

        List<Car> allCars = Arrays.asList(c1, c2, c3);

        given(carManagerService.getAllCars()).willReturn(allCars);

        mvc.perform(get("/cars").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3))).andExpect(jsonPath("$[0].mark", is(c1.getMark()))).andExpect(jsonPath("$[1].mark", is(c2.getMark())))
                .andExpect(jsonPath("$[2].mark", is(c3.getMark())));
        verify(carManagerService, VerificationModeFactory.times(1)).getAllCars();

    }

}
