package ee.taltech.iti0302.okapi.backend.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class ApplicationErrorHandler {

    @ExceptionHandler(ApplicationRuntimeException.class)
    public ResponseEntity<ErrorMessageResponse> handleException(ApplicationRuntimeException error) {
        return new ResponseEntity<>(new ErrorMessageResponse(error.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleException(Exception error) {
        String errorMessage = String.format("%s:%s", LocalDateTime.now(), error);
        String responseMessage = "Oops, it looks like the server could not fulfill your request! We're looking into it.";

        log.info(errorMessage);
        return new ResponseEntity<>(new ErrorMessageResponse(responseMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
