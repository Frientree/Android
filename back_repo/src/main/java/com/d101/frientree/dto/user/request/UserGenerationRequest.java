package com.d101.frientree.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGenerationRequest {

    private String userEmail;

    private String userPw;

    private String userNickname;

}