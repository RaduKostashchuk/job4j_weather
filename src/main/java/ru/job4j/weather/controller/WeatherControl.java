package ru.job4j.weather.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.job4j.weather.model.Weather;
import ru.job4j.weather.service.WeatherService;

import java.time.Duration;

@RestController
public class WeatherControl {

    private final WeatherService service;

    public WeatherControl(WeatherService service) {
        this.service = service;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> all() {
        Flux<Weather> data = service.findAll();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping("/get/{id}")
    public Mono<Weather> get(@PathVariable int id) {
        return service.findById(id);
    }

    @GetMapping("/hottest")
    public Flux<Weather> getHottest() {
        return service.getHottest();
    }

    @GetMapping("/greaterthen/{temperature}")
    public Flux<Weather> greaterThen(@PathVariable int temperature) {
        return service.greaterThen(temperature);
    }
}
