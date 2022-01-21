package com.hdpros.hdprosbackend.providers.cloudinary.implemetation;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.hdpros.hdprosbackend.providers.cloudinary.CloudinaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    /**
     * Upload a MultipartFile to Cloudinary. More info:
     * https://stackoverflow.com/a/39572293
     *
     * @param file to be uploaded
     * @return the publicId assigned to the uploaded file, or null in case of
     * error
     */
    @Override
    public Map<String, String> upload(MultipartFile file) {
        String fileName = file.getName();
        log.trace("Called CloudinaryService.upload for the multipart file => {}", fileName);
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String publicId = uploadResult.get("public_id").toString();
            String url = uploadResult.get("url").toString();
            log.info("Successfully uploaded the file => {}, public ID => {}", fileName, publicId);

            //Create map object
            Map<String, String> returnedValues = new HashMap<>();
            returnedValues.put("publicId", publicId);
            returnedValues.put("url", url);

            return returnedValues;
        } catch (Exception ex) {
            log.error("Failed to upload to Cloudinary the image file => " + fileName);
            log.error(ex.getMessage());
            return null;
        }

    }

    /**
     * Download an image from Cloudinary
     *
     * @param publicId of the image
     * @param width
     * @param isAvatar
     * @param height
     * @return
     */
    @Override
    public ResponseEntity<ByteArrayResource> downloadImg(String publicId, int width, int height, boolean isAvatar) {

        log.info("Requested to download the image file: " + publicId);

        // Generates the URL
        String format = "jpg";
        Transformation transformation = new Transformation().width(width).height(height).crop("fill");
        if (isAvatar) {
            // transformation = transformation.gravity("face").radius("max");
            transformation = transformation.radius("max");
            format = "png";
        }
        String cloudUrl = cloudinary.url().secure(true).format(format)
                .transformation(transformation)
                .publicId(publicId)
                .generate();

        log.debug("Generated URL of the image to be downloaded: " + cloudUrl);

        try {
            // Get a ByteArrayResource from the URL
            URL url = new URL(cloudUrl);
            InputStream inputStream = url.openStream();
            byte[] out = StreamUtils.copyToByteArray(inputStream);
            ByteArrayResource resource = new ByteArrayResource(out);

            // Creates the headers
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "attachment; filename=image.jpg");
            responseHeaders.add("Content-Type", "image/jpeg");

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .contentLength(out.length)
                    // .contentType(MediaType.parseMediaType(mimeType))
                    .body(resource);

        } catch (Exception ex) {
            log.error("FAILED to download the file: " + publicId);
            return null;
        }
    }
}