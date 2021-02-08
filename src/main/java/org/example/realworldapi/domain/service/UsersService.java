package org.example.realworldapi.domain.service;

import java.util.List;

import org.example.realworldapi.domain.model.entity.User;

public interface UsersService {
  User create(String username, String email, String password);


  List<User> getAll();

  User login(String email, String password);

  User findById(Long id);

  User update(User user);

  User findByUsername(String username);


List<User> getFollowingUser(String username);


List<User> getAllFollowers(String username);
}
