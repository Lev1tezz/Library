package kz.iitu.Library.repository;

import kz.iitu.Library.domain.entity.OteuIslamBorrowRecord;
import kz.iitu.Library.domain.enums.OteuIslamBorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OteuIslamBorrowRepository extends JpaRepository<OteuIslamBorrowRecord, Long> {

    List<OteuIslamBorrowRecord> findByUserId(Long userId);

    boolean existsByUserIdAndBookIdAndStatus(
            Long userId,
            Long bookId,
            OteuIslamBorrowStatus status
    );
}