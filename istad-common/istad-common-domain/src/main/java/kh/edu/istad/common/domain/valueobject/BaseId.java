package kh.edu.istad.common.domain.valueobject;

public interface BaseId<T> {

    T value();

    default T getValue() {
        return value();
    }
}
