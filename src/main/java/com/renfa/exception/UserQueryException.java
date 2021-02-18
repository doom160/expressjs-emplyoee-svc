package com.renfa.exception;

public class UserQueryException extends Exception{  
     public UserQueryException(String s){  
          super("UserQueryException: " + s);  
     }  
}  