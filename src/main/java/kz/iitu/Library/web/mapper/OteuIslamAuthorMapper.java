package kz.iitu.Library.web.mapper;

import kz.iitu.Library.domain.entity.OteuIslamAuthor;
import kz.iitu.Library.web.dto.request.OteuIslamAuthorRequest;
import kz.iitu.Library.web.dto.response.OteuIslamAuthorResponse;
import org.springframework.stereotype.Component;

@Component
public class OteuIslamAuthorMapper {

    public OteuIslamAuthorResponse toResponse(OteuIslamAuthor author) {
        OteuIslamAuthorResponse response = new OteuIslamAuthorResponse();
        response.setId(author.getId());
        response.setFirstName(author.getFirstName());
        response.setLastName(author.getLastName());
        response.setBio(author.getBio());
        return response;
    }

    public OteuIslamAuthor toEntity(OteuIslamAuthorRequest request) {
        return OteuIslamAuthor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .bio(request.getBio())
                .build();
    }
}