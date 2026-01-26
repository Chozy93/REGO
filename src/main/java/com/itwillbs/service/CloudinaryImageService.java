package com.itwillbs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryImageService {

    private final Cloudinary cloudinary;

    @Getter
    @AllArgsConstructor
    private static class UploadedImage {
        private final String publicId;
        private final String url;
    }

    public List<String> upload(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("업로드할 이미지가 없습니다.");
        }

        List<UploadedImage> uploadedImages = new ArrayList<>();

        try {
            for (MultipartFile image : images) {
                if (image.isEmpty()) {
                    throw new IllegalArgumentException("빈 이미지 파일이 포함되어 있습니다.");
                }

                Map<?, ?> result = cloudinary.uploader().upload(
                    image.getBytes(),
                    Map.of(
                        "folder", "rego/products",
                        "resource_type", "image"
                    )
                );

                uploadedImages.add(
                    new UploadedImage(
                        (String) result.get("public_id"),
                        (String) result.get("secure_url")
                    )
                );
            }

            return uploadedImages.stream()
                .map(UploadedImage::getUrl)
                .toList();

        } catch (Exception e) {
            rollback(uploadedImages);
            throw new RuntimeException("Cloudinary 이미지 업로드 실패", e);
        }
    }

    private void rollback(List<UploadedImage> uploadedImages) {
        for (UploadedImage image : uploadedImages) {
            try {
                cloudinary.uploader().destroy(
                    image.getPublicId(),
                    Map.of("resource_type", "image")
                );
            } catch (Exception ex) {
                log.warn("Cloudinary 이미지 롤백 실패: {}", image.getPublicId(), ex);
            }
        }
    }
}

