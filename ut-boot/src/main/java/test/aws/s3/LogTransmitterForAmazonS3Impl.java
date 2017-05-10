package test.aws.s3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * 
 * @author Administrator
 *
 */
@Component
public class LogTransmitterForAmazonS3Impl implements LogTransmitterForAmazonS3 {
    private static final String TAGRET_SUFFIX = ".log";
    private static final String PATH_DELIMITTER = "/";
    private static final long MAX_DL_BYTES = 5120000L;
    /**
     * {@inheritDoc}
     */
    @Override
    public void transmit(String bucketName, String toPath, String fromPath) throws IOException{
        System.out.println("transmit to S3 via bucketeer start.");

        Files.list(new File(fromPath).toPath())
        .filter(p -> p.toFile().isFile())
        .map(p -> p.getFileName().toString())
        .filter(s -> s.endsWith(TAGRET_SUFFIX))
        .forEach(f -> {
            // putting to Amazon S3. every override.
            String tp =  toPath + "/" + f;
            String fp = fromPath + "/" + f;
            System.out.println("bucketName=" + bucketName + " toPath=" + tp + " fromPath=" + fp);
            AmazonS3ClientFactory.getClient().putObject(bucketName,tp, new File(fp));
        });
        System.out.println("transmit to S3 via bucketeer end.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DownloadResource> getDownloadListFromPath(String bucketName, String searchRoot) {
        
        System.out.println("get file-list from S3 via bucketeer start. searchRoot = " + searchRoot);
        
        ListObjectsRequest listObj = new ListObjectsRequest().withBucketName(bucketName).withPrefix(searchRoot)
                .withDelimiter(PATH_DELIMITTER);
        List<DownloadResource> dwnloadList = new ArrayList<>();
        ObjectListing objLiting = AmazonS3ClientFactory.getClient().listObjects(listObj);

        for (String prefix : objLiting.getCommonPrefixes()) {
            DownloadResource dwnRsrc = new DownloadResource();
            dwnRsrc.setName(prefix);
            dwnRsrc.setFile(false);
            dwnloadList.add(dwnRsrc);
        }
        for (S3ObjectSummary summary : objLiting.getObjectSummaries()) {
            DownloadResource dwnRsrc = new DownloadResource();
            dwnRsrc.setName(summary.getKey());
            dwnRsrc.setFile(true);
            dwnRsrc.setSize(summary.getSize());
            dwnloadList.add(dwnRsrc);
        }

        System.out.println("get file-list from S3 via bucketeer end. searchRoot = " + searchRoot);
        return dwnloadList;
    }

    @Override
    public <R> R getDownloadStream(String bucketname, String downloadLink, Class<? extends R> resourClaz) {
        System.out.println("get donwload-lik from S3 via bucketeer start. downloadLink = " + downloadLink);

        S3Object s3obj = AmazonS3ClientFactory.getClient().getObject(new GetObjectRequest(bucketname, downloadLink).withRange(0, MAX_DL_BYTES));
        InputStreamResource resource = new InputStreamResource(s3obj.getObjectContent());

        System.out.println("get donwload-lik from S3 via bucketeer end. downloadLink = " + downloadLink);
        return resourClaz.cast(resource);
    }

    @Override
    public boolean deleteObject(String bucketName, String deletePath) {

        System.out.println("delete donwload-lik from S3 via bucketeer start. deletePath = " + deletePath);

        AmazonS3ClientFactory.getClient().deleteObject(new DeleteObjectRequest(bucketName, deletePath));

        System.out.println("delete donwload-lik from S3 via bucketeer end. deletePath = " + deletePath);

        return true;
    }
}
