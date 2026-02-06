package ru.sicampus.bootcamp2026.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found!");
    }
}
