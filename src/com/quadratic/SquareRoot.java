package com.quadratic;

public class SquareRoot {

	private double leftNumber;
	private double rightNumber;
	private double exactValue;
	private boolean radicalVisible;
	
	public SquareRoot(double number) {
		exactValue = Math.sqrt(number);
		simplifySqrt(number);
	}
	
	private void simplifySqrt(double number) {
		leftNumber = 1;
		rightNumber = number;
		radicalVisible = true;
		
		if (exactValue % 1 == 0) {
			rightNumber = exactValue;
			radicalVisible = false;
		} else {
			if (Utilities.inBounds(number)) {
				for (int i = (int) Math.sqrt(number / 2); i >= 2; i--) {
					if (number % (i * i) == 0) {
						leftNumber = i;
						rightNumber = number / (i * i);
						return;
					}
				}
			}
		}
	}
	
	public String getSquareRoot() {
		if (radicalVisible) {
			return (leftNumber == 1 ? "" : Utilities.stringFormat(leftNumber)) + "√" + Utilities.stringFormat(rightNumber);
		} else {
			return Utilities.stringFormat(rightNumber);
		}
	}
	
	public double getExactValue() {
		return exactValue;
	}
	
	public double getLeftNumber() {
		return leftNumber;
	}
	
	public double getRightNumber() {
		return rightNumber;
	}
	
	public void divideByGcf(int gcf) {
		leftNumber /= gcf;
		exactValue /= gcf;
	}
	
}