package com.renfa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.renfa.model.User;
import com.renfa.repository.UserRepository;

@Service
public class UserService {
  @Autowired
  UserRepository repository;

  public List<User> getAllUsers() {
    return repository.findAll();
  }
}
