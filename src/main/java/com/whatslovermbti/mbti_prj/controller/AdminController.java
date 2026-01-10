package com.whatslovermbti.mbti_prj.controller;

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.Review;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean active,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                adminService.getUsers(keyword, active, pageable)
        );
    }

    // 활성 회원 조회
    @GetMapping("/users/active")
    public ResponseEntity<Page<User>> getActiveUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getActiveUsers(pageable));
    }

    // 비활성 회원 조회
    @GetMapping("/users/inactive")
    public ResponseEntity<Page<User>> getInactiveUsers(Pageable pageable) {
        return ResponseEntity.ok(adminService.getInactiveUsers(pageable));
    }

    // 회원 활성화
    @PatchMapping("/users/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
        adminService.activateUser(userId);
        return ResponseEntity.noContent().build();
    }

    // 회원 비활성화
    @PatchMapping("/users/{userId}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
        adminService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reviews")
    public ResponseEntity<Page<Review>> getReviews(
            @RequestParam(required = false) String placeName,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                adminService.getReviews(placeName, pageable)
        );
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        adminService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/places")
    public ResponseEntity<Page<Place>> getPlaces(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Category category,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                adminService.getPlaces(keyword, category, pageable)
        );
    }
    @PatchMapping("/{placeId}/soft-delete")
    public ResponseEntity<Void> softDeletePlace(
            @PathVariable Long placeId
    ) {
        adminService.softDeletePlace(placeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<Void> hardDeletePlace(
            @PathVariable Long placeId
    ) {
        adminService.hardDeletePlace(placeId);
        return ResponseEntity.noContent().build();
    }
}
