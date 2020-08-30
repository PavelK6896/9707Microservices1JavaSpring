package app.web.pavelk.microservices2.admin3;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableAdminServer
public class Spring3AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(Spring3AdminApplication.class, args);
    }
}
