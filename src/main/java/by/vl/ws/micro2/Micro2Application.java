package by.vl.ws.micro2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableRetry
public class Micro2Application {

    public static void main(String[] args) {
        SpringApplication.run(Micro2Application.class, args);
    }

}
