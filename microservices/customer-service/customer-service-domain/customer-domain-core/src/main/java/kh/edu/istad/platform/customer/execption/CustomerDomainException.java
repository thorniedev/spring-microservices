package kh.edu.istad.platform.customer.execption;

import kh.edu.istad.common.domain.exception.DomainExecption;

public class CustomerDomainException extends DomainExecption {

    public CustomerDomainException(String message) {
        super(message);
    }

    public CustomerDomainException(String message, Throwable cause) {
        super(message, cause);
    }

}
