package uz.chelkatrao.chatpat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.chelkatrao.chatpat.domains.ProfilePhoto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

@Service
public class FileStorageService {

    /**
     * Paths file
     */
    private Path fileStorageMsgFilePath;
    private Path fileStorageUserProfilePhotosPath;

    /**
     * Created file storage directories
     *
     * @param fileStorageLocation files location directory
     */
    @Autowired
    public FileStorageService(@Value("${file.storage.location}") String fileStorageLocation,
                              @Value("${file.storage.location.user-msg-file}") String userMessagingFileLocation,
                              @Value("${file.storage.location.user-profile-photos}") String userProfilePhotosLocation) {
        /*
         Path | 📁 storeDirectory
         Path | 📁 storeDirectory / 📁 userMessagingFile
         Path | 📁 storeDirectory / 📁 userProfilePhotos
         */
        Path fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        this.fileStorageMsgFilePath = Paths.get(fileStorageLocation + '\\' + userMessagingFileLocation).toAbsolutePath().normalize();
        this.fileStorageUserProfilePhotosPath = Paths.get(fileStorageLocation + '\\' + userProfilePhotosLocation).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStoragePath);                  // 📁-> create root folder
            Files.createDirectories(fileStorageMsgFilePath);           // 📁-> create msg file folder
            Files.createDirectories(fileStorageUserProfilePhotosPath); // 📁-> create profile photo folder
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }
    }

    public void storeUserMessagingFile(MultipartFile file, String fileId) {
        fileStorage(file, fileStorageMsgFilePath, fileId);
    }

    public void storeUserProfilePhoto(MultipartFile file, ProfilePhoto profilePhoto) {
        Calendar now = Calendar.getInstance();
        profilePhoto.setUploadPath(String.format("%s\\%d\\%d\\%d",
                fileStorageUserProfilePhotosPath,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)));

        fileStorage(file, fileStorageUserProfilePhotosPath, profilePhoto.getHashId() + "." + profilePhoto.getExtension());
    }

    private void fileStorage(MultipartFile file, Path fileStoragePath, String hashedFileName) {
        Path storagePath = createStoragePath(fileStoragePath);

        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating file directory");
        }

        try {
            Files.copy(file.getInputStream(), Paths.get(storagePath.toString(), hashedFileName));
        } catch (IOException e) {
            throw new RuntimeException("Issue in storing the file", e);
        }
    }

    private Path createStoragePath(Path fileStoragePath) {
        Calendar now = Calendar.getInstance();
        return Paths.get(String.format("%s\\%d\\%d\\%d",
                fileStoragePath,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH))
        ).toAbsolutePath();
    }

    public Resource downloadFile(String filePath, String fileName) {
        Path path = Paths.get(filePath).toAbsolutePath().resolve(fileName).normalize();
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading file", e);
        }

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("The file doesn't exist or readable");
        }
    }
}
