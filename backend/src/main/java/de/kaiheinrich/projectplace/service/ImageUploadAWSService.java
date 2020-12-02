package de.kaiheinrich.projectplace.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.kaiheinrich.projectplace.utils.AmazonS3ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageUploadAWSService {

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final AmazonS3ClientUtils s3ClientUtils;

    @Autowired
    public ImageUploadAWSService(AmazonS3ClientUtils s3ClientUtils) {
        this.s3ClientUtils = s3ClientUtils;
    }

    public String upload(MultipartFile file) throws IOException, InterruptedException {

        AmazonS3 s3Client = s3ClientUtils.getS3Client();

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            PutObjectRequest request = new PutObjectRequest(bucketName, file.getOriginalFilename(), file.getInputStream(), metadata);
            s3Client.putObject(request);

            return file.getOriginalFilename();

        } catch (AmazonServiceException e) {
            e.printStackTrace();

        } catch (SdkClientException e) {
            e.printStackTrace();
        }

        return "";
    }
}
