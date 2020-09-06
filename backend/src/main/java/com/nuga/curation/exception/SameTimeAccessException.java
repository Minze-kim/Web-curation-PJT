package com.nuga.curation.exception;

public class SameTimeAccessException extends Exception{
    public SameTimeAccessException(){}
    public SameTimeAccessException(String msg){
        super(msg);
    }
}
