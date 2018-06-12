package org.laotse.github.java8.feature.method;

import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * ��������
 * 
 * @author ZhongWen
 *
 */
public class Main {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ITransfer<Integer, String> valueOf = (s) -> String.valueOf(s);
		System.out.println(valueOf.transfer(Integer.MAX_VALUE));
		
		// ���ù��췽��
		// �����Ժ����ͽӿ�
		Supplier<Student> newObj = Student :: new;
		System.out.println(newObj.get());
		
		// �����ͺ���ʽ�ӿ�
		Predicate<Integer> isZero = (i) -> i == 0;
		System.out.println(isZero.test(0));
		
		Predicate<String> isEmpty = String :: isEmpty;
		System.out.println(isEmpty.test(""));
		
		// �����ͺ���ʽ�ӿ�
		Consumer<Student> print = (s) -> System.out.println(s);
		print.accept(new Student());
		
		// �����ͺ���ʽ�ӿڣ�XxxFunction��
		IntFunction<String> fun = (i) -> "Process " + i;
		System.out.println(fun.apply(11));
		
	}
	
}

@FunctionalInterface
interface ITransfer<S, T> {
	
	T transfer(S source);
	
}

class Student {
	
	private String name;
	
	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
