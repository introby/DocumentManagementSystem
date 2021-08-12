package by.intro.dms.service;

import by.intro.dms.model.PaginationInfo;
import org.springframework.data.domain.Page;

public class PaginationUtils {

    public static PaginationInfo buildPaginationInfo(Page<?> page) {
        return PaginationInfo.builder()
                .pageSize(page.getSize())
                .pageNumber(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getNumberOfElements())
                .build();
    }

    public static PaginationInfo buildPaginationInfo(int totalElements, int totalPages, int pageNumber, int pageSize) {
        return PaginationInfo.builder()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }
}
