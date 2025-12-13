package com.whatslovermbti.mbti_prj.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppSeedHistory {

    // 파일 실행 시 키워드 존재유무 체크 X

    @Id
    private String name;
}
