package lambdasinaction.chap9;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

import lambdasinaction.AppendTest;

public class Append extends AppendTest {

	public static void main(String... args) {
		test(Append.class);
	}

	@Test
	public void prepare() {
		List<Integer> numbers = Arrays.asList(3, 5, 1, 2, 6);
		numbers.sort(Comparator.naturalOrder());
		System.out.println("numbers: " + numbers);
	}

	@Test(3)
	public static class UsagePatternsForDefaultMethods {

		@Test(desc = "Optional methods")
		public void test1() {
			// Iterator
		}

		interface Iterator<T> {
			boolean hasNext();

			T next();

			default void remove() {
				throw new UnsupportedOperationException();
			}
		}

		@Test(desc = "Multiple inheritance of behavior")
		public void test2() {
			// ArrayList
		}

		public class ArrayList<E> extends AbstractList<E>
				implements List<E>, RandomAccess, Cloneable, Serializable, Iterable<E>, Collection<E> {

			private static final long serialVersionUID = 1L;

			@Override
			public E get(int index) {
				return null;
			}

			@Override
			public int size() {
				return 0;
			}
		}

	}

}
