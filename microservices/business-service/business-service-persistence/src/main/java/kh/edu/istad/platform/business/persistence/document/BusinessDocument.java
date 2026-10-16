package kh.edu.istad.platform.business.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "businesses")
public class BusinessDocument {
    @Id
    private UUID id;
    private String legalName;
    private String displayName;
    private String email;
    private String phoneNumber;
    private boolean active;
    private List<ProductDocument> products;
}