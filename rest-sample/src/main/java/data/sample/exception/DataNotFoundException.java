package data.sample.exception;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String string) {
        super(string);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
