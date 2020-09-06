package com.nuga.curation.exception;

public class UnDefinedUserException extends Exception{
    public UnDefinedUserException(){}
    public UnDefinedUserException(String msg){
        super(msg);
    }
}
