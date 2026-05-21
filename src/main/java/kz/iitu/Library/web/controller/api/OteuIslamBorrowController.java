package kz.iitu.Library.web.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.iitu.Library.service.OteuIslamBorrowService;
import kz.iitu.Library.web.dto.request.OteuIslamBorrowRequest;
import kz.iitu.Library.web.dto.response.OteuIslamBorrowResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
@Tag(name = "Borrows")
public class OteuIslamBorrowController {

    private final OteuIslamBorrowService borrowService;

    @PostMapping
    @Operation(summary = "Borrow a book")
    public ResponseEntity<OteuIslamBorrowResponse> borrowBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody OteuIslamBorrowRequest request) {
        return ResponseEntity.ok(borrowService.borrowBook(userDetails.getUsername(), request));
    }

    @PutMapping("/{id}/return")
    @Operation(summary = "Return a book")
    public ResponseEntity<OteuIslamBorrowResponse> returnBook(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(borrowService.returnBook(userDetails.getUsername(), id));
    }

    @GetMapping("/my")
    @Operation(summary = "Get my borrow records")
    public ResponseEntity<List<OteuIslamBorrowResponse>> getMyBorrows(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(borrowService.getMyBorrows(userDetails.getUsername()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all borrow records (admin only)")
    public ResponseEntity<List<OteuIslamBorrowResponse>> getAll() {
        return ResponseEntity.ok(borrowService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get borrow record by id (admin only)")
    public ResponseEntity<OteuIslamBorrowResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(borrowService.getById(id));
    }
}