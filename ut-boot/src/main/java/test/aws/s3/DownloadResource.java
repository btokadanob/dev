package test.aws.s3;

import java.io.Serializable;

import lombok.Data;

@Data
public class DownloadResource implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -944747570381234944L;

    private boolean isFile;

    private String name;
    
    private long size;
    
    private byte[] data;

}
