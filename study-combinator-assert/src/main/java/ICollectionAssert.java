import java.util.Collection;

/**
 * Created by varvelworld on 2015/12/30.
 */
public interface ICollectionAssert<T extends Collection<E>, E> extends IAssert<T> {
    ICollectionAssert<T, E> any(ICondition<E> condition);
    ICollectionAssert<T, E> all(ICondition<E> condition);
}
