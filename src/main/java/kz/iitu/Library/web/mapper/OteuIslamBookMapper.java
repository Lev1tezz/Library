package kz.iitu.Library.web.mapper;

import kz.iitu.Library.domain.entity.OteuIslamBook;
import kz.iitu.Library.web.dto.response.OteuIslamBookResponse;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class OteuIslamBookMapper {

    public OteuIslamBookResponse toResponse(OteuIslamBook book) {
        OteuIslamBookResponse response = new OteuIslamBookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setIsbn(book.getIsbn());
        response.setDescription(book.getDescription());
        response.setPublishedYear(book.getPublishedYear());
        response.setTotalCopies(book.getTotalCopies());
        response.setAvailableCopies(book.getAvailableCopies());

        if (book.getCategory() != null) {
            response.setCategoryName(book.getCategory().getName());
        }

        if (book.getAuthors() != null) {
            response.setAuthorNames(
                    book.getAuthors().stream()
                            .map(a -> a.getFirstName() + " " + a.getLastName())
                            .collect(Collectors.toList())
            );
        } else {
            response.setAuthorNames(Collections.emptyList());
        }

        return response;
    }
}