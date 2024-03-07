package com.java.programming.springwebrestcall.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class Employee {
    private int id;
    private String name;
    private String title;
    private String department;
}
