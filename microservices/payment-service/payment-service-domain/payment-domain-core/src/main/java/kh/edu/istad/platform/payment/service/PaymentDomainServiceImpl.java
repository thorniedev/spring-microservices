package kh.edu.istad.platform.payment.service;

import kh.edu.istad.common.domain.valueobject.Money;
import kh.edu.istad.common.domain.valueobject.PaymentStatus;
import kh.edu.istad.platform.payment.entity.CreditEntry;
import kh.edu.istad.platform.payment.entity.CreditHistory;
import kh.edu.istad.platform.payment.entity.Payment;
import kh.edu.istad.platform.payment.event.PaymentCancelledEvent;
import kh.edu.istad.platform.payment.event.PaymentCompletedEvent;
import kh.edu.istad.platform.payment.event.PaymentFailedEvent;
import kh.edu.istad.platform.payment.valueobject.CreditHistoryId;
import kh.edu.istad.platform.payment.valueobject.TransactionType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class PaymentDomainServiceImpl implements PaymentDomainService {

    private static final String UTC = "UTC";

    @Override
    public PaymentCompletedEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
                                                            List<CreditHistory> creditHistories, List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        payment.initializePayment();

        if (failureMessages.isEmpty()) {
            validateCreditEntry(payment, creditEntry, failureMessages);
            subtractCreditEntry(payment, creditEntry);
            updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);

            if (failureMessages.isEmpty()) {
                payment.updateStatus(PaymentStatus.COMPLETED);
                return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages);
            }
        }

        payment.updateStatus(PaymentStatus.FAILED);
        return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages);
    }

    @Override
    public PaymentCancelledEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
                                                          List<CreditHistory> creditHistories, List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);
        payment.updateStatus(PaymentStatus.CANCELLED);
        return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages);
    }

    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            failureMessages.add("Customer with id: " + payment.getCustomerId().getValue()
                    + " doesn't have enough credit for payment!");
        }
    }

    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment, List<CreditHistory> creditHistories, TransactionType transactionType) {
        creditHistories.add(new CreditHistory(
                new CreditHistoryId(UUID.randomUUID()),
                payment.getCustomerId(),
                payment.getPrice(),
                transactionType
        ));
    }
}