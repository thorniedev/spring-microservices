package kh.edu.istad.platform.business.restapi.dto;

import java.util.UUID;

public record CreateBusinessResponse(
        UUID businessId,
        String legalName,
        String displayName,
        boolean active,
        String message
) {
}