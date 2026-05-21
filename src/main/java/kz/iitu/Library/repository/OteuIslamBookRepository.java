package kz.iitu.Library.repository;

import kz.iitu.Library.domain.entity.OteuIslamBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OteuIslamBookRepository extends JpaRepository<OteuIslamBook, Long> {

    @Query(value = """
        SELECT * FROM books b
        WHERE (:search IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:categoryId IS NULL OR b.category_id = :categoryId)
        """,
            countQuery = """
        SELECT COUNT(*) FROM books b
        WHERE (:search IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:categoryId IS NULL OR b.category_id = :categoryId)
        """,
            nativeQuery = true)
    Page<OteuIslamBook> findAllWithFilters(
            @Param("search") String search,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    boolean existsByIsbn(String isbn);
}