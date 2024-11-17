package org.survivalcraft.launcher.exception;

public class UpdateException extends Exception{

    public UpdateException(String message) {
        super(message);
    }

    public UpdateException(Throwable cause) {
        super(cause);
    }
}