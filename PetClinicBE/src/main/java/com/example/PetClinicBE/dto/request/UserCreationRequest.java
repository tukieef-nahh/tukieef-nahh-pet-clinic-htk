
package com.example.PetClinicBE.dto.request;

import lombok.*;

@Getter
@Setter
public class UserCreationRequest {
    private String name;
    private String phone;
    private String email;
    private String address;
    private String password;
}
