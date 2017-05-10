package test.aws.s3;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public class LogTransmitScheduler implements Runnable {
    private final String bucketName;
    private final String fromPath;
    private final String toPath;
    private final String corelogName;
    private volatile boolean isForceStopped = false;
    private DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    int retryCount = 10;

    int retryInterval = 10000;

    @Autowired(required = true)
    private LogTransmitterForAmazonS3 transmitter;

    @Autowired
    private ApplicationEventPublisher publisher;

    private AtomicLong lastModified = new AtomicLong(0L);
    
    public LogTransmitScheduler(String bucketName, String fromPath, String toPath , String corelogName) {
        this.bucketName = bucketName;
        this.fromPath = fromPath;
        this.toPath = toPath;
        this.corelogName = corelogName;
    }

    /**
     * ThreadPoolTaskSchedulerによる任意のdelay間隔で実行される。単一スレッドのためスレッドセーフ<br>
     * XXX don't throw exception & don't output error-log of logback, cause its
     * thread is separated from online-thread.<br>
     * and this component is kind of logging-api.<br>
     * should publish error-event ( use Spring application-listener)
     */
    @Override
    public void run() {
        if (!this.isForceStopped) {
            System.out.println("log-transmit is called by scheduler.");
            File coremonitor = new File(this.fromPath + "/" + this.corelogName);
            if (!(this.lastModified.get() == 0L) && coremonitor.lastModified() == this.lastModified.get()) {
                System.out.println("corelog { " + this.corelogName +" } is not modified.");
                return;
            }
            try {
                this.transmitter.transmit(this.bucketName, this.toPath, this.fromPath);
                this.lastModified.set(coremonitor.lastModified());
            } catch (Exception e) {
                // 一応リトライ
                for (int i = 0; i < this.retryCount; i++) {
                    try {
                        System.out.println("retry trasmit : count = " + (i+1));
                        Thread.sleep(this.retryInterval);
                        this.transmitter.transmit(this.bucketName, this.toPath, this.fromPath);
                    } catch (IOException e2) {
                        continue;
                    } catch (InterruptedException e3) {
                        // 割り込みはスレッド異常として即停止
                        break;
                    }
                    // 成功したらおしまい
                    return;
                }
                e.printStackTrace();
                // 例外イベント
                this.publisher.publishEvent(new LogTransmitErrorEvent("Failed to put to S3! >> " + this.bucketName + " , " + this.toPath + " , "+ this.fromPath));
            }
        } else {
            System.err.println("ログ転送スレッドが停まっている");
        }
    }
    /**
     * AP停止時、または Dyno停止時（SIGTERM）で呼ばれるフック処理 現在日時のディレクトリ名でPutする
     */
    @PreDestroy
    public void shutdownHook() {
        System.out.println("ShutdownHook!");
        try {
            this.transmitter.transmit(this.bucketName, this.toPath + "_" + this.dtf.format(LocalDateTime.now(Clock.system(ZoneId.of("UTC+9")))),
                    this.fromPath);
        } catch (Exception e) {
            e.printStackTrace();
            // ここでの例外は救いようがない
        }
    }

    /**
     * 強制停止メソッド。
     */
    public void forceStop() {
        this.isForceStopped = true;
    }

    /**
     * 開始準備メソッド。外部から実行可能にする
     */
    public void settingStart() {
        this.isForceStopped = false;
    }
}
