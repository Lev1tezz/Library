package kz.iitu.Library.repository;

import kz.iitu.Library.domain.entity.OteuIslamUserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OteuIslamUserFileRepository extends JpaRepository<OteuIslamUserFile, Long> {

    List<OteuIslamUserFile> findByUserId(Long userId);

    List<OteuIslamUserFile> findByUserIdAndFileType(Long userId, String fileType);

    Optional<OteuIslamUserFile> findFirstByUserIdAndFileType(Long userId, String fileType);
}