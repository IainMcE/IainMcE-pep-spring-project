package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public List<Message> getMessagesFromAccountId(int postedBy){
        return messageRepository.findAllByPostedBy(postedBy);
    }

    public Message addMessage(Message message){
        return messageRepository.save(message);
    }

    public Message getMessageById(int messageId){
        return messageRepository.findByMessageId(messageId);
    }

    public int deleteMessageById(int messageId){
        return messageRepository.deleteByMessageId(messageId);
    }

    public int patchMessageById(int messageId, String messageText){
        return messageRepository.setMessageTextByMessageId(messageId, messageText);
    }
}
