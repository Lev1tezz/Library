package kz.iitu.Library.service;

import kz.iitu.Library.domain.entity.OteuIslamBook;
import kz.iitu.Library.domain.entity.OteuIslamBorrowRecord;
import kz.iitu.Library.domain.entity.OteuIslamUser;
import kz.iitu.Library.domain.enums.OteuIslamBorrowStatus;
import kz.iitu.Library.exception.OteuIslamBadRequestException;
import kz.iitu.Library.exception.OteuIslamNotFoundException;
import kz.iitu.Library.repository.OteuIslamBookRepository;
import kz.iitu.Library.repository.OteuIslamBorrowRepository;
import kz.iitu.Library.repository.OteuIslamUserRepository;
import kz.iitu.Library.web.dto.request.OteuIslamBorrowRequest;
import kz.iitu.Library.web.dto.response.OteuIslamBorrowResponse;
import kz.iitu.Library.web.mapper.OteuIslamBorrowMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OteuIslamBorrowService {

    private static final Logger log = LoggerFactory.getLogger(OteuIslamBorrowService.class);

    private final OteuIslamBorrowRepository borrowRepository;
    private final OteuIslamBookRepository bookRepository;
    private final OteuIslamUserRepository userRepository;
    private final OteuIslamBorrowMapper borrowMapper;
    private final OteuIslamNotificationService notificationService;

    public OteuIslamBorrowResponse borrowBook(String email, OteuIslamBorrowRequest request) {
        log.info("User {} borrowing book id: {}", email, request.getBookId());

        OteuIslamUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OteuIslamNotFoundException("User not found"));

        OteuIslamBook book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new OteuIslamNotFoundException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new OteuIslamBadRequestException("No available copies for: " + book.getTitle());
        }

        if (borrowRepository.existsByUserIdAndBookIdAndStatus(
                user.getId(), book.getId(), OteuIslamBorrowStatus.BORROWED)) {
            throw new OteuIslamBadRequestException("You already borrowed this book");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        OteuIslamBorrowRecord record = OteuIslamBorrowRecord.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(request.getDueDate())
                .status(OteuIslamBorrowStatus.BORROWED)
                .build();

        OteuIslamBorrowRecord saved = borrowRepository.save(record);
        notificationService.sendBorrowNotification(email, book.getTitle());

        return borrowMapper.toResponse(saved);
    }

    public OteuIslamBorrowResponse returnBook(String email, Long recordId) {
        log.info("User {} returning borrow record id: {}", email, recordId);

        OteuIslamBorrowRecord record = borrowRepository.findById(recordId)
                .orElseThrow(() -> new OteuIslamNotFoundException("Borrow record not found"));

        if (!record.getUser().getEmail().equals(email)) {
            throw new OteuIslamBadRequestException("This borrow record does not belong to you");
        }

        if (record.getStatus() == OteuIslamBorrowStatus.RETURNED) {
            throw new OteuIslamBadRequestException("Book already returned");
        }

        record.setStatus(OteuIslamBorrowStatus.RETURNED);

        OteuIslamBook book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        OteuIslamBorrowRecord saved = borrowRepository.save(record);
        notificationService.sendReturnNotification(email, book.getTitle());

        return borrowMapper.toResponse(saved);
    }

    public List<OteuIslamBorrowResponse> getMyBorrows(String email) {
        log.info("Fetching borrows for user: {}", email);
        OteuIslamUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OteuIslamNotFoundException("User not found"));
        return borrowRepository.findByUserId(user.getId())
                .stream()
                .map(borrowMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<OteuIslamBorrowResponse> getAll() {
        log.info("Fetching all borrow records");
        return borrowRepository.findAll()
                .stream()
                .map(borrowMapper::toResponse)
                .collect(Collectors.toList());
    }

    public OteuIslamBorrowResponse getById(Long id) {
        log.info("Fetching borrow record by id: {}", id);
        return borrowMapper.toResponse(
                borrowRepository.findById(id)
                        .orElseThrow(() -> new OteuIslamNotFoundException("Borrow record not found"))
        );
    }
}