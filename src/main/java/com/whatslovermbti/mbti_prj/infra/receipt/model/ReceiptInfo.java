package com.whatslovermbti.mbti_prj.infra.receipt.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReceiptInfo {

    private String storeName;
    private String address;
    private LocalDate date;
    private Integer totalAmount;
}