package org.test4j.module.spring.testedbeans.resource;

import java.util.List;

import javax.annotation.Resource;

import org.test4j.fortest.beans.User;
import org.test4j.fortest.service.UserDao;
import org.test4j.fortest.service.UserService;

public class UserServiceResourceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public double paySalary(String postcode) {
        List<User> users = this.userDao.findUserByPostcode(postcode);
        double total = 0.0d;
        if (users == null || users.size() == 0) {
            return total;
        }
        for (User user : users) {
            total += user.getSarary();
        }
        return total;
    }

    @Override
    public void insertUser(User user) {
        this.userDao.insertUser(user);
    }

    @Override
    public void insertUserWillException(User user) throws Exception {
        this.userDao.insertUser(user);
        throw new Exception("insert user exception!");
    }

    @Override
    public void insertUserException(User user) throws Exception {
        throw new Exception("insert user exception!");
    }

    @Override
    public String getServiceName() {
        return "org.test4j.module.spring.resource.UserServiceResourceImpl";
    }

    @Override
    public List<User> findAllUser() {
        return this.userDao.findAllUser();
    }
}
