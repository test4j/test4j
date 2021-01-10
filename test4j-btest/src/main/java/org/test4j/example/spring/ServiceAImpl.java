package org.test4j.example.spring;

import org.springframework.stereotype.Service;

@Service
public class ServiceAImpl implements ServiceA {
    @Override
    public String say(String hi) {
        return hi;
    }
}