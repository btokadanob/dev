package data.sample;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import data.sample.api.process01.DataProcess01Resource;
import data.sample.api.process01.DataProcess01RestController;


@SpringBootTest(classes=MockConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DataProcess01RestControllerTest {

    @Autowired
    private DataProcess01RestController target;
    
    @Test
    public void testUpdateSampleData01 () throws Exception {
        
        DataProcess01Resource resource = new DataProcess01Resource();
        resource.setSampleData("test-data01");
        resource.setUserId("test-id01");
        
        target.updateSampleData(resource);
    }
}
