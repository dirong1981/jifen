package com.gljr.jifen.service;

import com.gljr.jifen.common.StrUtil;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;


@Service
public class StorageService {

    private final static Logger LOG = LoggerFactory.getLogger(StorageService.class);

    @Value("${bucket.access.ak}")
    private String accessAK;

    @Value("${bucket.access.sk}")
    private String accessSK;

    @Value("${bucket.public}")
    private String publicBucketName;

    @Value("${bucket.private}")
    private String privateBucketName;

    @Value("${static.prefix.url}")
    private String publicPrefixUrl;

    private UploadManager uploadManager = new UploadManager(new Configuration(Zone.autoZone()));

    private Auth auth = null;

    @PostConstruct
    private void init() {
        if (null == auth) {
            auth = Auth.create(accessAK, accessSK);
        }
    }

    public String uploadToPublicBucket(String prefix, MultipartFile file) {
        String _token = auth.uploadToken(publicBucketName);
        try {
            String _key = prefix + "/" + StrUtil.randomKey(8) + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            Response _resp = uploadManager.put(IOUtils.toByteArray(file.getInputStream()), _key, _token);
            if (null != _resp && _resp.isOK()) {
                return _key;
            }

        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return null;
    }

    public String accessUrl(boolean isPrivateResource, String key, String styleName) {
        if (!isPrivateResource) {
            return publicPrefixUrl + "/" + key + (StringUtils.isEmpty(styleName) ? "" : ("!" + styleName));
        }

        return null;
    }

}
