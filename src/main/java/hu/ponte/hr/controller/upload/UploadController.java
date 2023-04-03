package hu.ponte.hr.controller.upload;

import hu.ponte.hr.controller.ImagesController;

import hu.ponte.hr.services.ImageStore;
import hu.ponte.hr.services.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
@Component
@RequestMapping("api/file")
public class UploadController
{
    private final String privateKeyPath = "src/main/resources/config/keys/key.private";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SignService signService;

    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> handleFormUpload(@RequestParam("file") MultipartFile file) {
        ImagesController imagesController = applicationContext.getBean(ImagesController.class);
        ImageStore imageStore = imagesController.getImageStore();

        byte[] base64PK = signService.ReadPrivateKeyFromFile(privateKeyPath);

        String signature;
        try {
            signature = signService.SignByteArray(file.getBytes(), base64PK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sign error");
        }

        Object imageReturn = imageStore.saveImageWithSignature(file, signature);
        if (imageReturn == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}
