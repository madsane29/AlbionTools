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
	
	public void loggerInfoIsCalled(HttpServletRequest request) {
		logger.info("\"" + request.getRequestURI() + "\" is called by: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() + "(" + request.getRemoteAddr() + ")");
	}
	
	public void loggerInfoWithMessage(HttpServletRequest request, String message) {
		logger.info("\"" + request.getRequestURI() + "\" is called by: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() + "(" + request.getRemoteAddr() + ") --> Message: " + message);
	}
	
	public void loggerWarn(HttpServletRequest request, Exception e) {
		logger.info("\"" + request.getRequestURI() + "\" --> User: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() + "(" + request.getRemoteAddr() + ")" + " --> Exception: " + e.getMessage());
	}
	
	public void loggerError(List<ObjectError> errors, HttpServletRequest request) {
		logger.error("\"" + request.getRequestURI() + "\" is called --> errors: ");
		for (ObjectError error : errors) {
			logger.error(error.toString());
		}
		logger.error("User: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() + "(" + request.getRemoteAddr() + ")");
	}	


}
