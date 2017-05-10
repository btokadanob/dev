package test.aws.s3;

import java.io.IOException;
import java.util.List;

/**
 * 
 * @author Administrator
 *
 */
public interface LogTransmitterForAmazonS3 {

    /**
     * fromPath直下にある、「.log」を全て引数のbucketName内に、toRelativePath＋ファイル名でPUTする
     * 
     * @param bucketName
     * @param toPath
     * @param fromPath
     */
    void transmit(String bucketName, String toRelativePath, String fromPath) throws IOException;
    
    /**
     * 指定パス配下のフォルダ、オブジェクト一覧を参照する
     * @param bucketName
     * @param searchRoot
     * @return
     */
    List<DownloadResource> getDownloadListFromPath(String bucketName, String searchRoot) throws IOException;

    /**
     * 指定したパスのオブジェクトをダウンロードし。特定のStreamresourceに変換して返す
     * @param bucketname
     * @param downloadLink
     * @param streamClazz
     * @return
     */
    <R> R getDownloadStream(String bucketname, String downloadLink, Class<? extends R> streamClazz) throws IOException;

    /**
     * 
     * @param bucketName
     * @param deletePath
     * @return
     */
    boolean deleteObject (String bucketName, String deletePath);
}
