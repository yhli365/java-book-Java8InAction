package lambdasinaction.chap2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilteringApples {

	public static void main(String... args) {

		List<Apple> inventory = Arrays.asList(new Apple(80, "green"), new Apple(155, "green"), new Apple(120, "red"));

		System.out.println("初试牛刀：筛选绿苹果");

		// [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
		List<Apple> greenApples1 = filterGreenApples(inventory);
		System.out.println(greenApples1);

		System.out.println("再展身手：把颜色作为参数");

		// [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
		List<Apple> greenApples = filterApplesByColor(inventory, "green");
		System.out.println(greenApples);

		// [Apple{color='red', weight=120}]
		List<Apple> redApples = filterApplesByColor(inventory, "red");
		System.out.println(redApples);

		System.out.println("第三次尝试：对你能想到的每个属性做筛选");

		// [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
		List<Apple> greenApples3 = filterApples(inventory, "green", 0, true);
		System.out.println(greenApples3);

		// [Apple{color='green', weight=155}]
		List<Apple> heavyApples3 = filterApples(inventory, "", 150, false);
		System.out.println(heavyApples3);

		System.out.println("第四次尝试：根据抽象条件筛选");

		// [Apple{color='green', weight=80}, Apple{color='green', weight=155}]
		List<Apple> greenApples2 = filter(inventory, new AppleColorPredicate());
		System.out.println(greenApples2);

		// [Apple{color='green', weight=155}]
		List<Apple> heavyApples = filter(inventory, new AppleWeightPredicate());
		System.out.println(heavyApples);

		// []
		List<Apple> redAndHeavyApples = filter(inventory, new AppleRedAndHeavyPredicate());
		System.out.println(redAndHeavyApples);

		System.out.println("第五次尝试：使用匿名类");

		// [Apple{color='red', weight=120}]
		List<Apple> redApples2 = filter(inventory, new ApplePredicate() {
			public boolean test(Apple a) {
				return a.getColor().equals("red");
			}
		});
		System.out.println(redApples2);

		System.out.println("第六次尝试：使用Lambda 表达式");

		// [Apple{color='red', weight=120}]
		List<Apple> result = filter(inventory, (Apple apple) -> "red".equals(apple.getColor()));
		System.out.println(result);

		System.out.println("第七次尝试：将List 类型抽象化");

		// [Apple{color='red', weight=120}]
		List<Apple> redApples7 = filter(inventory, (Apple apple) -> "red".equals(apple.getColor()));
		System.out.println(redApples7);

		// [2, 4]
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> evenNumbers7 = filter(numbers, (Integer i) -> i % 2 == 0);
		System.out.println(evenNumbers7);
	}

	public static List<Apple> filterGreenApples(List<Apple> inventory) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if ("green".equals(apple.getColor())) {
				result.add(apple);
			}
		}
		return result;
	}

	public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if (apple.getColor().equals(color)) {
				result.add(apple);
			}
		}
		return result;
	}

	public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if (apple.getWeight() > weight) {
				result.add(apple);
			}
		}
		return result;
	}

	public static List<Apple> filterApples(List<Apple> inventory, String color, int weight, boolean flag) {
		List<Apple> result = new ArrayList<Apple>();
		for (Apple apple : inventory) {
			if ((flag && apple.getColor().equals(color)) || (!flag && apple.getWeight() > weight)) {
				result.add(apple);
			}
		}
		return result;
	}

	public static List<Apple> filter(List<Apple> inventory, ApplePredicate p) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : inventory) {
			if (p.test(apple)) {
				result.add(apple);
			}
		}
		return result;
	}

	public static class Apple {
		private int weight = 0;
		private String color = "";

		public Apple(int weight, String color) {
			this.weight = weight;
			this.color = color;
		}

		public Integer getWeight() {
			return weight;
		}

		public void setWeight(Integer weight) {
			this.weight = weight;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String toString() {
			return "Apple{" + "color='" + color + '\'' + ", weight=" + weight + '}';
		}
	}

	interface ApplePredicate {
		public boolean test(Apple a);
	}

	static class AppleWeightPredicate implements ApplePredicate {
		public boolean test(Apple apple) {
			return apple.getWeight() > 150;
		}
	}

	static class AppleColorPredicate implements ApplePredicate {
		public boolean test(Apple apple) {
			return "green".equals(apple.getColor());
		}
	}

	static class AppleRedAndHeavyPredicate implements ApplePredicate {
		public boolean test(Apple apple) {
			return "red".equals(apple.getColor()) && apple.getWeight() > 150;
		}
	}

	public interface MyPredicate<T> {
		boolean test(T t);
	}

	public static <T> List<T> filter(List<T> list, MyPredicate<T> p) {
		List<T> result = new ArrayList<>();
		for (T e : list) {
			if (p.test(e)) {
				result.add(e);
			}
		}
		return result;
	}
}