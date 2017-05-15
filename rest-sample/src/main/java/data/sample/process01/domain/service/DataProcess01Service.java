package data.sample.process01.domain.service;

import data.sample.process01.domain.model.DataProcess01Bean;

public interface DataProcess01Service {

    DataProcess01Bean getSampleDataByUser(String userId);

    DataProcess01Bean createUserSampleData(DataProcess01Bean bean);

    DataProcess01Bean updateUserSampleData(DataProcess01Bean bean);
}
