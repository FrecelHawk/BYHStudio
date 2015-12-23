package com.xyt.ygcf.logic.my;

public class ConfusionAlgorithm {

	public static String getConfusionText(String inputText) {
		StringBuffer sb = new StringBuffer(inputText.length());
		final int lenght = inputText.length();
		for (int i = 0; i < lenght; i++) {
			sb.append((char) (inputText.charAt(i) + i + 1));
		}
		return sb.toString();
	}

	public static String getRecoveryText(String inputText) {
		StringBuffer sb = new StringBuffer(inputText.length());
		final int lenght = inputText.length();
		for (int i = 0; i < lenght; i++) {
			sb.append((char) (inputText.charAt(i) - i - 1));
		}
		return sb.toString();
	}
}
