package kh.edu.istad.platform.business.dto;

import java.util.List;

public record CreateBusinessCommand(
        String legalName,
        String displayName,
        String email,
        String phoneNumber,
        List<ProductDto> products
) {
}