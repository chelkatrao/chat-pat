package uz.chelkatrao.chatpat.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.chelkatrao.chatpat.domains.ProfilePhoto;
import uz.chelkatrao.chatpat.domains.User;
import uz.chelkatrao.chatpat.models.UploadFileResponse;
import uz.chelkatrao.chatpat.security.SecurityUtils;
import uz.chelkatrao.chatpat.services.FileStorageService;
import uz.chelkatrao.chatpat.services.ProfilePhotoService;
import uz.chelkatrao.chatpat.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserRestController {

    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final SecurityUtils securityUtils;
    private final ProfilePhotoService profilePhotoService;

    @Autowired
    public UserRestController(UserService userService,
                              FileStorageService fileStorageService,
                              SecurityUtils securityUtils,
                              ProfilePhotoService profilePhotoService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.securityUtils = securityUtils;
        this.profilePhotoService = profilePhotoService;
    }

    @GetMapping("/user-list")
    public List<Map<String, String>> getListOfUsersByCurrentUser() {
        return this.userService.getListOfUsersByCurrentUser();
    }

    @PostMapping("/upload-profile-photo")
    public UploadFileResponse uploadProfilePhoto(@RequestParam("file") MultipartFile multipartFile) {
        ProfilePhoto profilePhoto = this.userService.uploadProfilePhoto(multipartFile);
        String downloadUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/user/download/profile-photo/")
                .path(profilePhoto.getHashId())
                .toUriString();
        return new UploadFileResponse(profilePhoto.getName(), downloadUrl, profilePhoto.getContentType(), profilePhoto.getFileSize());
    }

    @GetMapping("/download/profile-photo/{fileName}")
    public ResponseEntity<Resource> downloadProfilePhoto(@PathVariable String fileName, HttpServletRequest request) {
        User user = securityUtils.currentUser();
        ProfilePhoto profilePhoto = user.getProfilePhoto();
        String extension = profilePhoto.getExtension();
        Resource resource = fileStorageService.downloadFile(profilePhoto.getUploadPath(), fileName + '.' + extension);
        return downloadPreparer(request, resource);
    }

    @GetMapping("/download/user-list-photo/{fileName}")
    public ResponseEntity<Resource> downloadUserProfilePhoto(@PathVariable String fileName, HttpServletRequest request) {
        ProfilePhoto profilePhoto = profilePhotoService.findProfilePhotoByHashId(fileName);
        String extension = profilePhoto.getExtension();
        Resource resource = fileStorageService.downloadFile(profilePhoto.getUploadPath(), fileName + '.' + extension);
        return downloadPreparer(request, resource);
    }

    private ResponseEntity<Resource> downloadPreparer(HttpServletRequest request, Resource resource) {
        String mimeType;
        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename())
                .body(resource);
    }

}
