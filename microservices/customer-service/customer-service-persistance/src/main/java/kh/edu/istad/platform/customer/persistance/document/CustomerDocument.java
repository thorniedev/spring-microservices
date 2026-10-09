package kh.edu.istad.platform.customer.persistance.document;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.valueobject.CustomerStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "customers")
public class CustomerDocument {
    @Id
    private CustomerId id;

    private String username;

    private String familyName;

    private String givenName;

    private String email;

    private String phoneNumber;

    private CustomerStatus status;
}
