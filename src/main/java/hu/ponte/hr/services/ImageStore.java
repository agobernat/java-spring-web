package hu.ponte.hr.services;

import com.sun.source.tree.Tree;
import hu.ponte.hr.controller.ImageMeta;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ImageStore {

    TreeMap<Long, ImageMeta> imageMap = new TreeMap<>();
    TreeMap<Long, byte[]> imageData = new TreeMap<>();


    public List<ImageMeta> getAllImageMeta() {
        return new ArrayList<>(imageMap.values());
    }

    public ImageMeta getImageMetaByID(Long id) {
        return imageMap.get(id);
    }
    public byte[] getImageDataById(Long id){
        return imageData.get(id);
    }

    public ImageMeta saveImageWithSignature(MultipartFile file, String signature) {

        ImageMeta imageMeta = ImageMeta.builder()
                .name(file.getOriginalFilename())
                .size(file.getSize())
                .id(imageMap.isEmpty() ? "0" : Long.toString(imageMap.lastKey() + 1))
                .mimeType(file.getContentType())
                .digitalSign(signature)
                .build();
        try {
            imageData.put(Long.parseLong(imageMeta.getId()), file.getBytes());
        } catch (IOException e) {
            return null;
        }
        imageMap.put(Long.parseLong(imageMeta.getId()), imageMeta);
        return imageMeta;
    }

}
