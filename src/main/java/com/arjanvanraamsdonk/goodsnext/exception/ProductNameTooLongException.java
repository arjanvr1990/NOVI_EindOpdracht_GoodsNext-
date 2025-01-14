package com.arjanvanraamsdonk.goodsnext.exception;

public class ProductNameTooLongException extends RuntimeException{

    public ProductNameTooLongException (String message){
        super(message);
    }

    public ProductNameTooLongException(){
        super();
    }
}
