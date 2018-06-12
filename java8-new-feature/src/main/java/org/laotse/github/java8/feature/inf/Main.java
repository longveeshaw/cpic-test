package org.laotse.github.java8.feature.inf;

import java.util.UUID;

/**
 * 接口增强功能
 * 
 * @author ZhongWen
 *
 */
public class Main {

	public static void main(String[] args) {
		Parent obj = new Child();
		System.out.println(obj.getName());
		System.out.println(Parent.newId());
		
		System.out.println(Parent.singleton.getId());
	}

}

interface Parent {
	
	static Parent singleton = new Child();

	String getName();

	default String getId() {
		return UUID.randomUUID().toString();
	}
	
	static String newId() {
		return UUID.randomUUID().toString();
	}

}

class Child implements Parent {

	@Override
	public String getName() {
		return "Child";
	}

}
