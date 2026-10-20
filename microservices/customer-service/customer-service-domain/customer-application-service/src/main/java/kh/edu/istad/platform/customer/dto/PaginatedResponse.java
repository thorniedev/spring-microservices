package kh.edu.istad.platform.customer.dto;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {
    public static <T> PaginatedResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = size == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) size);
        boolean isLast = (page + 1) >= totalPages;
        return new PaginatedResponse<>(content, page, size, totalElements, totalPages, isLast);
    }
}
