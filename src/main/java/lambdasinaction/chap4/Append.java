package lambdasinaction.chap4;

import static java.util.stream.Collectors.toList;
import static lambdasinaction.chap4.Dish.menu;

import java.util.List;

import lambdasinaction.AppendTest;

public class Append extends AppendTest {

	public static void main(String... args) {
		test(Append.class);
	}

	@Test(2)
	public static class GettingStartedWithStreams {
		public void test() {
			List<String> threeHighCaloricDishNames = //
					menu.stream() //
							.filter(d -> d.getCalories() > 300) //
							.map(Dish::getName) //
							.limit(3) //
							.collect(toList()); //
			System.out.println(threeHighCaloricDishNames);
		}
	}

	@Test(4)
	public static class StreamOperations {

		public void testIntermediateOperations() {
			List<String> names = //
					menu.stream() //
							.filter(d -> { //
								System.out.println("filtering " + d.getName());
								return d.getCalories() > 300;
							}).map(d -> { //
								System.out.println("mapping " + d.getName());
								return d.getName();
							}).limit(3) //
							.collect(toList()); // Terminal
			System.out.println(names);
		}

		public void testTerminalOperations() {
			menu.stream().forEach(System.out::println);

			long count = menu.stream() //
					.filter(d -> d.getCalories() > 300) //
					.distinct() //
					.limit(3) //
					.count(); // Terminal
			System.out.println("count: " + count);
		}

	}

}
