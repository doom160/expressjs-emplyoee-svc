package com.renfa.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;;
import org.springframework.data.domain.Sort.Order;
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
      throw new RuntimeException("Fail query for result: " + e.getMessage());
    }
  }

  private int getIndexOf(String[] strings, String item) {
    for (int i = 0; i < strings.length; i++) {
        if (item.equals(strings[i])) return i;
    }
    return -1;
  }
}
