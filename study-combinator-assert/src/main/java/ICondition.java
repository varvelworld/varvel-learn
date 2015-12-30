/**
 * Created by varvelworld on 2015/12/30.
 */
@FunctionalInterface
public interface ICondition<T> {
    boolean test(T actual);
}
