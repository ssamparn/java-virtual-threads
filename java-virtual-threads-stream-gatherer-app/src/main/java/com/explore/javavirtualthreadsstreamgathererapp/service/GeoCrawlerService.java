package com.explore.javavirtualthreadsstreamgathererapp.service;

import com.explore.javavirtualthreadsstreamgathererapp.client.GeoDataClient;
import com.explore.javavirtualthreadsstreamgathererapp.entity.CityEntity;
import com.explore.javavirtualthreadsstreamgathererapp.model.City;
import com.explore.javavirtualthreadsstreamgathererapp.repository.CityRepository;
import com.explore.javavirtualthreadsstreamgathererapp.util.GatherersUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.stream.Gatherers;

@Slf4j
@Service
public class GeoCrawlerService implements CommandLineRunner {

    private final GeoDataClient geoDataClient;
    private final CityRepository cityRepository;

    public GeoCrawlerService(final GeoDataClient geoDataClient,
                             final CityRepository cityRepository) {
        this.geoDataClient = geoDataClient;
        this.cityRepository = cityRepository;
    }

    @Override
    public void run(String... args) {
        log.info("records count before: {}", this.cityRepository.count());
        this.cityRepository.deleteAll();

        this.geoDataClient.getRegions()
                .stream()
                .flatMap(region -> region.subRegions().stream())
                .gather(GatherersUtil.executeConcurrent(this.geoDataClient::getSubRegion))
                .flatMap(subRegion -> subRegion.countries().stream())
                .gather(GatherersUtil.executeConcurrent(this.geoDataClient::getCountry))
                .flatMap(country -> country.states().stream())
                .gather(GatherersUtil.executeConcurrent(this.geoDataClient::getState))
                .flatMap(state -> state.cities().stream())
                .gather(GatherersUtil.executeConcurrent(this.geoDataClient::getCity))
                .map(this::toEntity)
                .gather(Gatherers.windowFixed(1000))
                .forEach(this.cityRepository::saveAll);
        log.info("records count after: {}", this.cityRepository.count());
    }

    /**
     * Is it ok to use external id as primary key?
     * Well, it depends! In this case, It is ok assuming the external id will be stable and unique!
     * Or generate your own id and we can introduce one more column in our table - external_id or geo_city_id - to map city.id()
     * */
    private CityEntity toEntity(City city) {
        var entity = new CityEntity();
        entity.setId(city.id());
        entity.setName(city.name());
        entity.setCountry(city.country());
        entity.setState(city.state());
        entity.setLatitude(city.latitude());
        entity.setLongitude(city.longitude());
        return entity;
    }
}
