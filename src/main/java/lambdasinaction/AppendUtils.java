package lambdasinaction;

import static java.util.Comparator.comparing;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class AppendUtils {

	public final static void test(Class<?> clazz) {
		System.out.println(clazz.getName());
		List<Object[]> tests = new ArrayList<>();
		Class<?> innerClazz[] = clazz.getDeclaredClasses();
		for (Class<?> cls : innerClazz) {
			AppendTest at = cls.getAnnotation(AppendTest.class);
			if (at == null) {
				continue;
			}
			tests.add(new Object[] { cls, at.value() });
		}
		tests.sort(comparing(a -> (int) a[1]));
		for (Object[] t : tests) {
			Class<?> cls = (Class<?>) t[0];
			System.out.println("\n=====test:" + cls.getSimpleName() + "=====");
			// Method method = cls.getMethod("test");
			Object obj = null;
			for (Method method : cls.getMethods()) {
				if (!method.getName().startsWith("test") || method.getParameterTypes().length != 0) {
					continue;
				}
				try {
					if (!"test".equalsIgnoreCase(method.getName())) {
						title(method.getName());
					}
					int modifiers = method.getModifiers();
					if (Modifier.isStatic(modifiers)) {
						method.invoke(null);
					} else {
						if (obj == null) {
							obj = cls.newInstance();
						}
						method.invoke(obj);
					}
				} catch (Exception e) {
					e.printStackTrace(System.out);
				}
			}
		}
	}

	public final static void title(String title) {
		System.out.println("--[" + title + "]----------");
	}

	public static Reader getReader(String fileName) {
		// return new FileReader(fileName);
		if (!fileName.startsWith("/")) {
			fileName = "/" + fileName;
		}
		return new InputStreamReader(AppendUtils.class.getResourceAsStream(fileName));
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
	public static @interface AppendTest {
		int value() default 0; // order value
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
