package hu.ponte.hr.controller;


import hu.ponte.hr.services.ImageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@RestController()
@RequestMapping("api/images")
public class ImagesController {

    @Autowired
    private ImageStore imageStore;

    public ImageStore getImageStore() {
        return imageStore;
    }

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
		return imageStore.getAllImageMeta();
    }


    @ResponseBody
    @RequestMapping(value = "preview/{id}", method = RequestMethod.GET)
    public byte[] getImage(@PathVariable("id") String id, HttpServletResponse response) {
        Long idlong = Long.parseLong(id);
        ImageMeta imageMeta = imageStore.getImageMetaByID(idlong);
        response.setContentType(imageMeta.getMimeType());
        return imageStore.getImageDataById(idlong);
	}

}
