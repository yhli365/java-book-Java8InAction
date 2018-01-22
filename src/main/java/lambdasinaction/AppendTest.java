package lambdasinaction;

import static java.util.Comparator.comparing;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class AppendTest {

	private static boolean titleFirst;

	public final static void test(Class<?> clazz) {
		System.out.println(clazz.getName());
		List<Object[]> tests = new ArrayList<>();
		Class<?> innerClazz[] = clazz.getDeclaredClasses();
		for (Class<?> cls : innerClazz) {
			Test at = cls.getAnnotation(Test.class);
			if (at == null) {
				continue;
			}
			tests.add(new Object[] { cls, at.value() });
		}
		tests.sort(comparing(a -> (int) a[1]));
		for (Object[] t : tests) {
			Class<?> cls = (Class<?>) t[0];
			System.out.println("\n\033[31m=====" + cls.getSimpleName() + "=====\033[0m");
			// Method method = cls.getMethod("test");
			Object obj = null;
			boolean first = true;
			for (Method method : cls.getMethods()) {
				if (!method.getName().startsWith("test") || method.getParameterTypes().length != 0) {
					continue;
				}
				try {
					Test at = method.getAnnotation(Test.class);
					String desc = (at == null || at.desc().trim().length() == 0) ? method.getName()
							: method.getName() + ":" + at.desc().trim();
					if (first) {
						System.out.println("\033[32m-----[" + desc + "]----------\033[0m");
					} else {
						System.out.println("\n\033[32m-----[" + desc + "]----------\033[0m");
					}
					titleFirst = true;
					int modifiers = method.getModifiers();
					if (Modifier.isStatic(modifiers)) {
						method.invoke(null);
					} else {
						if (obj == null) {
							obj = cls.newInstance();
						}
						method.invoke(obj);
					}
					first = false;
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}
			}
		}
	}

	public final static void title(String title) {
		if (titleFirst) {
			System.out.println("\033[36m--(" + title + ")-----\033[0m");
			titleFirst = false;
		} else {
			System.out.println("\n\033[36m--(" + title + ")-----\033[0m");
		}
	}

	public static Reader getReader(String fileName) {
		// return new FileReader(fileName);
		if (!fileName.startsWith("/")) {
			fileName = "/" + fileName;
		}
		return new InputStreamReader(AppendTest.class.getResourceAsStream(fileName));
	}

	public static Path getPath(String fileName) {
		Path p = Paths.get("lambdasinaction/chap5/data.txt");
		if (p.toFile().exists()) {
			return p;
		} else {
			File f = new File(fileName);
			if (!f.exists()) {
				f = new File("src/main/resources/" + fileName);
			}
			// System.out.println("Paths.get: " + f.getAbsolutePath());
			p = f.toPath();
		}
		return p;
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

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Documented
	public static @interface Test {
		int value() default 0; // order value

		String desc() default "";
	}

	public static List<Apple> inventory = new ArrayList<Apple>();

	static {
		inventory.add(new Apple(80, "green", "US"));
		inventory.add(new Apple(155, "green", "China"));
		inventory.add(new Apple(190, "red", "US"));
		inventory.add(new Apple(80, "red", "China"));
	}

	public static class Apple {
		private int weight = 0;
		private String color = "";
		private String country;

		public Apple(int weight, String color, String country) {
			this.weight = weight;
			this.color = color;
			this.country = country;
		}

		public Apple(int weight, String color) {
			this(weight, color, null);
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

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public String toString() {
			if (country == null) {
				return "Apple{" + "color='" + color + '\'' + ", weight=" + weight + '}';
			} else {
				return "Apple{'" + color + "', " + weight + ", " + country + '}';
			}
		}
	}

}
