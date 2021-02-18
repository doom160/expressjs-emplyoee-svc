package com.renfa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.renfa.service.CSVService;
import com.renfa.service.UserService;
import com.renfa.helper.CSVHelper;
import com.renfa.message.ResponseMessage;
import com.renfa.model.User;

@CrossOrigin("http://localhost:8081")
@Controller
@RequestMapping("/")
public class UserController {

  @Autowired
  CSVService fileService;

  @Autowired
  UserService userService;

  @PostMapping("/users/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if (CSVHelper.hasCSVFormat(file)) {
      try {
        fileService.save(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "! " + e.getMessage();
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }

    message = "Please upload a csv file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @GetMapping("/users")
  public ResponseEntity<Map<String, List<User>>> getAllUsers(@RequestParam Map<String, String> params) {
    try {
      List<User> users = new ArrayList<User>();
      if(params.size() == 0){
        users = userService.getAllUsers();
      } else {
        users = userService.getFilteredUsers(params);
      }

      Map<String, List<User>> map = new HashMap<>();
      map.put("result",users);
      return new ResponseEntity<>(map, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/users/download")
  public ResponseEntity<Resource> getFile() {
    String filename = "users.csv";
    InputStreamResource file = new InputStreamResource(fileService.load());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/csv"))
        .body(file);
  }
}
