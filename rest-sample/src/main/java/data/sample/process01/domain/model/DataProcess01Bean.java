package data.sample.process01.domain.model;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sampledata")
public class DataProcess01Bean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    String userId;

    Blob sampleData;

    String sampleDataRaw;

    public String getSampleDataRaw() {
        return sampleDataRaw;
    }

    public void setSampleDataRaw(String sampleDataRaw) {
        this.sampleDataRaw = sampleDataRaw;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Blob getSampleData() {
        return sampleData;
    }

    public void setSampleData(Blob sampleData) {
        this.sampleData = sampleData;
    }
}
