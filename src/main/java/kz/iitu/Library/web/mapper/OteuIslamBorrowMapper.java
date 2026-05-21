package kz.iitu.Library.web.mapper;

import kz.iitu.Library.domain.entity.OteuIslamBorrowRecord;
import kz.iitu.Library.web.dto.response.OteuIslamBorrowResponse;
import org.springframework.stereotype.Component;

@Component
public class OteuIslamBorrowMapper {

    public OteuIslamBorrowResponse toResponse(OteuIslamBorrowRecord record) {
        OteuIslamBorrowResponse response = new OteuIslamBorrowResponse();
        response.setId(record.getId());
        response.setBorrowDate(record.getBorrowDate());
        response.setDueDate(record.getDueDate());
        response.setStatus(record.getStatus().name());

        if (record.getBook() != null) {
            response.setBookTitle(record.getBook().getTitle());
        }

        if (record.getUser() != null) {
            response.setUserEmail(record.getUser().getEmail());
        }

        return response;
    }
}