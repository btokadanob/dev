package test.aws.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import test.aws.s3.LogTransmitterForAmazonS3;
import test.aws.s3.LogTransmitterForAmazonS3Impl;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {
    @Bean(name = "#log-transmitter")
    public LogTransmitterForAmazonS3 getAmznS3Transmitter() {
        // 1.bucket-name 2.from logpath 3.to s3path s4 corelog name
        LogTransmitterForAmazonS3 transmitter = new LogTransmitterForAmazonS3Impl();
        return transmitter;
    }
}
