package kh.edu.istad.platform.payment.exception;

import kh.edu.istad.common.domain.exception.DomainExecption;

public class PaymentDomainException extends DomainExecption {
    public PaymentDomainException(String message) {
        super(message);
    }

    public PaymentDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}