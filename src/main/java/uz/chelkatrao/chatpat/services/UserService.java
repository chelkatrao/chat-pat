package uz.chelkatrao.chatpat.services;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.chelkatrao.chatpat.Utils;
import uz.chelkatrao.chatpat.domains.ProfilePhoto;
import uz.chelkatrao.chatpat.domains.User;
import uz.chelkatrao.chatpat.repositories.ProfilePhotoRepository;
import uz.chelkatrao.chatpat.repositories.UserRepository;
import uz.chelkatrao.chatpat.security.SecurityUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final ProfilePhotoRepository profilePhotoRepository;
    private final SecurityUtils securityUtils;
    private final Hashids hashIds;

    @Autowired
    public UserService(UserRepository userRepository,
                       FileStorageService fileStorageService,
                       ProfilePhotoRepository profilePhotoRepository,
                       SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.profilePhotoRepository = profilePhotoRepository;
        this.securityUtils = securityUtils;

        hashIds = new Hashids(getClass().getName(), 20);
    }

    public List<Map<String, String>> getListOfUsersByCurrentUser() {
        String downloadUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/v1/user/download/user-list-photo/").toUriString();
        return userRepository.getListOfUsersByCurrentUser(securityUtils.currentUser().getId(), downloadUrl);
    }

    public ProfilePhoto uploadProfilePhoto(MultipartFile multipartFile) {
        ProfilePhoto profilePhoto = new ProfilePhoto();
        profilePhoto.setContentType(multipartFile.getContentType());
        profilePhoto.setExtension(Objects.requireNonNull(multipartFile.getContentType()).split("/")[1]);
        profilePhoto.setFileSize(multipartFile.getSize());
        profilePhoto.setName(multipartFile.getOriginalFilename());

        profilePhoto = profilePhotoRepository.save(profilePhoto);
        profilePhoto.setHashId(hashIds.encode(profilePhoto.getId()));

        fileStorageService.storeUserProfilePhoto(multipartFile, profilePhoto);

        User user = securityUtils.currentUser();
        user.setProfilePhoto(profilePhoto);
        userRepository.save(user);

        // Crop profile photo
        String profileImageURL = profilePhoto.getUploadPath() + '\\'
                + profilePhoto.getHashId() + "." + profilePhoto.getExtension();

        Utils.cropProfilePhoto(profileImageURL);

        return profilePhoto;
    }

}

