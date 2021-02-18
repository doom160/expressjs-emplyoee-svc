package com.renfa.exception;

public class FileUploadContentException extends Exception{  
     public FileUploadContentException(String s){  
          super("FileUploadContentException: " + s);  
     }  
}  