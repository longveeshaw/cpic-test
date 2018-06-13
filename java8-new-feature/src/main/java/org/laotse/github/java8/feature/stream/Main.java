package org.laotse.github.java8.feature.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) {
		
		List<Product> products = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			Product product = new Product();
			product.setId("100000" + i);
			product.setName(UUID.randomUUID().toString());
			product.setHeight(111.34f + i);
			product.setLength(231f + i);
			product.setPrice(12143214.99f + i * 100);
			product.setWeight(312f + 11.11f * i);
			product.setWidth(321.39f + 131.11f * i);
			product.setCategory(i % 2 == 0 ? "IT" : "Food");
			
			products.add(product);
		}
		
		products.forEach(product -> {
			System.out.println(product);
		});
		
		// https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/
		List<String> ids = products.parallelStream().filter(product -> "Food".equals(product.getCategory()))
				.map(Product :: getId).sorted((o1, o2) -> o2.hashCode() - o1.hashCode()).collect(Collectors.toList());
		System.out.println(ids);
		
		String[] ids2 = products.parallelStream().filter(product -> "Food".equals(product.getCategory()))
				.map(Product :: getId).sorted((o1, o2) -> o2.hashCode() - o1.hashCode()).toArray(String[] :: new);
		System.out.println(ids2);
		
		IntStream.range(1, 10).forEach(System.out :: println);
		LongStream.range(1, 10).forEach(e -> {
			System.out.println(e);
		});
		
		// Never to end
		// new Random().ints().filter(e -> e == Integer.MAX_VALUE).forEach(e -> {
		// System.out.println(e + ", Doing XXX ...");
		// });
		new Random().ints().limit(10).filter(e -> e > 0).forEach(System.out :: println);
		
		BitSet.valueOf(new long[] { 1, 3, 5, 7 }).stream().forEach(e -> System.out.println(e));
		
		Pattern.compile(" ").splitAsStream("java stream programming").forEach(e -> System.out.println(e));

		List<Integer> nums = Arrays.asList(1, 2, 3, 4);
		List<Integer> squareNums = nums.stream().map(n -> n * n).collect(Collectors.toList());
		System.out.println(squareNums);
		
		Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6));
		Stream<Integer> outputStream = inputStream.flatMap((childList) -> childList.stream());
		// flatMap 把 input Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，最终 output 的新 Stream 里面已经没有 List 了，都是直接的数字。
		outputStream.forEach(System.out :: println);
		
		// 过滤
		Integer[] evens = Stream.of(new Integer[] { 1, 2, 3, 4, 5 }).filter(e -> e % 2 == 0).toArray(Integer[] :: new);
		System.out.println(evens.length == 2);
		
		//String text = null;
		String text = "Optional.ofNullable";
		int length = Optional.ofNullable(text).map(String :: length).orElse(-1);
		System.out.println(length);
		
		System.out.println(Optional.of(text).get());
		
		
		Integer sum = Stream.of(1, 2, 3).reduce(0, (a, b) -> a + b);
		Integer sum2 = Stream.of(1, 2, 3).reduce(0, Integer::sum);
		System.out.println(sum + ", " + sum2);
		
		
		// 字符串连接，concat = "ABCD"
		String concat = Stream.of("A", "B", "C", "D").reduce("", String::concat);
		// 求最小值，minValue = -3.0
		double minValue = Stream.of(-1.5, 1.0, -3.0, -2.0).reduce(Double.MAX_VALUE, Double::min);
		// 求和，sumValue = 10, 有起始值
		int sumValue = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
		// 求和，sumValue = 10, 无起始值
		sumValue = Stream.of(1, 2, 3, 4).reduce(Integer::sum).get();
		// 过滤，字符串连接，concat = "ace"
		concat = Stream.of("a", "B", "c", "D", "e", "F").filter(x -> x.compareTo("Z") > 0).reduce("", String::concat);

	}

	public static class Product {
		
		private String id;
		
		private String name;
		
		private String category;
		
		private float price;
		
		private float width;
		
		private float height;
		
		private float length;
		
		private float weight;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public float getWidth() {
			return width;
		}

		public void setWidth(float width) {
			this.width = width;
		}

		public float getHeight() {
			return height;
		}

		public void setHeight(float height) {
			this.height = height;
		}

		public float getLength() {
			return length;
		}

		public void setLength(float length) {
			this.length = length;
		}

		public float getWeight() {
			return weight;
		}

		public void setWeight(float weight) {
			this.weight = weight;
		}

		@Override
		public String toString() {
			return "Product [id=" + id + ", name=" + name + ", category=" + category + ", price=" + price + ", width="
					+ width + ", height=" + height + ", length=" + length + ", weight=" + weight + "]";
		}
		
	}
	
}

