package com.nuga.curation.exception;

import com.nuga.curation.domain.article.dto.BidTryResponse;

public class BidImposibleException extends Exception{
    public BidImposibleException(){}
    public BidImposibleException(String msg){
        super(msg);
    }

}
