package springbook.learningtest.jdk.template;

//public interface LineCallback {
//    Integer doSomethingWithLine(String line, Integer value);
//}

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}