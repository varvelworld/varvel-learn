import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/**
 * Created by varvelworld on 2015/12/30.
 */
public class Conditions {

    public static <T> ICondition<T> _true() {
        return actual -> true;
    }

    public static <T> ICondition<T> _false() {
        return actual -> false;
    }

    public static <T> ICondition<T> eq(T expect) {
        return actual -> actual.equals(expect);
    }

    public static <T> ICondition<T> same(T expect) {
        return actual -> actual == expect;
    }

    public static <T extends Comparable<T>> ICondition<T> gt(T expect) {
        return actual -> actual.compareTo(expect) > 0;
    }

    public static <T> ICondition<T> gt(T expect, Comparator<T> comparator) {
        return actual -> comparator.compare(actual, expect) > 0;
    }

    public static <T extends Comparable<T>> ICondition<T> ge(T expect) {
        return actual -> actual.compareTo(expect) >= 0;
    }

    public static <T> ICondition<T> ge(T expect, Comparator<T> comparator) {
        return actual -> comparator.compare(actual, expect) >= 0;
    }

    public static <T extends Comparable<T>> ICondition<T> lt(T expect) {
        return actual -> actual.compareTo(expect) < 0;
    }

    public static <T> ICondition<T> lt(T expect, Comparator<T> comparator) {
        return actual -> comparator.compare(actual, expect) < 0;
    }

    public static <T extends Comparable<T>> ICondition<T> le(T expect) {
        return actual -> actual.compareTo(expect) <= 0;
    }

    public static <T> ICondition<T> le(T expect, Comparator<T> comparator) {
        return actual -> comparator.compare(actual, expect) <= 0;
    }

    @SafeVarargs
    public static <T> ICondition<T> and(ICondition<T> ... conditions) {
        return actual -> Arrays.stream(conditions).allMatch(c -> c.test(actual));
    }

    @SafeVarargs
    public static <T> ICondition<T> or(ICondition<T> ... conditions) {
        return actual -> Arrays.stream(conditions).anyMatch(c -> c.test(actual));
    }

    public static <T extends Collection<E>, E> ICondition<T> any(ICondition<E> condition) {
        return actual -> actual.stream().anyMatch(condition::test);
    }

    public static <T extends Collection<E>, E> ICondition<T> all(ICondition<E> condition) {
        return actual -> actual.stream().allMatch(condition::test);
    }
}
