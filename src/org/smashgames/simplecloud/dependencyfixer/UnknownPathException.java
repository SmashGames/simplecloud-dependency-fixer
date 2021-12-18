package org.smashgames.simplecloud.dependencyfixer;

public class UnknownPathException extends Exception {

    public UnknownPathException()
    {
        super();
    }

    public UnknownPathException(String message)
    {
        super(message);
    }
}
