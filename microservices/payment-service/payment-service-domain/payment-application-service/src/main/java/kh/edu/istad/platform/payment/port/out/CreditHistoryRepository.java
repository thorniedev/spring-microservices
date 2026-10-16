package kh.edu.istad.platform.payment.port.out;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.entity.CreditHistory;
import java.util.List;

public interface CreditHistoryRepository {
    CreditHistory save(CreditHistory creditHistory);
    List<CreditHistory> findByCustomerId(CustomerId customerId);
}