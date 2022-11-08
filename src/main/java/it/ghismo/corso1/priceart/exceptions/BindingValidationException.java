package it.ghismo.corso1.priceart.exceptions;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import it.ghismo.corso1.priceart.errors.ResultEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BindingValidationException extends BaseResultException {
	private static final long serialVersionUID = -2404717895107950786L;
	
	public BindingValidationException(FieldError field, ResourceBundleMessageSource rb) {
		super(ResultEnum.BindingValidationError, field.getObjectName(), field.getField(), field.getRejectedValue()); 
		/*
		log.error("----------- ghismo - code" + field.getCode());
		log.error("----------- ghismo - DefaultMessage" + field.getDefaultMessage());
		log.error("----------- ghismo - Field" + field.getField());
		log.error("----------- ghismo - ObjectName" + field.getObjectName());
		log.error("----------- ghismo - Args" + field.getArguments());
		log.error("----------- ghismo - class" + field.getClass());
		log.error("----------- ghismo - codes" + field.getCodes());
		log.error("----------- ghismo - rejected" + field.getRejectedValue());
		*/
		if(rb != null) {
			String errTranslated = rb.getMessage(field, LocaleContextHolder.getLocale());
			log.warn(errTranslated);
		}
	}
	public BindingValidationException(FieldError field) {
		this(field, null);
	}

	public BindingValidationException(String infoName, Object infoValue) {
		super(ResultEnum.InfoInvalidValueError, infoName, infoValue); 
	}

	@Override public HttpStatus getHttpStatus() { return HttpStatus.BAD_REQUEST; }

}
