package org.laotse.github.java8.feature.lambda;

/**
 * 函数式编程
 * 
 * @author ZhongWen
 *
 */
public class Main {
	
	public static void main(String[] args) {
		
		IService service = (String arg) -> {
			System.out.println("Call " + arg);
			return arg;
		};
		service.call("Java Lambda");
		
		// 简写，参数类型可以不用声明，如果代码体只有一个语句可以省略 '{}'，
		// 如果只有一条语句且需要返回，则return也可以省略
		IService service2 = (arg) -> arg.toUpperCase();
		System.out.println(service2.call("Lambda"));
	}

}

/**
 * @FunctionalInterface 约束接口只能定义一个方法，为了可以使用Lambda表达式
 *
 */
@FunctionalInterface
interface IService {
	
	String call(String arg);
	
}

