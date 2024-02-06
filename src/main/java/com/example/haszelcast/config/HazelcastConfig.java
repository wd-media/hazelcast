package com.example.haszelcast.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.config.Config;
import com.hazelcast.config.ClasspathYamlConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        Config config = new ClasspathYamlConfig("hazelcast.yaml");
        return Hazelcast.newHazelcastInstance(config);
    }
}
