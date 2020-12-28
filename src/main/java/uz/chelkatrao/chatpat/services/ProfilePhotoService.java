package uz.chelkatrao.chatpat.services;

import org.springframework.stereotype.Service;
import uz.chelkatrao.chatpat.domains.ProfilePhoto;
import uz.chelkatrao.chatpat.repositories.ProfilePhotoRepository;

@Service
public class ProfilePhotoService {

    private final ProfilePhotoRepository profilePhotoRepository;

    public ProfilePhotoService(ProfilePhotoRepository profilePhotoRepository) {
        this.profilePhotoRepository = profilePhotoRepository;
    }

    public ProfilePhoto findProfilePhotoByHashId(String fileName) {
       return profilePhotoRepository.findByHashId(fileName);
    }

}
