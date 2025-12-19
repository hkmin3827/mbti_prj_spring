package com.whatslovermbti.mbti_prj.repository;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPlaceOrderByCreateAtDesc(Place place);
}
