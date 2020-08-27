package app.web.pavelk.microservices1.client1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping("/")
    public String main() {
        return appName;
    }

    @GetMapping("/greeting")
    public String greeting() {
        return String.format("Hello from '%s'!", appName);
    }

    @GetMapping("/param/{id}")
    public String parametrized(@PathVariable(value = "id") String id) {
        return "echo: " + id;
    }
}
