package test.aws.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.util.StringUtils;

@Component
public class AmazonS3ClientFactory {

    private static AmazonS3ClientFactory factory = new AmazonS3ClientFactory();
    
    @Autowired
    private AmazonS3BuilderDef builderDef;
    private AmazonS3 s3Client;

    private AmazonS3ClientFactory() {
        init();
    }

    void init() {
        this.s3Client = this.builderDef.def().build();
    }

    public static AmazonS3 getClient() {
        return factory.getInnerS3Client();
    }

    private AmazonS3 getInnerS3Client() {
        return this.s3Client;
    }


    interface AmazonS3BuilderDef {
        AmazonS3ClientBuilder def ();
    }

    @Component
    @Profile("local")
    class Amazons3BuilderLocalDef implements AmazonS3BuilderDef{
        @Override
        public AmazonS3ClientBuilder def() {
            System.out.println("ローカル環境で起動");
            AmazonS3ClientBuilder s3builder = AmazonS3Client.builder();
            s3builder.setCredentials(new ClasspathPropertiesFileCredentialsProvider());
            s3builder.setRegion(Regions.US_EAST_1.getName());
            return s3builder;
        }
        
    }
}
