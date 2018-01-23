package lambdasinaction.chap5;

import static java.util.stream.Collectors.toList;
import static lambdasinaction.chap4.Dish.menu;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lambdasinaction.AppendTest;
import lambdasinaction.chap4.Dish;

public class Append extends AppendTest {

	public static void main(String... args) {
		test(Append.class);
	}

	@Test(1)
	public static class FilteringAndSlicing {

		@Test(desc = "Filtering with a predicate")
		public void test1() {
			List<Dish> vegetarianMenu = menu.stream() //
					.filter(Dish::isVegetarian) //
					.collect(toList()); //
			vegetarianMenu.forEach(System.out::println);
		}

		@Test(desc = "Filtering unique elements")
		public void test2() {
			List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
			numbers.stream() //
					.filter(i -> i % 2 == 0) //
					.distinct() //
					.forEach(System.out::println); //
		}

		@Test(desc = "Truncating a stream")
		public void test3() {
			List<Dish> dishes = menu.stream() //
					.filter(d -> d.getCalories() > 300) //
					.limit(3) //
					.collect(toList()); //
			dishes.forEach(System.out::println);
		}

		@Test(desc = "Skipping elements")
		public void test4() {
			List<Dish> dishes = menu.stream() //
					.filter(d -> d.getCalories() > 300) //
					.skip(2) //
					.collect(toList()); //
			dishes.forEach(System.out::println);
		}

		@Test(desc = "Test")
		public void test9() {
			List<Dish> dishes = menu.stream() //
					.filter(d -> d.getType() == Dish.Type.MEAT) //
					.limit(2) //
					.collect(toList()); //
			dishes.forEach(System.out::println);
		}

	}

	@Test(2)
	public static class Mapping {

		@Test(desc = "Applying a function to each element of a stream")
		public void test1() {
			title("name");
			List<String> dishNames = menu.stream() //
					.map(Dish::getName) //
					.collect(toList()); //
			dishNames.forEach(System.out::println);

			title("name length");
			List<Integer> dishNameLengths = menu.stream().map(Dish::getName).map(String::length).collect(toList());
			dishNameLengths.forEach(System.out::println);
		}

		@Test(desc = "Flattening streams")
		public void test2() {
			System.out.println("hello world: " + "hello world".split("").length);
			List<String> words = Arrays.asList("hello", "world");
			List<String> uniqueCharacters = //
					words.stream() //
							.map(w -> w.split("")) //
							.flatMap(Arrays::stream) //
							.distinct() //
							.collect(Collectors.toList()); //
			System.out.println("uniqueCharacters: " + uniqueCharacters);
		}

		@Test(desc = "Test")
		public void test9() {
			title("Q1");
			List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
			List<Integer> squares = //
					numbers.stream() //
							.map(n -> n * n) //
							.collect(toList()); //
			System.out.println("numbers: " + numbers);
			System.out.println("squares: " + squares);

			title("Q2");
			List<Integer> numbers1 = Arrays.asList(1, 2, 3);
			List<Integer> numbers2 = Arrays.asList(3, 4);
			List<int[]> pairs = numbers1.stream() //
					.flatMap(i -> numbers2.stream() //
							.map(j -> new int[] { i, j }) //
					) //
					.collect(toList()); //
			System.out.println("numbers1: " + numbers1);
			System.out.println("numbers2: " + numbers2);
			System.out.print("pairs: [");
			pairs.forEach(a -> System.out.print("(" + a[0] + "," + a[1] + ") "));
			System.out.println("]");

			title("Q3");
			List<int[]> pairs2 = numbers1.stream() //
					.flatMap(i -> numbers2.stream() //
							.filter(j -> (i + j) % 3 == 0) //
							.map(j -> new int[] { i, j }) //
					) //
					.collect(toList()); //
			System.out.print("pairs2: [");
			pairs2.forEach(a -> System.out.print("(" + a[0] + "," + a[1] + ") "));
			System.out.println("]");
		}

	}

	@Test(3)
	public static class FindingAndMatching {

		@Test(desc = "Finding the first element")
		public void test4() {
			List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
			Optional<Integer> firstSquareDivisibleByThree = //
					someNumbers.stream() //
							.map(x -> x * x) //
							.filter(x -> x % 3 == 0) //
							.findFirst(); // 9
			System.out.println("someNumbers: " + someNumbers);
			System.out.println("firstSquareDivisibleByThree: " + firstSquareDivisibleByThree.isPresent());
			firstSquareDivisibleByThree.ifPresent(System.out::println);
		}

	}

	@Test(4)
	public static class Reducing {

		@Test(desc = "Summing the elements")
		public void test1() {
			List<Integer> numbers = Arrays.asList(3, 4, 5, 1, 2);
			System.out.println("numbers: " + numbers);

			int sum = numbers.stream().reduce(0, (a, b) -> a + b);
			System.out.println("sum: " + sum);

			int product = numbers.stream().reduce(1, (a, b) -> a * b);
			System.out.println("product: " + product);

			int sum2 = numbers.stream().reduce(0, Integer::sum);
			System.out.println("sum2: " + sum2);

			Optional<Integer> sum3 = numbers.stream().reduce((a, b) -> (a + b));
			System.out.println("sum3: " + (sum3.isPresent() ? sum3.get() : "empty"));

			numbers = Arrays.asList();
			sum3 = numbers.stream().reduce((a, b) -> (a + b));
			System.out.println("sum3: " + (sum3.isPresent() ? sum3.get() : "empty"));
		}

		@Test(desc = "Maximum and minimum")
		public void test2() {
			List<Integer> numbers = Arrays.asList(3, 4, 5, 1, 2);

			int max1 = numbers.stream().reduce(0, (a, b) -> Integer.max(a, b));
			System.out.println(max1);

			Optional<Integer> max = numbers.stream().reduce(Integer::max);
			max.ifPresent(System.out::println);

			Optional<Integer> min = numbers.stream().reduce(Integer::min);
			min.ifPresent(System.out::println);

			int count = menu.stream().map(d -> 1).reduce(0, (a, b) -> a + b);
			System.out.println("count: " + count);

			long count2 = menu.stream().count();
			System.out.println("count2: " + count2);
		}

	}

}
