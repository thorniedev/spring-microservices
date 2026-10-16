package kh.edu.istad.platform.business.dto;

import java.util.UUID;

public record CreateBusinessResult(
        UUID businessId,
        String legalName,
        String displayName,
        boolean active,
        String message
) {
}