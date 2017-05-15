package data.sample.process01.domain.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import data.sample.exception.DataNotFoundException;
import data.sample.process01.domain.model.DataProcess01Bean;
import data.sample.process01.domain.repository.DataRepository;

@Service
@Scope(value = "prototype")
public class DataProcess01ServiceImpl implements DataProcess01Service, InitializingBean, DisposableBean {

    @Inject
    DataRepository rep;

    @Transactional(readOnly = true)
    @Override
    public DataProcess01Bean getSampleDataByUser(String userId) {
        System.out.println("DataProcess01Bean#getSampleDataByUser() called.");
        DataProcess01Bean res = rep.findOne(userId);
        if (res == null) {
            System.out.println("このユーザのデータは存在しない : " + userId);
            res = new DataProcess01Bean();
            res.setUserId("#empty");
            return res;
        }
        // Blobデータを取得してStringの生データに加工
        Blob blob = null;
        ObjectInputStream stream = null;
        try {
            blob = res.getSampleData();
            res.setSampleDataRaw(new String(blob.getBytes(0, (int) blob.length()), Charset.forName("utf-8")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (blob != null)
                    blob.free();
                if (stream != null)
                    stream.close();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        return res;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public DataProcess01Bean createUserSampleData(DataProcess01Bean bean) {
        System.out.println("DataProcess01Bean#createUserSampleData() called.");
        if (rep.exists(bean.getUserId())) {
            System.out.println("このユーザのデータは既にある : " + bean.getUserId());
        }
        return rep.save(bean);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public DataProcess01Bean updateUserSampleData(DataProcess01Bean bean) {
        System.out.println("DataProcess01Bean#updateUserSampleData() called.");
        if (!rep.exists(bean.getUserId())) {
            System.out.println("このユーザのデータは存在しない : " + bean.getUserId());
            throw new DataNotFoundException("更新しようとしたらデータがなかったよ");
        }

        return rep.save(bean);
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("★デストローイ１★");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("★afterPropertiesSet() called.★");
    }

}
