package com.udacity.vehicles.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import java.net.URI;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;

    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        Car car = getCar();
        car.setId(1L);
        given(carService.save(any(Car.class))).willAnswer(invoke -> {
            Car savedCar = invoke.getArgument(0);
            savedCar.setId(1L);
            return savedCar;
        });
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests for successful creation of new car in the system
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        Car car = getCar();
        mvc.perform(
                post(new URI("/cars"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print()).andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        mvc.perform(
                get(new URI("/cars"))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("2018")))
                .andExpect(content().string(containsString("Chevrolet")))
                .andExpect(content().string(containsString("Impala")));
    }

    /**
     * Tests the read operation for a single car by ID.
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        mvc.perform(
                get(new URI("/cars/1"))
                                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.details.modelYear", is(2018)))
                .andExpect(jsonPath("$.details.manufacturer.name", is("Chevrolet")))
                .andExpect(jsonPath("$.details.model", is("Impala")));
    }

    /**
     * Tests the deletion of a single car by ID.
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar() throws Exception {
        mvc.perform(delete(new URI("/cars/1"))
                        .accept(MediaType.APPLICATION_JSON_UTF8)).andDo(print()).andExpect(status().isNoContent());
    }

    /**
     * Tests the update of a single car by ID.
     * @throws Exception if the update operation of a vehicle fails
     */
    @Test
    public void updateCar() throws Exception {
        Car car = getCar();
        car.getDetails().setModelYear(2024);
        car.getDetails().setProductionYear(2024);
        car.getDetails().setMileage(0);
        car.getDetails().setExternalColor("Ultra Red");
        car.getDetails().setEngine("AC Motor");
        car.getDetails().setFuelType("Electric");
        car.getDetails().setManufacturer(new Manufacturer(106, "Tesla"));
        car.getDetails().setModel("Model 3");
        car.setCondition(Condition.NEW);
        mvc.perform(
                put(new URI("/cars/1"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.details.modelYear", is(2024)))
                .andExpect(jsonPath("$.details.productionYear", is(2024)))
                .andExpect(jsonPath("$.details.mileage", is(0)))
                .andExpect(jsonPath("$.details.externalColor", is("Ultra Red")))
                .andExpect(jsonPath("$.details.engine", is("AC Motor")))
                .andExpect(jsonPath("$.details.fuelType", is("Electric")))
                .andExpect(jsonPath("$.details.manufacturer.code", is(106)))
                .andExpect(jsonPath("$.details.manufacturer.name", is("Tesla")))
                .andExpect(jsonPath("$.details.model", is("Model 3")))
                .andExpect(jsonPath("$.condition", is(Condition.NEW.name())));
    }

    /**
     * Creates an example Car object for use in testing.
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}