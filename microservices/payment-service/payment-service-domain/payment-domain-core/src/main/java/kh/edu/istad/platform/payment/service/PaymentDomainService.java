package kh.edu.istad.platform.payment.service;

import kh.edu.istad.platform.payment.entity.CreditEntry;
import kh.edu.istad.platform.payment.entity.CreditHistory;
import kh.edu.istad.platform.payment.entity.Payment;
import kh.edu.istad.platform.payment.event.PaymentCancelledEvent;
import kh.edu.istad.platform.payment.event.PaymentCompletedEvent;
import kh.edu.istad.platform.payment.event.PaymentFailedEvent;

import java.util.List;

public interface PaymentDomainService {
    PaymentCompletedEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
                                                    List<CreditHistory> creditHistories, List<String> failureMessages);
    PaymentCancelledEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
                                                  List<CreditHistory> creditHistories, List<String> failureMessages);
}