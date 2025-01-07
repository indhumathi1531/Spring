package com.test;

public class StringBufferVSStringBuilder {
	public static void main(String[] args) throws InterruptedException {

		int n = 10;

		// *************************String Builder Test*******************************//
		StringBuilder sb = new StringBuilder();
		StringBuilderTest[] builderThreads = new StringBuilderTest[n];
		for (int i = 0; i < n; i++) {
			builderThreads[i] = new StringBuilderTest(sb);
		}
		for (int i = 0; i < n; i++) {
			builderThreads[i].start();
		}
		for (int i = 0; i < n; i++) {
			builderThreads[i].join();
		}
		System.out.println("StringBuilderTest: Expected result is 1000; got " + sb.length());

		// *************************String Buffer Test*******************************//

		StringBuffer sb2 = new StringBuffer();
		StringBufferTest[] bufferThreads = new StringBufferTest[n];
		for (int i = 0; i < n; i++) {
			bufferThreads[i] = new StringBufferTest(sb2);
		}
		for (int i = 0; i < n; i++) {
			bufferThreads[i].start();
		}
		for (int i = 0; i < n; i++) {
			bufferThreads[i].join();
		}
		System.out.println("StringBufferTest: Expected result is 1000; got " + sb2.length());

	}

}

class StringBufferTest extends Thread {
	StringBuffer sb2;

	public StringBufferTest(StringBuffer sb2) {
		this.sb2 = sb2;
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			sb2.append("A");
		}

	}

}

class StringBuilderTest extends Thread {
	StringBuilder sb;

	public StringBuilderTest(StringBuilder sb) {
		this.sb = sb;
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			sb.append("A");
		}

	}

}
