package by.intro.dms.car.controller;

import by.intro.dms.car.request.CarRequest;
import by.intro.dms.car.response.CarResponse;
import by.intro.dms.car.service.CarService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/autos", produces = MediaType.APPLICATION_JSON_VALUE)
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping()
    public CarResponse getAutos(CarRequest request) {
        return carService.findAll(request);
    }
}
