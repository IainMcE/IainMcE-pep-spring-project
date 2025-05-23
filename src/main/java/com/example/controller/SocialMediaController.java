package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.entity.Account;
import com.example.entity.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService;
    @Autowired
    MessageService messageService;

    /** Create a new Account on the endpoint POST localhost:8080/register. The body will contain a representation of a JSON Account, but will not contain an accountId.
    - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its accountId. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
    - If the registration is not successful due to a duplicate username, the response status should be 409. (Conflict)
    - If the registration is not successful for some other reason, the response status should be 400. (Client error) */
    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody Account newAcc){
        if(newAcc.getUsername().length()<4 || newAcc.getPassword().length()==0){
            return ResponseEntity.status(400).body(null);
        }

        if(accountService.hasAccountWithUsername(newAcc.getUsername())){
            return ResponseEntity.status(409).body(null);

        }

        return ResponseEntity.status(200).body(accountService.addAccount(newAcc));
    }


    /**Verify my login on the endpoint POST localhost:8080/login. The request body will contain a JSON representation of an Account.
    - The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. If successful, the response body should contain a JSON of the account in the response body, including its accountId. The response status should be 200 OK, which is the default.
    - If the login is not successful, the response status should be 401. (Unauthorized) */
    @PostMapping("/login")
    public ResponseEntity<Account> verifyLogin(@RequestBody Account loggingIn){
        Account result = accountService.validateAccountPassword(loggingIn);
        if(result != null){
            return ResponseEntity.status(200).body(result);
        }else{
            return ResponseEntity.status(401).body(null);
        }
    }


    /** Submit a new post on the endpoint POST localhost:8080/messages. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a messageId.
    - The creation of the message will be successful if and only if the messageText is not blank, is not over 255 characters, and postedBy refers to a real, existing user. If successful, the response body should contain a JSON of the message, including its messageId. The response status should be 200, which is the default. The new message should be persisted to the database.
    - If the creation of the message is not successful, the response status should be 400. (Client error) */
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message){
        boolean valid = true;

        if(message.getMessageText().length()==0 || message.getMessageText().length()>255){
            valid = false;
        }
        if(!accountService.hasAccountWithId(message.getPostedBy())){
            valid = false;
        }

        if(valid){
            return ResponseEntity.status(200).body(messageService.addMessage(message));
        }else{
            return ResponseEntity.status(400).body(null);
        }
    }


    /** Submit a GET request on the endpoint GET localhost:8080/messages.
    - The response body should contain a JSON representation of a list containing all messages retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default. */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }


    /**GET request on the endpoint GET localhost:8080/messages/{messageId}.
    - The response body should contain a JSON representation of the message identified by the messageId. It is expected for the response body to simply be empty if there is no such message. The response status should always be 200, which is the default. */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageFromId(@PathVariable("messageId") int messageId){
        return ResponseEntity.status(200).body(messageService.getMessageById(messageId));
    }


    /** Submit a DELETE request on the endpoint DELETE localhost:8080/messages/{messageId}.
    - The deletion of an existing message should remove an existing message from the database. If the message existed, the response body should contain the number of rows updated (1). The response status should be 200, which is the default.
    - If the message did not exist, the response status should be 200, but the response body should be empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response. */
    @Transactional
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable("messageId") int messageId){
        if(messageService.getMessageById(messageId)!=null){
            return ResponseEntity.status(200).body(messageService.deleteMessageById(messageId));
        }
        return ResponseEntity.status(200).body(null);
    }


    /** Submit a PATCH request on the endpoint PATCH localhost:8080/messages/{messageId}. The request body should contain a new messageText values to replace the message identified by messageId. The request body can not be guaranteed to contain any other information.
    - The update of a message should be successful if and only if the message id already exists and the new messageText is not blank and is not over 255 characters. If the update is successful, the response body should contain the number of rows updated (1), and the response status should be 200, which is the default. The message existing on the database should have the updated messageText.
    - If the update of the message is not successful for any reason, the response status should be 400. (Client error) */
    @Transactional
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> patchMessageById(@PathVariable("messageId") int messageId, @RequestBody Message newMessage){
        if(newMessage.getMessageText().length()==0 || newMessage.getMessageText().length()>255){
            return ResponseEntity.status(400).body(null);
        }
        if(messageService.getMessageById(messageId)==null){
            return ResponseEntity.status(400).body(null);
        }

        return ResponseEntity.status(200).body(messageService.patchMessageById(messageId, newMessage.getMessageText()));
    }


    /** Submit a GET request on the endpoint GET localhost:8080/accounts/{accountId}/messages.
    - The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default. */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesFromAccountId(@PathVariable("accountId") int accountId){
        if(accountService.hasAccountWithId(accountId)){
            return ResponseEntity.status(200).body(messageService.getMessagesFromAccountId(accountId));
        }
        return ResponseEntity.status(200).body(null);
    }
}
