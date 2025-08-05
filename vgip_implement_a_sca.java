package com.vgip.integrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class VgipImplementAScaApplication {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(VgipImplementAScaApplication.class, args);
    }
}

@Configuration
class ApplicationConfig {

    // Database configuration
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(dbUrl)
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }

    // API integration configuration
    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.key}")
    private String apiKey;

    @Bean
    public RestTemplate apiTemplate() {
        RestTemplate template = new RestTemplate();
        template.getInterceptors().add(new ApiKeyInterceptor(apiKey));
        return template;
    }
}

@Component
class ApiKeyInterceptor implements ClientHttpRequestInterceptor {

    private final String apiKey;

    public ApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add("API-KEY", apiKey);
        return execution.execute(request, body);
    }
}