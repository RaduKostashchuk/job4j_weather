package ru.job4j.weather.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.job4j.weather.model.Weather;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final Map<Integer, Weather> weathers = new ConcurrentHashMap<>();

    {
        weathers.put(1, new Weather(1, "Msc", 20));
        weathers.put(2, new Weather(2, "SPb", 15));
        weathers.put(3, new Weather(3, "Bryansk", 10));
        weathers.put(4, new Weather(4, "Smolensk", 25));
        weathers.put(5, new Weather(5, "Kiev", 30));
        weathers.put(6, new Weather(6, "Minsk", 30));
    }

    public Mono<Weather> findById(int id) {
        return Mono.justOrEmpty(weathers.get(id));
    }

    public Flux<Weather> findAll() {
        return Flux.fromIterable(weathers.values());
    }

    public Flux<Weather> getHottest() {
        Flux<Weather> result = null;
        Weather hottest = weathers.values().stream().max((w1, w2)
                -> Comparator.comparingInt(Weather::getTemperature).compare(w1, w2)).orElse(null);
        if (hottest != null) {
            result = Flux.fromIterable(weathers.values().stream()
                    .filter(w -> w.getTemperature() == hottest.getTemperature())
                    .collect(Collectors.toList()));
        }
        return result;
    }

    public Flux<Weather> greaterThen(int temperature) {
        return Flux.fromIterable(weathers.values().stream().filter(w
                -> w.getTemperature() > temperature).collect(Collectors.toList()));
    }
}
