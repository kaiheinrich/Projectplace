package de.kaiheinrich.projectplace.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.kaiheinrich.projectplace.utils.AmazonS3ClientUtils;
import de.kaiheinrich.projectplace.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageUploadAWSService {

    private final AmazonS3ClientUtils s3ClientUtils;
    private final IdUtils idUtils;

    @Autowired
    public ImageUploadAWSService(AmazonS3ClientUtils s3ClientUtils, IdUtils idUtils) {
        this.s3ClientUtils = s3ClientUtils;
        this.idUtils = idUtils;
    }

    public String upload(MultipartFile file) throws IOException {

        AmazonS3 s3Client = s3ClientUtils.getS3Client();
        String randomFileName = idUtils.generateId();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            PutObjectRequest request = new PutObjectRequest(s3ClientUtils.getBucketName(), randomFileName, file.getInputStream(), metadata);
            s3Client.putObject(request);

            return randomFileName;

        } catch (AmazonServiceException e) {
            e.printStackTrace();

        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        return "";
    }
}
