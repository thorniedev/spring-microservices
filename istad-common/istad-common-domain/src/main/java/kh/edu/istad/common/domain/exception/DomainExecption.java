package kh.edu.istad.common.domain.exception;

public class DomainExecption extends RuntimeException {

    // generate constructor 2 this:

    public DomainExecption(String message) {
        super(message);
    }

    public DomainExecption(String message, Throwable cause) {
        super(message, cause);
    }

}
