package com.faos.exception;

import java.util.Map;

public class InvalidEntityException extends RuntimeException {
    private Map<String, String> errorMap = null;
	
		public InvalidEntityException(Map<String, String> errorMap) {
			super("Invalid entity");
			this.errorMap = errorMap;
    }
	public InvalidEntityException(String message){
		super(message);
	}

    public Map<String, String> getErrorMap() {
        return errorMap;
    }
}
