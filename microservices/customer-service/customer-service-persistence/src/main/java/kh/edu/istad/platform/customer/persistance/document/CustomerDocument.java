package kh.edu.istad.platform.customer.persistance.document;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.valueobject.Email;
import kh.edu.istad.platform.customer.valueobject.PhoneNumber;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Getter
@Setter
@Document(collection = "customers")
@CompoundIndex(name = "name_idx", def = "{'given_name': 1, 'family_name': 1}")
public class CustomerDocument implements Persistable<String> {

    @Id
    // private CustomerId id;
    private String id;

    private String username;

    private String familyName;

    private String givenName;

    private String email;

    private String phoneNumber;

    private CustomerStatus status;

    @Field("total_spent")
    private BigDecimal totalSpent;

    @Field("loyalty_points")
    private Integer loyaltyPoints;

    // RELATIONSHIP use @DBRef
    @DBRef
    @Field("segment_dbref")
    private CustomerSegmentDocument segmentDbRef;

    // RELATIONSHIP: use @DocumentReference (Spring Data MongoDB Modern)
    @Field("segment_doc_ref")
    private CustomerSegmentDocument segmentDocRef;

    // RELATIONSHIP: Manual Reference (Pure Reactive & DDD Best Practice)
    @Field("segment_id")
    private String segmentId;

    @CreatedBy
    private String createdBy;
    @CreatedDate
    private String createdAt;
    @LastModifiedBy
    private String modifiedBy;
    @LastModifiedDate
    private String modifiedAt;

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
