package com.mcw.football.domain.dto;

import lombok.Data;

@Data
public class StudentUpdateRequest {

    private String name;
    private String state;
    private String position;
    private Double salary;
}
