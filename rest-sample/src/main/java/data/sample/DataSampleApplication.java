package data.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DataSampleApplication extends org.springframework.boot.web.support.SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(DataSampleApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DataSampleApplication.class);
    }

}
