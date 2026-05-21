package kz.iitu.Library.repository;

import kz.iitu.Library.domain.entity.OteuIslamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OteuIslamUserRepository extends JpaRepository<OteuIslamUser, Long> {

    Optional<OteuIslamUser> findByEmail(String email);

    boolean existsByEmail(String email);
}