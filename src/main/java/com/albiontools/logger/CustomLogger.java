package com.albiontools.logger;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.ObjectError;


public class CustomLogger {
	
	private Logger logger;
	
	public CustomLogger(Class clss) {
		logger = LoggerFactory.getLogger(clss);
	}
	
	public void loggerInfoWithHttpServletRequestParam(HttpServletRequest request) {
		logger.info(getCallersData(request));
	}
	
	public void loggerInfoWithHttpServletRequestAndMessageParam(HttpServletRequest request, String message) {
		logger.info(getCallersData(request) + " --> Message: " + message);
	}
	
	public void loggerWarnWithHttpServletRequestAndExceptionParam(HttpServletRequest request, Exception e) {
		logger.info(getCallersData(request) + " --> Exception: " + e.getMessage());
	}
	
	public void loggerErrorWithHttpServletRequestAndErrorsParam(HttpServletRequest request, List<ObjectError> errors) {
		logger.error(getCallersData(request) + " --> Errors: ");
		
		for (ObjectError error : errors) {
			logger.error(error.toString());
		}
	}	

	private String getPrincipal() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}
	
	private String getCallersData(HttpServletRequest request) {
		return "\"" + request.getRequestURI() + "\" is called by: " + getPrincipal() + "(" + request.getRemoteAddr() + ")";
	}

}
