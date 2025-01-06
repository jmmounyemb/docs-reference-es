package com.geo.docsReferenceEs.exception;

import com.geo.docsReferenceEs.model.ErrorMessage;
import com.geo.docsReferenceEs.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(IngestionDocumentException.class)
    public ResponseEntity<ErrorResponse> handleIngestionDocumentException(IngestionDocumentException exception,
                                                                          WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(IngestionDocumentException.class.getSimpleName(),
                exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(false, errorMessage);
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
