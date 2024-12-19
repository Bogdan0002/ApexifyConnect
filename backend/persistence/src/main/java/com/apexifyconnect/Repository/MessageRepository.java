package com.apexifyconnect.Repository;

import com.apexifyconnect.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderEmailAndRecipientEmailOrSenderEmailAndRecipientEmailOrderByTimestampDesc(
            String senderEmail1, String recipientEmail1,
            String senderEmail2, String recipientEmail2
    );

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.email = :user1Email AND m.recipient.email = :user2Email) OR " +
            "(m.sender.email = :user2Email AND m.recipient.email = :user1Email) " +
            "ORDER BY m.timestamp")
    List<Message> findConversation(@Param("user1Email") String user1Email,
                                   @Param("user2Email") String user2Email);
}
