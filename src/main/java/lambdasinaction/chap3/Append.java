package lambdasinaction.chap3;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import lambdasinaction.AppendTest;

public class Append extends AppendTest {

	public static void main(String... args) {
		test(Append.class);
	}

	@Test(4)
	public static class UsingFunctionalInterfaces {

		public static void test() {
			title("Predicate");
			Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
			List<String> listOfStrings = Arrays.asList("hello", "", "world");
			List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);
			System.out.println(nonEmpty);

			title("Consumer");
			forEach(Arrays.asList(1, 2, 3, 4, 5), (Integer i) -> System.out.println(i));

			title("Function");
			// [7, 2, 6]
			List<Integer> l = map(Arrays.asList("lambdas", "in", "action"), (String s) -> s.length());
			System.out.println(l);

			title("原始类型特化");
			IntPredicate evenNumbers = (int i) -> i % 2 == 0;
			System.out.println("unboxing: " + evenNumbers.test(1000));
			Predicate<Integer> oddNumbers = (Integer i) -> i % 2 == 1;
			System.out.println("boxing: " + oddNumbers.test(1000));
		}

		public static <T> List<T> filter(List<T> list, Predicate<T> p) {
			List<T> results = new ArrayList<>();
			for (T s : list) {
				if (p.test(s)) {
					results.add(s);
				}
			}
			return results;
		}

		public static <T> void forEach(List<T> list, Consumer<T> c) {
			for (T i : list) {
				c.accept(i);
			}
		}

		public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
			List<R> result = new ArrayList<>();
			for (T s : list) {
				result.add(f.apply(s));
			}
			return result;
		}

	}

	@Test(8)
	public static class UsefulMethodsToComposeLambdaExpressions {

		public void testComparator() {
			Collections.shuffle(inventory);
			inventory.sort(comparing(Apple::getWeight).reversed());
			System.out.println(inventory);

			Collections.shuffle(inventory);
			inventory.sort(comparing(Apple::getWeight) //
					.reversed() //
					.thenComparing(Apple::getCountry)); //
			System.out.println(inventory);

			Collections.shuffle(inventory);
			inventory.sort(comparing(Apple::getWeight) //
					.thenComparing(Apple::getCountry) //
					.reversed()); //
			System.out.println(inventory);
		}

		public void testPredicate() {
			Predicate<Apple> redApple = a -> "red".equals(a.getColor());
			System.out.println("redApple: " + filter(inventory, redApple));

			Predicate<Apple> notRedApple = redApple.negate();
			System.out.println("notRedApple: " + filter(inventory, notRedApple));

			Predicate<Apple> redAndHeavyApple = redApple.and(a -> a.getWeight() > 150);
			System.out.println("redAndHeavyApple: " + filter(inventory, redAndHeavyApple));

			Predicate<Apple> redAndHeavyAppleOrGreen = redApple.and(a -> a.getWeight() > 150)
					.or(a -> "green".equals(a.getColor()));
			System.out.println("redAndHeavyAppleOrGreen: " + filter(inventory, redAndHeavyAppleOrGreen));
		}

		public void testFunction() {
			Function<Integer, Integer> f = x -> x + 1;
			Function<Integer, Integer> g = x -> x * 2;
			Function<Integer, Integer> h = f.andThen(g);
			System.out.println("g(f(x)): " + map(Arrays.asList(1, 2, 3), h));
			h = f.compose(g);
			System.out.println("f(g(x)): " + map(Arrays.asList(1, 2, 3), h));

			String content = "hello labda ok";
			Function<String, String> addHeader = Letter::addHeader;
			Function<String, String> transformationPipeline = addHeader.andThen(Letter::checkSpelling)
					.andThen(Letter::addFooter);
			System.out.println("transformationPipeline: " + transformationPipeline.apply(content));

			Function<String, String> addHeader2 = Letter::addHeader;
			Function<String, String> transformationPipeline2 = addHeader2.andThen(Letter::addFooter);
			System.out.println("transformationPipeline2: " + transformationPipeline2.apply(content));
		}

		public static class Letter {
			public static String addHeader(String text) {
				return "From Raoul, Mario and Alan: " + text;
			}

			public static String addFooter(String text) {
				return text + " Kind regards";
			}

			public static String checkSpelling(String text) {
				return text.replaceAll("labda", "lambda");
			}
		}

	}

	@Test(9)
	public static class SimilarIdeasFromMathematics {

		public void testIntegrate() {
			double r = integrate((double x) -> x + 10, 3, 7);
			System.out.println("1/2 × ((3 + 10) + (7 + 10)) × (7 – 3) = " + r);
		}

		public double integrate(DoubleFunction<Double> f, double a, double b) {
			return (f.apply(a) + f.apply(b)) * (b - a) / 2.0;
		}

	}

}
