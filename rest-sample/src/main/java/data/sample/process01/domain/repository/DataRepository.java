package data.sample.process01.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import data.sample.process01.domain.model.DataProcess01Bean;

public interface DataRepository extends
                                    JpaRepository<DataProcess01Bean, String> {

    // XXX JpaRepositoryにお任せ
}
