package lambdasinaction.chap7;

import java.util.concurrent.ForkJoinPool;

import lambdasinaction.AppendTest;

public class Append extends AppendTest {

	public static void main(String... args) {
		test(Append.class);
	}

	@Test(1)
	public static class ParallelStreams {

		@Test(desc = "Turning a sequential stream into a parallel one")
		public void test1() {
			System.out.println("ForkJoinPool.threads: " + ForkJoinPool.getCommonPoolParallelism());
			System.out.println("ForkJoinPool.threads: " + Runtime.getRuntime().availableProcessors());
			System.out.println("ForkJoinPool.threads: "
					+ System.getProperty("java.util.concurrent.ForkJoinPool.common.parallelism"));
		}

	}

}
