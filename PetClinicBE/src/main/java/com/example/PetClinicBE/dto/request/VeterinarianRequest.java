package com.example.PetClinicBE.dto.request;

import lombok.*;

@Getter
@Setter
public class VeterinarianRequest {
    private String name;
    private String phone;
    private String email;
    private String specialization;
    private String password;
}
