package test.aws.s3;

import org.springframework.context.ApplicationEvent;

public class LogTransmitErrorEvent extends ApplicationEvent {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 601083856739862195L;

    public LogTransmitErrorEvent(String source) {
        super(source);
    }
}
