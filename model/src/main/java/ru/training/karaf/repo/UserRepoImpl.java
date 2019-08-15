package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.apache.aries.jpa.template.JpaTemplate;

import ru.training.karaf.model.TagDO;
import ru.training.karaf.model.User;
import ru.training.karaf.model.UserDO;

public class UserRepoImpl implements UserRepo {
    private JpaTemplate template;

    public UserRepoImpl(JpaTemplate template) {
        this.template = template;
    }

    @Override
    public List<User> getAll() {
        return template.txExpr(em -> em.createNamedQuery(UserDO.GET_ALL, UserDO.class).getResultList())
                .stream()
                .map(UserImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public void create(String login, String firstName, String lastName, String address, Integer age, Set<String> properties) {
        UserDO userToCreate = new UserDO();
        userToCreate.setLogin(login);
        userToCreate.setFirstName(firstName);
        userToCreate.setLastName(lastName);
        userToCreate.setAddress(address);
        userToCreate.setAge(age);
        userToCreate.setProperties(properties);
        template.tx(em -> em.persist(userToCreate));
    }

    @Override
    public Optional<User> get(String login) {
        return template.txExpr(em -> getByLogin(login, em)).map(UserImpl::new);
    }

    @Override
    public void delete(String login) {
        template.tx(em -> getByLogin(login, em).ifPresent(em::remove));
    }

    @Override
    public void assignTag(String login, String name, String value) {
        UserDO usr = template.txExpr(em -> {
            Optional<UserDO> user = getByLogin(login, em);
            UserDO userDO = user.get();
            TagDO tagDO = new TagDO();
            tagDO.setName(name);
            tagDO.setValue(value);
            tagDO.setUser(userDO);
            userDO.getTags().add(tagDO);

            em.persist(userDO);
            return userDO;
        });
        System.out.println(usr); // we have tag in bd but not in user
    }

    private Optional<UserDO> getByLogin(String login, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(UserDO.GET_BY_LOGIN, UserDO.class).setParameter("login", login)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}

class UserImpl implements User{

    private final UserDO userDO;

    UserImpl(UserDO userDO) {
        this.userDO = userDO;
    }


    @Override
    public String getFirstName() {
        return userDO.getFirstName();
    }

    @Override
    public String getLastName() {
        return userDO.getLastName();
    }

    @Override
    public String getLogin() {
        return userDO.getLogin();
    }

    @Override
    public Integer getAge() {
        return userDO.getAge();
    }

    @Override
    public String getAddress() {
        return userDO.getAddress();
    }

    @Override
    public Set<String> getProperties() {
        return userDO.getProperties();
    }

    UserDO getDO(){
        return userDO;
    }
}
