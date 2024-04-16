package im.tretyakov.java;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.LL_Result;

@State
@JCStressTest
@Outcome(id = "1, 0", expect = Expect.ACCEPTABLE)
@Outcome(id = "2, 0", expect = Expect.ACCEPTABLE)
@Outcome(id = "0, 0", expect = Expect.ACCEPTABLE_INTERESTING)
@Outcome(expect = Expect.FORBIDDEN)
public class ContainerConcurrencyTest {
    private final Container container = new Container();

    @Actor
    void valueProducer1() {
        container.accept(1L);
    }

    @Actor
    void valueProducer2() {
        container.accept(2L);
    }

    @Arbiter
    void arbiter(LL_Result result) {
        result.r1 = 0;
        try {
            result.r1 = container.get().stream().findFirst().orElse(0L);
        } catch (Exception e) {
            result.r1 = -1;
        }
        try {
            result.r2 = container.get().stream().findAny().orElse(0L);
        } catch (Exception e) {
            result.r2 = -1;
        }
    }
}
