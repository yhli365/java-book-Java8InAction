package lambdasinaction.chap11;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lambdasinaction.AppendTest;

public class Append extends AppendTest {

	public static void main(String... args) {
		test(Append.class);
	}

	// @Test(1)
	public static class Futures {

		// @Test(desc = "Futures limitations")
		@org.junit.Test
		public void test1() {
			ExecutorService executor = Executors.newCachedThreadPool();
			Future<Double> future = executor.submit(new Callable<Double>() {
				public Double call() {
					return doSomeLongComputation();
				}
			});
			doSomethingElse();
			try {
				Double result = future.get(10, TimeUnit.SECONDS);
				logger.info("result: " + result);
			} catch (ExecutionException ee) {
				// 计算抛出一个异常
			} catch (InterruptedException ie) {
				// 当前线程在等待过程中被中断
			} catch (TimeoutException te) {
				// 在Future对象完成之前超过已过期
			}
		}

		private Double doSomeLongComputation() {
			logger.info("doSomeLongComputation begin");
			sleep(10, TimeUnit.SECONDS);
			logger.info("doSomeLongComputation end");
			return 1500.0;
		}

		private void doSomethingElse() {
			logger.info("doSomethingElse begin");
			sleep(5, TimeUnit.SECONDS);
			logger.info("doSomethingElse end");
		}

	}

}
