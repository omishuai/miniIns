package com.app.miniIns.services;

import com.app.miniIns.entities.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer> {

    List<Message> findByReceiverId(int ReceiverId);
}
