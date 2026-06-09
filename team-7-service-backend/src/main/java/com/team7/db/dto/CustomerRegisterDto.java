package com.team7.db.dto;


import com.team7.db.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterDto {

    private String firstName;
    private String lastName;
    private String birth;
    private String phone;
    private String email;
    private String accountId;
    private String accountPassword;
    private String gender;

    public CustomerRegisterDto(User user){
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.birth = user.getBirth().toString();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.accountId = user.getAccountId();
        this.accountPassword = user.getAccountPassword();
        this.gender = user.getGender();
    }
}
