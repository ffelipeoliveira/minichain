package io.Controllers;

import other.Calculator;
import io.Simple;

public class Calculating {
	public static double calculate(String menuPath, double previousResult) {
        Simple.banner(menuPath);
        Calculator calculator = new Calculator();
		System.out.println("[!] Write an Expression\n[-] r: previous result ^: exponent, V: squareroot, /: division, *: multiplication, +: addition, -: subtraction");
		String input = Simple.strInput(20);
		
        try {
            double result = calculator.calcUsingPreviousResult(input, previousResult);
            Simple.banner(menuPath + "> Calculator");
            System.out.println(result);
            return result;
        } catch (Exception e) {
            Simple.banner(menuPath + "> Calculator (error)");
            System.out.println("[X] " + e.getMessage());
            return previousResult;
        }
	}
}
