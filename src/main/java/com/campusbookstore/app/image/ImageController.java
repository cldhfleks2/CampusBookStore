package com.campusbookstore.app.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
}
