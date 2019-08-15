package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import ru.training.karaf.model.User;

public interface UserRepo {
    List<? extends User> getAll();
    void create(String login, String firstName, String lastName, String address, Integer age, Set<String> properties);
    Optional<? extends User> get(String login);
    void delete(String login);
    void assignTag(String login, String name, String value);
}
