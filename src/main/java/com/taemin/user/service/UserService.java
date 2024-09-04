package com.taemin.user.service;

import com.taemin.user.common.ErrorCode;
import com.taemin.user.domain.user.User;
import com.taemin.user.exception.UserException;
import com.taemin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

}
