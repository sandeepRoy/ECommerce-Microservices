package com.msa.order.services;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ByteToMultipartConverter {
    public MultipartFile convertByteToMultipart(byte[] fileByte, String name, String originalFileName, String contentType) {
        // overriden methods for multipartfile conversion
        return new ByteArrayMultipartFile(fileByte, name, originalFileName, contentType);
    }
}
