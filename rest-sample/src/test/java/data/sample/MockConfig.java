package data.sample;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import data.sample.api.process01.DataProcess01RestController;
import data.sample.process01.domain.model.DataProcess01Bean;
import data.sample.process01.domain.service.DataProcess01Service;

@Configuration
public class MockConfig {

    @Bean
    public DataProcess01RestController getController() {
        DataProcess01RestController controller = new DataProcess01RestController();
        return controller;
    }

    @Bean
    public DataProcess01Service getService01() {
        DataProcess01Service mock =  mock(DataProcess01Service.class);
        DataProcess01Bean response = new DataProcess01Bean();
        response.setSampleDataRaw("test01-responseData");
        response.setUserId("test01-responseId");
        when(mock.updateUserSampleData(any(DataProcess01Bean.class))).thenReturn(response);
        return mock;
    }
}
