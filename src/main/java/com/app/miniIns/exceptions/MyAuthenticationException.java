package com.app.miniIns.exceptions;

import org.springframework.security.core.AuthenticationException;

 public class MyAuthenticationException extends AuthenticationException {

     private String msg;
     public MyAuthenticationException(String msg) {
         super(msg);
         this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }

}
