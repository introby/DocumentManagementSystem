package by.intro.dms.initializer;

import lombok.experimental.UtilityClass;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MongoDBContainer;

@UtilityClass
public class Mongo {

    public static final MongoDBContainer container = new MongoDBContainer("mongo:5.0.0");

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.data.mongodb.host=" + container.getHost(),
                    "spring.data.mongodb.port=" + container.getFirstMappedPort()
            ).applyTo(applicationContext);
        }
    }
}
