package com.mcw.football.domain.dto;

import lombok.Data;

@Data
public class StudentUpdateRequestDto {

    private String name;
    private String state;
    private String position;
    private Double salary;
}
