package com.whatslovermbti.mbti_prj.domain.appSeedHistory;

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

    @Id
    private String name;
}
