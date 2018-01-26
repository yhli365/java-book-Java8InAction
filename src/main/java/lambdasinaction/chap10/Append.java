package lambdasinaction.chap10;

import java.util.Optional;

import lambdasinaction.AppendTest;

public class Append extends AppendTest {

	public static void main(String... args) {
		test(Append.class);
	}

	@Test(3)
	public static class PatternsForAdoptingOptional {

		@Test(desc = "Combining two optionals")
		public void test5() {
			// nullSafeFindCheapestInsurance
		}

		public Insurance findCheapestInsurance(Person person, Car car) {
			// 不同的保险公司提供的查询服务
			// 对比所有数据
			Insurance cheapestCompany = new Insurance();
			return cheapestCompany;
		}

		public Optional<Insurance> nullSafeFindCheapestInsurance1(Optional<Person> person, Optional<Car> car) {
			if (person.isPresent() && car.isPresent()) {
				return Optional.of(findCheapestInsurance(person.get(), car.get()));
			} else {
				return Optional.empty();
			}
		}

		public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car) {
			return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
		}

		@Test(desc = "Rejecting certain values with filter")
		public void test6() {
			Optional<Insurance> optInsurance = Optional.empty();
			optInsurance.filter(insurance -> "CambridgeInsurance".equals(insurance.getName()))
					.ifPresent(x -> System.out.println("ok"));
		}

	}

}
