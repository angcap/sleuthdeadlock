package online.pelago.p4p.shipitinerary.exceptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Value;


/**
 * Customize the default exception handling mechanism provided by Spring Web.
 * Change the response body as well as {@link CustomErrorAttributes}.
 * It intercept {@link MethodArgumentNotValidException} and {@link TypeMismatchException}
 *
 */
@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String MESSAGE = "message";
	private static final Logger log = LoggerFactory.getLogger(CustomGlobalExceptionHandler.class);
	
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ElementNotFoundException.class)
    public final ErrorResponse exceptionNotFoundHandler(HttpServletRequest request, Exception ex) {
        ErrorResponse errore = new ErrorResponse();

        errore.setCode(CustomErrorAttributes.http2CustomCodeMap.get(HttpStatus.NOT_FOUND.value()));
        errore.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errore.setMessage(ex.getMessage());
        errore.setPath(request.getServletPath());
        errore.setTimestamp(CustomErrorAttributes.isoDatetimeFormatter.format(new Date()));

       log.error(ex.getMessage(), ex);

        return errore;
    }

    @Override
    protected ResponseEntity<Object>
    handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                 HttpHeaders headers,
                                 HttpStatus status, WebRequest request) {

        Map<String, Object> body = getValidationErrorAttributesMap((ServletWebRequest) request);

        List<CustomFieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new CustomFieldError(fe.getField(),fe.getDefaultMessage()))
                .collect(Collectors.toList());

        body.put("fields", errors);

        return new ResponseEntity<>(body, headers, status);

    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, Object> body = getValidationErrorAttributesMap((ServletWebRequest) request);

        body.put(MESSAGE, ex.getPropertyName() +" must have a value conform to "+ex.getRequiredType());

        return new ResponseEntity<>(body, headers, status);

    }
    
    @Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
    	 Map<String, Object> body = getValidationErrorAttributesMap((ServletWebRequest) request);

         body.put(MESSAGE, ex.getFieldErrors().stream().map(err -> "Field: " + err.getField() + " cannot accept value: "+ err.getRejectedValue()));

         return new ResponseEntity<>(body, headers, status);
	}

    private Map<String, Object> getValidationErrorAttributesMap(ServletWebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()));
        body.put("code", "1003");
        body.put(MESSAGE, "Validation Failed");
        body.put("error", "Validation Failed");
        body.put("path", request.getRequest().getServletPath());
        return body;
    }

    @Value
    @AllArgsConstructor
    static class CustomFieldError{
        private String field;
        private String message;
    }

}
