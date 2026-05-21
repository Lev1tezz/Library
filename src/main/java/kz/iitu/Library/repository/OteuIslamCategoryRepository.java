package kz.iitu.Library.repository;

import kz.iitu.Library.domain.entity.OteuIslamCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OteuIslamCategoryRepository extends JpaRepository<OteuIslamCategory, Long> {

    boolean existsByName(String name);
}