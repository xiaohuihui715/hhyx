package com.hjh.hhyx.product.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 韩
 * @version 1.0
 */
public interface FileUploadService {
    //文件上传
    String fileUpload(MultipartFile file) throws Exception;
}
