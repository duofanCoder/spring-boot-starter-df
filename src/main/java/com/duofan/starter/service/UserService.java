package com.duofan.starter.service;

import com.duofan.starter.dto.model.common.UserDto;

/**
 * Created by Arpit Khandelwal.
 */
public interface UserService {
    /**
     * Register a new user
     *
     * @param userDto
     * @return
     */
    UserDto signup(UserDto userDto);

    /**
     * Search an existing user
     *
     * @param email
     * @return
     */
    UserDto findUserByUsername(String email);

    /**
     * Update profile of the user
     *
     * @param userDto
     * @return
     */
    UserDto updateProfile(UserDto userDto);

    /**
     * Update password
     *
     * @param newPassword
     * @return
     */
    UserDto changePassword(UserDto userDto, String newPassword);
}
