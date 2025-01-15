package com.example.springhibernateneonapp.service;
import com.example.springhibernateneonapp.entity.UserProfile;
import com.example.springhibernateneonapp.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public UserProfile findById(Long id) {
        return userProfileRepository.findById(id).orElse(null);
    }
}