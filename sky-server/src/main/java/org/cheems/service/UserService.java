package org.cheems.service;

import org.cheems.dto.UserLoginDTO;
import org.cheems.entity.User;

public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);
}
