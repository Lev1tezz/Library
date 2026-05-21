package kz.iitu.Library.repository;

import kz.iitu.Library.domain.entity.OteuIslamAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OteuIslamAuthorRepository extends JpaRepository<OteuIslamAuthor, Long> {
}