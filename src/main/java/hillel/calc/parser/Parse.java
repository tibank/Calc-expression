package hillel.calc.parser;

public interface Parse<T> {
    T parsing(String str);
}
