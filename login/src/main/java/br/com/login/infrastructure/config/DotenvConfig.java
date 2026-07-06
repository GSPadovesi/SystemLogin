package br.com.login.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;


public class DotenvConfig  implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        Map<String, Object> properties = new HashMap<>();

        dotenv.entries().forEach(entry -> {
            String key = entry.getKey();

            if (environment.getProperty(key) == null) {
                properties.put(key, entry.getValue());
            }
        });

        if (!properties.isEmpty()) {
            environment.getPropertySources().addLast(new MapPropertySource("dotenv", properties));
        }
    }
}
