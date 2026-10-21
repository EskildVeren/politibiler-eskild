package no.eskildveren.policecars;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures CORS to allow GET and PATCH requests from any origin. This is due
 * to the domain of the origin not being known
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/politibiler/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "PATCH");
            }
        };
    }
}
