package com.arjanvanraamsdonk.goodsnext.exceptions;

public class ProductNameTooLongException extends RuntimeException{

    public ProductNameTooLongException (String message){
        super(message);
    }

    public ProductNameTooLongException(){
        super();
    }
}
