package org.example.realworldapi.infrastructure.repository.panache;

import static io.quarkus.panache.common.Parameters.with;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.model.entity.UsersFollowed;
import org.example.realworldapi.domain.model.repository.UserRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepositoryPanache implements PanacheRepository<User>, UserRepository {

  @Override
  public User create(User user) {
    persistAndFlush(user);
    return user;
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return find("upper(email)", email.toUpperCase().trim()).firstResultOptional();
  }

  @Override
  public boolean existsBy(String field, String value) {
    return count("upper(" + field + ")", value.toUpperCase().trim()) > 0;
  }

  @Override
  public Optional<User> findUserById(Long id) {
    return findByIdOptional(id);
  }

  @Override
  public boolean existsUsername(Long excludeId, String username) {
    return count(
            "id != :excludeId and upper(username) = :username",
            with("excludeId", excludeId).and("username", username.toUpperCase().trim()))
        > 0;
  }

  @Override
  public boolean existsEmail(Long excludeId, String email) {
    return count(
            "id != :excludeId and upper(email) = :email",
            with("excludeId", excludeId).and("email", email.toUpperCase().trim()))
        > 0;
  }

  @Override
  public Optional<User> findByUsernameOptional(String username) {
    return find("upper(username)", username.toUpperCase().trim()).firstResultOptional();
  }

  @Override
  public List<User> getAll() {
    return this.findAll().list();
  }

@Override
public List<User> getAllFollowing(String username) {
	
	List<User> res = new ArrayList<User>();
	List<User> us = find("select distinct u from User u join fetch o.following where upper(username) = :username", with("username", username.toUpperCase().trim())).list();
	for (User u : us) {
		for (UsersFollowed following: u.getFollowing()) {
			res.add(following.getFollowed());
		}
	}
	return res;
}

@Override
public List<User> getAllFollowed(String username) {
	List<User> res = new ArrayList<User>();
	List<User> us = find("select distinct u from User u join fetch o.followedBy where upper(username) = :username", with("username", username.toUpperCase().trim())).list();
	for (User u : us) {
		for (UsersFollowed followed: u.getFollowedBy()) {
			res.add(followed.getUser());
		}
	}
	return res;
}

}
