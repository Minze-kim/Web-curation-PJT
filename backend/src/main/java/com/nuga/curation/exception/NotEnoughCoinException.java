package com.nuga.curation.exception;

public class NotEnoughCoinException extends Exception{
    public NotEnoughCoinException(){}
    public NotEnoughCoinException(String msg){
        super(msg);
    }
}
