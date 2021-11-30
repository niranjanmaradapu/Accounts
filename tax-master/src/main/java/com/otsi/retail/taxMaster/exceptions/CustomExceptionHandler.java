package com.otsi.retail.taxMaster.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.otsi.retail.taxMaster.errors.ErrorResponse;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
	
	private Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);


	@ExceptionHandler(value = RecordNotFoundException.class)
	public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException recordNotException) {
		ErrorResponse<?> error = new ErrorResponse<>(404, "No Records Found");
		log.error("error response is:"+error);
		return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = InvalidDataException.class)
	public ResponseEntity<Object> handleInvalidDataException(InvalidDataException invalidDataException) {
		ErrorResponse<?> error = new ErrorResponse<>(403, "something is missing, please give valid data");
		log.error("error response is:"+error);
		return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
	}

}
