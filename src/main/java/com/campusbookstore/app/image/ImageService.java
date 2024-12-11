package com.campusbookstore.app.image;

import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageDTO getImageDTO(Image image) {
        if (image == null) return null;
        ImageDTO.ImageDTOBuilder builder = ImageDTO.builder();

        if (image.getId() != null)
            builder.imagePath(image.getImagePath());

        return builder.build();
    }

    //2. DTO -> Entity
    public Image convertToImage(ImageDTO imageDTO) {
        if (imageDTO == null) return null;
        Image image = new Image();

        if (imageDTO.getImagePath() != null)
            image.setImagePath(imageDTO.getImagePath());

        return image;
    }

}
