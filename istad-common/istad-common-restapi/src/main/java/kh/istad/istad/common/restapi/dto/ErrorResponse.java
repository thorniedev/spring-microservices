package kh.istad.istad.common.restapi.dto;
import lombok.Builder;

import java.time.ZonedDateTime;

@Builder
public record ErrorResponse(
        String code,
        String message,
        int status,
        ZonedDateTime timestamp
) {
}
