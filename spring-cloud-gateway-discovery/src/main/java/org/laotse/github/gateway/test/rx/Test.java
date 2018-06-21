package org.laotse.github.gateway.test.rx;

public class Test {
	
	static interface IFoo {
		void foo();
	}
	
	static class Foo implements IFoo {
		@Override
		public void foo() {
			// TODO Auto-generated method stub
		}
	}
	
	public static void main(String[] args) {
	
		System.out.println(IFoo.class.isAssignableFrom(new Foo().getClass())); // true
		System.out.println(IFoo.class.isAssignableFrom(new Object().getClass())); // false
		System.out.println(IFoo.class.isAssignableFrom(new Test().getClass())); // false
		
	}

}
