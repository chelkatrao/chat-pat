package uz.chelkatrao.chatpat.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.chelkatrao.chatpat.domains.Messages;
import uz.chelkatrao.chatpat.domains.User;
import uz.chelkatrao.chatpat.models.MessageModel;

@Repository
public interface MessageRepository extends PagingAndSortingRepository<Messages, Long> {

    @Query(value = "  select new uz.chelkatrao.chatpat.models.MessageModel( " +
            "       m.id, " +
            "       m.message, " +
            "       m.messageTime, " +
            "       m.seen, " +
            "       m.from.id, " +
            "       m.to.id, " +
            "       m.from =:currentUser " +
            "      )       " +
            "  from Messages m " +
            "       where (m.from = :currentUser and m.to = :toUser) or (m.from = :toUser and m.to = :currentUser) ")
    Page<MessageModel> getMessageByUser(@Param("currentUser") User currentUser, @Param("toUser") User toUser, Pageable pageable);

    @Modifying
    @Query(value = "update messages set seen = true where seen = false and from_id = :fromUserId and to_id =:currentUserId", nativeQuery = true)
    int makeSeenTrueByUserId(@Param("fromUserId") long fromUserId, @Param("currentUserId") long currentUserId);

}
