package azure.storage.test

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class TestContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Map<String, String> environmentProperties = new HashMap<>()

    static addEnvironmentProperty(String key, String value) {
        environmentProperties.put(key, value)
    }

    @Override
    void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                environmentProperties.collect { it.getKey() + ":" + it.getValue() }
        ).applyTo(applicationContext)
    }
}
