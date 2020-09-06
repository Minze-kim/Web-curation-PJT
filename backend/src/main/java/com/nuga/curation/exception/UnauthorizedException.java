package com.nuga.curation.exception;

public class UnauthorizedException extends RuntimeException{
    private static final long serialVersionUID = -2238030302650813813L;
    public UnauthorizedException(String msg){super(msg);}
    public UnauthorizedException(){
        super("계정 권한이 유효하지 않습니다.");
    }
}
