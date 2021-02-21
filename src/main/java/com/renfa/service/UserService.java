package com.renfa.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.renfa.helper.CSVHelper;
import com.renfa.model.User;
import com.renfa.repository.UserRepository;
import com.renfa.exception.UserQueryException;

@Service
public class UserService {
  @Autowired
  UserRepository repository;

  public List<User> getAllUsers() {
    return repository.findAll();
  }

  public User findById(String id){
    try {
      List<User> result = repository.findById(id);

      if(result.size() == 0){
        throw new UserQueryException("No such user");
      }

      return repository.findById(id).get(0);
    } catch (UserQueryException e){
      throw new RuntimeException("Fail query for result: " + e.getMessage());
    }
  }

  public void deleteById(String id){
    try {
      repository.deleteById(id);
    } catch (Exception e){
      throw new RuntimeException(String.format("Fail to delete %s : %s", id, e.getMessage()));
    }
  }

  public void addUser(User user){
    try {
      if(user.getSalary() < 0){
        throw new UserQueryException("User salary should be 0 or more");
      }
      if(user.getId() ==  null){
        throw new UserQueryException("User id is missing");
      }
      if(user.getName() ==  null){
        throw new UserQueryException("User name is missing");
      }
      if(user.getLogin() ==  null){
        throw new UserQueryException("User login is missing");
      }

      repository.save(user);
      
    } 
    catch (UserQueryException e){
      throw new RuntimeException(String.format("Fail to save user %s : %s", user.getId(), e.getMessage()));
    } catch (Exception e){
      throw new RuntimeException(String.format("Fail to save user %s", user.getId()));
    }
  }

  public void updateUser(User newUser, String id){
    User user = findById(id);
    try {
      if(newUser.getId() != null) {
        user.setId(newUser.getId());
      }
      if(newUser.getName() != null) {
        user.setName(newUser.getName());
      }
      if(newUser.getSalary() >= 0 ) {
        user.setSalary(newUser.getSalary());
      } else {
        throw new UserQueryException("User salary should be 0 or more");
      }
      if(newUser.getLogin() != null) {
        user.setLogin(newUser.getLogin());
      }

      repository.save(user);
    } catch (UserQueryException e){
      throw new RuntimeException(String.format("Fail to update user %s: %s", user.getId(), e.getMessage()));
    } catch (Exception e){
      throw new RuntimeException(String.format("Fail to update user %s", user.getId()));
    }
  }

  public List<User> getFilteredUsers(Map<String, String> params) {
    try {
      float minSalary = params.containsKey("minSalary") ? Float.parseFloat(params.get("minSalary")): 0f;
      float maxSalary = params.containsKey("maxSalary") ? Float.parseFloat(params.get("maxSalary")): (float)Integer.MAX_VALUE;
      int offset = params.containsKey("offset") ? Integer.parseInt(params.get("offset")): 0 ;
      int limit = params.containsKey("limit") ? Integer.parseInt(params.get("limit")): 30 ;
      
      if(minSalary > maxSalary){
        throw new UserQueryException("minSalary is more than maxSalary");
      }

      //ValidateSort 
      String sort = params.containsKey("sort") ? params.get("sort"): "+id";
      if(!(sort.startsWith("+") || sort.startsWith("-"))  || getIndexOf(CSVHelper.HEADERS, sort.substring(1).toLowerCase()) == -1 ){
        throw new UserQueryException("Invalid sort parameter");
      }

      Pageable pagingSortCriteria = PageRequest.of(offset, limit, Sort.by(sort.startsWith("+") ? Direction.ASC: Direction.DESC, sort.substring(1)));
      return repository.findBySalary(minSalary, maxSalary, pagingSortCriteria);
    } catch (UserQueryException e) {
      throw new RuntimeException(e.getMessage());
    } catch (NumberFormatException e) {
      throw new RuntimeException("Invalid sort parameter:" + e.getMessage());
    } catch (Exception e){
      throw new RuntimeException("Fail query for result");
    }
  }

  private int getIndexOf(String[] strings, String item) {
    for (int i = 0; i < strings.length; i++) {
        if (item.equals(strings[i])) return i;
    }
    return -1;
  }
}
