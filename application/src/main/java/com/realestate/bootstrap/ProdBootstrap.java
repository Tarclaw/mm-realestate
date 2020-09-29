package com.realestate.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class ProdBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdBootstrap.class);

    private static final String APARTMENT_PATH = "classpath:static.images/CA/apartment.jpg";
    private static final String BASEMENT_PATH = "classpath:static.images/CA/basement.jpg";
    private static final String GARAGE_PATH = "classpath:static.images/CA/garage.jpg";
    private static final String HOUSE_PATH = "classpath:static.images/CA/house.jpg";
    private static final String STORAGE_PATH = "classpath:static.images/CA/storage.jpg";

    private final FacilityImageBootstrap facilityImageBootstrap;

    public ProdBootstrap(FacilityImageBootstrap facilityImageBootstrap) {
        this.facilityImageBootstrap = facilityImageBootstrap;
        LOGGER.info("New instance of ProdBootstrap created.");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOGGER.trace("Enter in 'onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)' method");

        facilityImageBootstrap.saveImageInDB(6L, APARTMENT_PATH);
        facilityImageBootstrap.saveImageInDB(7L, BASEMENT_PATH);
        facilityImageBootstrap.saveImageInDB(8L, GARAGE_PATH);
        facilityImageBootstrap.saveImageInDB(9L, HOUSE_PATH);
        facilityImageBootstrap.saveImageInDB(10L, STORAGE_PATH);

        LOGGER.trace("'onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)' executed successfully.");
    }

}
