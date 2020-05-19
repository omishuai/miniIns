package com.app.miniIns.services.repositories;

import com.app.miniIns.entities.server.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    List<Message> findByReceiverId(int ReceiverId);
    Message findById(int id);
}
