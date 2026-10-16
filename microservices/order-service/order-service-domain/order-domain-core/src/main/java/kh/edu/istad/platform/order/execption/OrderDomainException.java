package kh.edu.istad.platform.order.execption;

import kh.edu.istad.common.domain.exception.DomainExecption;

public class OrderDomainException extends DomainExecption {
    public OrderDomainException(String message) {
        super(message);
    }

    public OrderDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
