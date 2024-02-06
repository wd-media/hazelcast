package com.example.haszelcast.config;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Log4j2
@Testcontainers
public class VehicleArchitectureConfigIT {

    private static final Logger logger = LoggerFactory.getLogger(VehicleArchitectureConfigIT.class);

//    private static final Logger logger = LoggerFactory.getLogger(VehicleArchitectureConfigIT.class);
    private static final DockerImageName APP_IMAGE = DockerImageName.parse("hazelcast-application-image:latest");

    @Container
    private static final GenericContainer<?> appInstanceOne = createContainer("AppInstanceOne");

    @Container
    private static final GenericContainer<?> appInstanceTwo = createContainer("AppInstanceTwo");

    @Container
    private static final GenericContainer<?> appInstanceThree = createContainer("AppInstanceThree");

    private static GenericContainer<?> createContainer(String name) {
        return new GenericContainer<>(APP_IMAGE)
                .withExposedPorts(8080)
                .withEnv("APP_NAME", name)
                .withLogConsumer(new Slf4jLogConsumer(logger));
    }

    @BeforeAll
    static void setUp() {
        // Start containers manually if not using @Container annotation
        logContainerInfo(appInstanceOne, "AppInstanceOne");
        logContainerInfo(appInstanceTwo, "AppInstanceTwo");
        logContainerInfo(appInstanceThree, "AppInstanceThree");

    }

    private static void logContainerInfo(GenericContainer<?> container, String name) {
        Integer mappedPort = container.getMappedPort(8080);
        String host = container.getHost();
        System.out.println(name + " is accessible at http://" + host + ":" + mappedPort);
        System.out.println("Container ID: " + container.getContainerId());
    }

//   @AfterAll
//    static void tearDown() {
//        // Stop containers manually if not using @Container annotation
//        appInstanceOne.stop();
//        appInstanceTwo.stop();
//        appInstanceThree.stop();
//    }
}
