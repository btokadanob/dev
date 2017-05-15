package data.sample.api.process01;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DataProcess01Resource implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 20)
    String userId;

    String sampleData;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSampleData() {
        return sampleData;
    }

    public void setSampleData(String sampleData) {
        this.sampleData = sampleData;
    }
}
