package uz.chelkatrao.chatpat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.chelkatrao.chatpat.domains.ProfilePhoto;

import java.util.UUID;

@Repository
public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, UUID> {
}
