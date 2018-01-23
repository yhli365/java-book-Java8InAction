package lambdasinaction.chap6;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static lambdasinaction.chap6.Dish.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lambdasinaction.AppendTest;
import lambdasinaction.chap6.Grouping.CaloricLevel;

public class Append extends AppendTest {

	public static void main(String... args) {
		test(Append.class);
	}

	@Test(2)
	public static class ReducingAndSummarizing {

		@Test(desc = "Finding maximum and minimum in a stream of values")
		public void test1() {
			Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(Comparator.comparingInt(Dish::getCalories)));
			System.out.println("mostCalorieDish: " + mostCalorieDish);
		}

		@Test(desc = "Generalized summarization with reduction")
		public void test4() {
			int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, (i, j) -> i + j));
			System.out.println("totalCalories: " + totalCalories);

			Optional<Dish> mostCalorieDish = menu.stream()
					.collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
			System.out.println("mostCalorieDish: " + mostCalorieDish);

			List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
			// 这个解决方案有两个问题：一个语义问题和一个实际问题。
			List<Integer> numbers = list.stream().reduce(new ArrayList<Integer>(), //
					(List<Integer> l, Integer e) -> {
						l.add(e);
						return l;
					}, //
					(List<Integer> l1, List<Integer> l2) -> {
						l1.addAll(l2);
						return l1;
					});
			System.out.println("numbers: " + numbers);
			List<Integer> numbers2 = list.stream().collect(toList());
			System.out.println("numbers2: " + numbers2);
		}

		@Test(desc = "Test")
		public void test9() {
			String shortMenu = menu.stream().map(Dish::getName).collect(joining());
			System.out.println("shortMenu: " + shortMenu);

			title("Q1");
			shortMenu = menu.stream().map(Dish::getName).collect(reducing((s1, s2) -> s1 + s2)).get();
			System.out.println("shortMenu: " + shortMenu);

			title("Q2");
			// shortMenu = menu.stream().collect(reducing((d1, d2) -> d1.getName() +
			// d2.getName())).get();

			title("Q3");
			shortMenu = menu.stream().collect(reducing("", Dish::getName, (s1, s2) -> s1 + s2));
			System.out.println("shortMenu: " + shortMenu);
		}
	}

	@Test(3)
	public static class Grouping {

		@Test(desc = "Collecting data in subgroups")
		public void test2() {
			Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream().collect( //
					groupingBy(Dish::getType, mapping(dish -> {
						if (dish.getCalories() <= 400)
							return CaloricLevel.DIET;
						else if (dish.getCalories() <= 700)
							return CaloricLevel.NORMAL;
						else
							return CaloricLevel.FAT;
					}, toCollection(HashSet::new))));
			System.out.println("caloricLevelsByType: " + caloricLevelsByType);
		}

	}

	@Test(4)
	public static class Partitioning {

		@Test(desc = "Test")
		public void test9() {
			title("Q1");
			Map<Boolean, Map<Boolean, List<Dish>>> result1 = menu.stream()
					.collect(partitioningBy(Dish::isVegetarian, partitioningBy(d -> d.getCalories() > 500)));
			System.out.println("result1: " + result1);

			title("Q2");
			// menu.stream().collect(partitioningBy(Dish::isVegetarian,
			// partitioningBy(Dish::getType)));

			title("Q3");
			Map<Boolean, Long> result3 = menu.stream().collect(partitioningBy(Dish::isVegetarian, counting()));
			System.out.println("result3: " + result3);
		}

	}

	@Test(5)
	public static class TheCollectorInterface {

		@Test(desc = "Putting them all together")
		public void test2() {
			List<Dish> dishes = menu.stream().collect(new ToListCollector<Dish>());
			System.out.println("dishes1: " + dishes);

			List<Dish> dishes2 = menu.stream().collect(toList());
			System.out.println("dishes2: " + dishes2);

			List<Dish> dishes3 = menu.stream().collect(ArrayList::new, List::add, List::addAll);
			System.out.println("dishes3: " + dishes3);
		}

	}

}
