package kz.iitu.Library.domain.entity;

import jakarta.persistence.*;
import kz.iitu.Library.domain.enums.OteuIslamBorrowStatus;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OteuIslamBorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private OteuIslamUser user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private OteuIslamBook book;

    @Column(nullable = false)
    private LocalDate borrowDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private OteuIslamBorrowStatus status;
}