/**
 * Created by varvelworld on 2015/12/30.
 */
public class Assert<T> implements IAssert<T> {

    protected T actual;
    protected ICondition<T> condition;

    public Assert(T actual) {
        this.actual = actual;
        this.condition = Conditions._true();
    }

    public Assert(T actual, ICondition<T> condition) {
        this.actual = actual;
        this.condition = condition;
    }

    public IAssert<T> add(ICondition<T> condition) {
        this.condition = Conditions.and(this.condition, condition);
        return this;
    }

    @Override
    public IAssert<T> match() {
        if(!condition.test(actual)) {
            throw new AssertException();
        }
        this.condition = Conditions._true();
        return this;
    }
}
