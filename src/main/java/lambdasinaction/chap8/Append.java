package lambdasinaction.chap8;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;

import lambdasinaction.AppendTest;

public class Append extends AppendTest {

	public static void main(String... args) {
		test(Append.class);
	}

	@Test(1)
	public static class RefactoringForImprovedReadabilityAndFlexibility {

		@Test(desc = "From anonymous classes to lambda expressions")
		public void test2() {
			title("p1");
			Runnable r1 = new Runnable() {
				public void run() {
					System.out.println("Hello");
				}
			};
			r1.run();

			Runnable r2 = () -> System.out.println("Hello");
			r2.run();

			title("p2");
			int a = 10;
			Runnable r1b = () -> {
				// int a = 2; // 编译错误！
				System.out.println(a);
			};
			r1b.run();

			Runnable r2b = new Runnable() {
				public void run() {
					int a = 2; // 一切正常
					System.out.println(a);
				}
			};
			r2b.run();

			title("p3");
			doSomething(new Task() {
				public void execute() {
					System.out.println("Danger danger!!");
				}
			});

			// 麻烦来了： doSomething(Runnable) 和 doSomething(Task) 都匹配该类型
			// doSomething(() -> System.out.println("Danger danger!!"));

			// 你可以对Task尝试使用显式的类型转换来解决这种模棱两可的情况：
			doSomething((Task) () -> System.out.println("Danger danger!!"));
		}

		interface Task {
			public void execute();
		}

		public static void doSomething(Runnable r) {
			r.run();
		}

		public static void doSomething(Task a) {
			a.execute();
		}

		@Test(desc = "From lambda expressions to method references")
		public void test3() {
			title("p1");
			Collections.shuffle(inventory);
			inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
			System.out.println("inventory: " + inventory);

			Collections.shuffle(inventory);
			inventory.sort(comparing(Apple::getWeight));
			System.out.println("inventory: " + inventory);

			title("p2");
			int totalCalories = menu.stream().map(Dish::getCalories).reduce(0, (c1, c2) -> c1 + c2);
			int totalCalories2 = menu.stream().collect(summingInt(Dish::getCalories));

			System.out.println("totalCalories1: " + totalCalories);
			System.out.println("totalCalories2: " + totalCalories2);
		}

		@Test(desc = "From imperative data processing to Streams")
		public void test4() {
			// 下面的命令式代码使用了两种模式：筛选和抽取
			List<String> dishNames = new ArrayList<>();
			for (Dish dish : menu) {
				if (dish.getCalories() > 300) {
					dishNames.add(dish.getName());
				}
			}

			List<String> dishNames2 = menu.parallelStream().filter(d -> d.getCalories() > 300).map(Dish::getName)
					.collect(toList());

			System.out.println("dishNames1: " + dishNames);
			System.out.println("dishNames2: " + dishNames2);
		}

		@Test(desc = "Improving code flexibility")
		public void test5() throws IOException {
			title("2. 有条件的延迟执行");
			if (logger.isLoggable(Level.FINER)) {
				logger.finer("Problem: " + generateDiagnostic());
			}

			logger.log(Level.FINER, "Problem: " + generateDiagnostic());

			logger.log(Level.FINER, () -> "Problem: " + generateDiagnostic());

			title("3. 环绕执行");
			String oneLine = processFile((BufferedReader b) -> b.readLine());
			String twoLines = processFile((BufferedReader b) -> b.readLine() + b.readLine());
			System.out.println("oneLine: " + oneLine);
			System.out.println("twoLines: " + twoLines);
		}

		private String generateDiagnostic() {
			return "generateDiagnostic message";
		}

		public static String processFile(BufferedReaderProcessor p) throws IOException {
			try (BufferedReader br = new BufferedReader(getReader("lambdasinaction/chap3/data.txt"))) {
				return p.process(br);
			}
		}

		public interface BufferedReaderProcessor {
			String process(BufferedReader b) throws IOException;
		}

	}

	// @Test(3)
	public static class TestingLambdas {

		@org.junit.Test
		public void testMoveRightBy() throws Exception {
			Point p1 = new Point(5, 5);
			Point p2 = p1.moveRightBy(10);
			assertEquals(15, p2.getX());
			assertEquals(5, p2.getY());
		}

		@org.junit.Test
		public void testComparingTwoPoints() throws Exception {
			Point p1 = new Point(10, 15);
			Point p2 = new Point(10, 20);
			int result = Point.compareByXAndThenY.compare(p1, p2);
			assertEquals(-1, result);
		}

		@org.junit.Test
		public void testMoveAllPointsRightBy() throws Exception {
			List<Point> points = Arrays.asList(new Point(5, 5), new Point(10, 5));
			List<Point> expectedPoints = Arrays.asList(new Point(15, 5), new Point(20, 5));
			List<Point> newPoints = Point.moveAllPointsRightBy(points, 10);
			assertEquals(expectedPoints, newPoints);
		}

		@org.junit.Test
		public void testFilter() throws Exception {
			List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
			List<Integer> even = filter(numbers, i -> i % 2 == 0);
			List<Integer> smallerThanThree = filter(numbers, i -> i < 3);
			assertEquals(Arrays.asList(2, 4), even);
			assertEquals(Arrays.asList(1, 2), smallerThanThree);
		}

	}

	public static class Point {
		public final static Comparator<Point> compareByXAndThenY = comparing(Point::getX).thenComparing(Point::getY);

		private final int x;
		private final int y;

		private Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		@Override
		public boolean equals(Object obj) {
			Point p = (Point) obj;
			return this.x == p.x && this.y == p.y;
		}

		public Point moveRightBy(int x) {
			return new Point(this.x + x, this.y);
		}

		public static List<Point> moveAllPointsRightBy(List<Point> points, int x) {
			return points.stream().map(p -> new Point(p.getX() + x, p.getY())).collect(toList());
		}

	}

	@Test(4)
	public static class Debugging {

		@Test(desc = "Examining the stack trace")
		public void test1() {
			List<Point> points = Arrays.asList(new Point(12, 2), null);
			points.stream().map(p -> p.getX()).forEach(System.out::println);
		}

		@Test(desc = "Examining the stack trace")
		public void test1b() {
			List<Integer> numbers = Arrays.asList(1, 2, 3);
			numbers.stream().map(Debugging::divideByZero).forEach(System.out::println);
		}

		public static int divideByZero(int n) {
			return n / 0;
		}

		@Test(desc = "Logging information")
		public void test2() {
			List<Integer> result = Stream.of(2, 3, 4, 5) //
					.peek(x -> System.out.println("taking from stream: " + x)) //
					.map(x -> x + 17) //
					.peek(x -> System.out.println("after map: " + x)) //
					.filter(x -> x % 2 == 0) //
					.peek(x -> System.out.println("after filter: " + x)) //
					.limit(3) //
					.peek(x -> System.out.println("after limit: " + x)) //
					.collect(toList());
			System.out.println("result: " + result);
		}

	}

}
