package com.d101.frientree.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChangeNicknameRequestDTO {

    private String userNickname;
}
