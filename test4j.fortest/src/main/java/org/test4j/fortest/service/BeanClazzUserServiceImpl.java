package org.test4j.fortest.service;

import java.util.List;

import org.test4j.fortest.beans.User;

@SuppressWarnings("unused")
public class BeanClazzUserServiceImpl implements UserService {
    private UserDao        userDao;

    private UserAnotherDao userAnotherDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserAnotherDao(UserAnotherDao userAnotherDao) {
        this.userAnotherDao = userAnotherDao;
    }

    @Override
    public String getServiceName() {
        return "BeanClazzUserServiceImpl";
    }

    @Override
    public void insertUser(User user) {
    }

    @Override
    public void insertUserException(User user) throws Exception {
    }

    @Override
    public double paySalary(String postcode) {
        return 0;
    }

    @Override
    public List<User> findAllUser() {
        return this.userDao.findAllUser();
    }

    @Override
    public void insertUserWillException(User user) throws Exception {

    }
}
