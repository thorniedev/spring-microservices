package kh.edu.istad.platform.business.exception;

import kh.edu.istad.common.domain.exception.DomainExecption;

public class BusinessDomainException extends DomainExecption {
    public BusinessDomainException(String message) {
        super(message);
    }

    public BusinessDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}