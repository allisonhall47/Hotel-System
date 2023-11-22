package ca.mcgill.ecse321.hotelsystem.exception;

import org.springframework.http.HttpStatus;

public class HRSException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private HttpStatus status;


    public HRSException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return status;
    }
}
