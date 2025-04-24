package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
    public Message findByMessageId(int messageId);
    public List<Message> findAllByPostedBy(int postedBy);
    public int deleteByMessageId(int messageId);

    @Modifying
    @Query("update Message m set m.messageText=?2 where m.messageId=?1")
    public int setMessageTextByMessageId(int messageId, String messageText);
}
