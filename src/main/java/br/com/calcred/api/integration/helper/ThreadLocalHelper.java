package br.com.calcred.api.integration.helper;

public class ThreadLocalHelper extends ThreadLocal {
    public static ThreadLocal<String> threadLocalSoapAction = new ThreadLocal<>();
}
