package de.hs.da.hskleinanzeigen.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionInterceptor {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AdvertisementNotFoundException.class)
    public void handleNotFound(){

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AdvertisementBadRequestException.class, CategoryBadRequestException.class,
            UserBadRequestException.class, NotepadBadRequestException.class})
    public void handleBadRequest(){

    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({CategoryConflictException.class, UserConflictException.class})
    public void handleConflict(){

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public void handleUserNotFound(){

    }

}
