package com.optimagrowth.license.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class Organization extends RepresentationModel<Organization> {

    private String id;
    private String name;
    private String contactName;
    private String contactEmail;
    private String contactPhone;

}