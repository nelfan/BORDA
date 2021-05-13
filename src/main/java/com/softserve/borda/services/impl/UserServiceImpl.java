package com.softserve.borda.services.impl;

import com.softserve.borda.entities.Board;
import com.softserve.borda.entities.User;
import com.softserve.borda.repositories.UserRepository;
import com.softserve.borda.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        // TODO: Throw custom exception
        return user.orElse(null);
    }

    @Override
    public User createOrUpdate(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getId() != null) {
            Optional<User> userOptional = userRepository.findById(user.getId());

            if (userOptional.isPresent()) {
                User newUser = userOptional.get();
                newUser.setUsername(user.getUsername());
                newUser.setPassword(user.getPassword());
                newUser.setEmail(user.getEmail());
                newUser.setFirstName(user.getFirstName());
                newUser.setLastName(user.getLastName());
                newUser.setAvatar(user.getAvatar());
                return userRepository.save(newUser);
            }
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Board> getBoardsByUserId(Long id) {
        return userRepository.findById(id).get().getUserBoardRelations().stream().map(
                i -> i.getBoard()
        ).collect(Collectors.toList());
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
        // TODO Throw custom exception
    }

    @Override
    public User findByLoginAndPassword(String login, String password) {
        User user = getUserByUsername(login);
        if (user != null &&
                (passwordEncoder.matches(password, user.getPassword()))) {
            return user;
        }
        return null;
    }
}
