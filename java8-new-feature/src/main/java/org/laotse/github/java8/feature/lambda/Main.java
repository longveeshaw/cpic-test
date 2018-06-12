package org.laotse.github.java8.feature.lambda;

/**
 * ����ʽ���
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
		
		// ��д���������Ϳ��Բ������������������ֻ��һ��������ʡ�� '{}'��
		// ���ֻ��һ���������Ҫ���أ���returnҲ����ʡ��
		IService service2 = (arg) -> arg.toUpperCase();
		System.out.println(service2.call("Lambda"));
	}

}

/**
 * @FunctionalInterface Լ���ӿ�ֻ�ܶ���һ��������Ϊ�˿���ʹ��Lambda���ʽ
 *
 */
@FunctionalInterface
interface IService {
	
	String call(String arg);
	
}

