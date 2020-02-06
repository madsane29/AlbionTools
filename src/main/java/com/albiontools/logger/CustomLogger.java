package com.albiontools.logger;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.ObjectError;

public class CustomLogger {

	private Logger logger;// = LoggerFactory.getLogger(getClass());
	
	public CustomLogger(Class clss) {
		logger = LoggerFactory.getLogger(clss);
	}
	
	public void loggerInfoIsCalled(String path, HttpServletRequest request) {
		logger.info(path + " is called by: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() + "(" + request.getRemoteAddr() + ")");
	}
	
	public void loggerInfoWithMessage(String path, HttpServletRequest request, String message) {
		logger.info(path + " is called by: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() + "(" + request.getRemoteAddr() + ") --> Message: " + message);
	}
	
	
	public void loggerWarn(String path, HttpServletRequest request, Exception e) {
		logger.info(path + " --> User: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() + "(" + request.getRemoteAddr() + ")" + " --> Exception: " + e.getMessage());
		
	}
	
	
	public void loggerError(String path, List<ObjectError> errors, HttpServletRequest request) {
		logger.error(path + " is called --> errors: ");
		for (ObjectError error : errors) {
			logger.error(error.toString());
		}
		logger.error("User: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() + "(" + request.getRemoteAddr() + ")");
	}	


}
