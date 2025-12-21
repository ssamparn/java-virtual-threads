package com.explore.javavirtualthreadsstreamgathererapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "city")
public class CityEntity {

    @Id
    private Integer id;
    private String name;
    private String state;
    private String country;
    private double latitude;
    private double longitude;
}