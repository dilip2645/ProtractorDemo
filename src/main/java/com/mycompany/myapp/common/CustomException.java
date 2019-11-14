package com.mycompany.myapp.common;


public class CustomException extends Exception {

    private static final long serialVersionUID = -7734784262104875066L;

    public CustomException() {
        /* Default Constructor */
    }

    /**
     * @param message
     */
    public CustomException(String message) {
        super(message);
    }

}
