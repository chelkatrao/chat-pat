package uz.chelkatrao.chatpat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.chelkatrao.chatpat.domains.User;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    @Query(value = "select u.id, " +
            "       u.login, " +
            "       (select message " +
            "        from messages m " +
            "        where (m.from_id = :currentUserId and m.to_id = u.id) " +
            "           or (m.from_id = u.id and m.to_id = :currentUserId) " +
            "        order by message_time desc " +
            "        limit 1) as message, " +
            "       (select count(*) " +
            "        from messages m " +
            "        where m.from_id = u.id " +
            "          and m.to_id = :currentUserId " +
            "          and m.seen = false) " +
            "                    not_seen_message, " +
            "          false as is_typing, " +
            "        concat(:downloadUrl , p.hash_id) as profile_image_download_url " +
            "from chat_user u join profile_photo p on u.profile_photo_id = p.id " +
            "where u.id != :currentUserId", nativeQuery = true)
    List<Map<String, String>> getListOfUsersByCurrentUser(@Param("currentUserId") Long currentUserId, String downloadUrl);

}
