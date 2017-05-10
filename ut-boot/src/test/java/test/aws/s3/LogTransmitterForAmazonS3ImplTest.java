package test.aws.s3;

import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import test.aws.conf.AppConfig;

@SpringBootTest(classes = AppConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LogTransmitterForAmazonS3ImplTest {

    @Autowired
    @Qualifier("#log-transmitter")
    LogTransmitterForAmazonS3 target;

    @Test
    public void testTransmit01() {
        try {
            target.transmit("bucketName", "top", "/tmp");
            fail("exception must be occured.");
        } catch (ExceptionInInitializerError e) {
            // success!
        } catch (IOException e) {
            fail("unexpected exception.", e);
        }

    }
}
