import java.util.Collection;

/**
 * Created by varvelworld on 2015/12/30.
 */
public class CollectionAssert<T extends Collection<E>, E>
        extends Assert<T>
        implements ICollectionAssert<T, E> {

    public CollectionAssert(T actual) {
        super(actual);
    }

    @Override
    public ICollectionAssert<T, E> any(ICondition<E> condition) {
        return (ICollectionAssert<T, E>) add(Conditions.any(condition));
    }

    @Override
    public ICollectionAssert<T, E> all(ICondition<E> condition) {
        return (ICollectionAssert<T, E>) add(Conditions.all(condition));
    }
}
