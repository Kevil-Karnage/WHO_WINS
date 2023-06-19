package com.rozhnov.who_wins.config;

import lombok.Getter;

@Getter
public class BaseException extends Exception{
    String name = "BaseException";
    String description;

    public BaseException(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
