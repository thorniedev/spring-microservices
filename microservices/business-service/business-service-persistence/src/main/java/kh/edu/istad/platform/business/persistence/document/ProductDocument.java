package kh.edu.istad.platform.business.persistence.document;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {
    private UUID id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private boolean available;
}