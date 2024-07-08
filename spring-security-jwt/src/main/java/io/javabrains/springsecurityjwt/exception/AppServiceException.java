package io.javabrains.springsecurityjwt.exception;

public class AppServiceException extends RuntimeException {
	private static final long serialVersionUID = 6612605187654038579L;

	public AppServiceException(String errorMessage) {
		super(errorMessage);
	}
}
