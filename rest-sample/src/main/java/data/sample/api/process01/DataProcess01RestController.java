package data.sample.api.process01;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import data.sample.api.process01.helper.DataProcessZipUtils;
import data.sample.process01.domain.model.DataProcess01Bean;
import data.sample.process01.domain.service.DataProcess01Service;

@RestController
@RequestMapping("data/process01")
@Scope(value = "prototype")
public class DataProcess01RestController {

    @Inject
    DataProcess01Service service;

    /**
     * select one
     * 
     * @param userId
     * @return
     */
    // ※データ取得リソース
    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DataProcess01Resource getSampleData(@PathVariable("userId") String userId) {

        DataProcess01Bean bean = service.getSampleDataByUser(userId);
        DataProcess01Resource resource = new DataProcess01Resource();
        resource.setUserId(bean.getUserId());

        return resource;
    }

    @RequestMapping(method = RequestMethod.POST)
    public DataProcess01Resource uploadSampleData(
            // @RequestPartでJsonのリソースクラスを取得
            @RequestPart("resource") @Validated DataProcess01Resource resource,
            @RequestParam("file") MultipartFile file) {
        DataProcessZipUtils.unzipMultipartFile(file, resource);
        DataProcess01Bean bean = new DataProcess01Bean();
        bean.setUserId(resource.getUserId());
        try {
            bean.setSampleData(new SerialBlob(resource.getSampleData().getBytes("utf-8")));
        } catch (UnsupportedEncodingException | SQLException e) {
            throw new RuntimeException(e);
        }
        bean = service.createUserSampleData(bean);
        DataProcess01Resource res = new DataProcess01Resource();
        res.setUserId(bean.getUserId());
        return res;
    }

    /**
     * UPDATE
     * 
     * @param resource
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public DataProcess01Resource updateSampleData(@RequestBody @Validated DataProcess01Resource resource) {

        DataProcess01Bean bean = new DataProcess01Bean();
        bean.setUserId(resource.getUserId());
        bean = service.updateUserSampleData(bean);

        resource.setUserId(bean.getUserId());
        return resource;

    }

}
