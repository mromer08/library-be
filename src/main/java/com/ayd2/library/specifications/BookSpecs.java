package com.ayd2.library.specifications;

import com.ayd2.library.models.book.Book;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BookSpecs {

    public static Specification<Book> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
                title == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> codeEquals(String code) {
        return (root, query, criteriaBuilder) ->
                code == null ? null : criteriaBuilder.equal(root.get("code"), code);
    }

    public static Specification<Book> isbnEquals(String isbn) {
        return (root, query, criteriaBuilder) ->
                isbn == null ? null : criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> minPrice(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) ->
                minPrice == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Book> maxPrice(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                maxPrice == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Book> minQuantity(Integer minQuantity) {
        return (root, query, criteriaBuilder) ->
                minQuantity == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), minQuantity);
    }

    public static Specification<Book> maxQuantity(Integer maxQuantity) {
        return (root, query, criteriaBuilder) ->
                maxQuantity == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), maxQuantity);
    }

    public static Specification<Book> minAvailableCopies(Integer minAvailableCopies) {
        return (root, query, criteriaBuilder) ->
                minAvailableCopies == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("availableCopies"), minAvailableCopies);
    }

    public static Specification<Book> maxAvailableCopies(Integer maxAvailableCopies) {
        return (root, query, criteriaBuilder) ->
                maxAvailableCopies == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("availableCopies"), maxAvailableCopies);
    }

    public static Specification<Book> publicationDateAfter(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                startDate == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("publicationDate"), startDate);
    }

    public static Specification<Book> publicationDateBefore(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("publicationDate"), endDate);
    }

    public static Specification<Book> hasAuthorIds(List<UUID> authorIds) {
        return (root, query, criteriaBuilder) -> {
            if (authorIds == null || authorIds.isEmpty()) return null;
            return root.join("author").get("id").in(authorIds);
        };
    }
    
    public static Specification<Book> hasPublisherIds(List<UUID> publisherIds) {
        return (root, query, criteriaBuilder) -> {
            if (publisherIds == null || publisherIds.isEmpty()) return null;
            return root.join("publisher").get("id").in(publisherIds);
        };
    }
}

