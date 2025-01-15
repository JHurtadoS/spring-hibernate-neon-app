package com.example.springhibernateneonapp.service;
import com.example.springhibernateneonapp.entity.User;
import com.example.springhibernateneonapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.springhibernateneonapp.entity.UserProfile;
import com.example.springhibernateneonapp.repository.UserProfileRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User createUserWithProfile(String email, String password, String name, String phone, String themePreference, boolean isAdmin) {


        System.out.println(email);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setName(name);
        profile.setPhone(phone);
        profile.setThemePreference(themePreference);
        profile.setIsAdmin(isAdmin);
        profile.setUser(savedUser);

        userProfileRepository.save(profile);

        return savedUser;
    }
}