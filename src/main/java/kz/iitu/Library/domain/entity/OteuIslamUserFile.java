package kz.iitu.Library.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OteuIslamUserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private OteuIslamUser user;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;  // AVATAR или DOCUMENT

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private String path;

    private LocalDateTime uploadedAt;

    @PrePersist
    public void prePersist() {
        if (uploadedAt == null) uploadedAt = LocalDateTime.now();
    }
}