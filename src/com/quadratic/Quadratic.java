package com.quadratic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Quadratic {
	
	private double a;
	private double b;
	private double c;
	private double negativeB;
	private SquareRoot discriminant;
	private double twoA;
	private static final String NO_REAL_SOLUTION = "no real solution";
	private boolean valid;
	
	public Quadratic() {
		clear();
	}
	
	public Quadratic(String equation) {
		setAbc(equation);
		negativeB = b * -1;
		discriminant = new SquareRoot(b * b - 4 * a * c);
		twoA = 2 * a;
		divideByGcf();
	}
	
	public double getA() {
		return a;
	}
	
	public double getB() {
		return b;
	}
	
	public double getC() {
		return c;
	}
	
	public String getNegativeB() {
		return Utilities.stringFormat(negativeB);
	}
	
	public SquareRoot getDiscriminant() {
		return discriminant;
	}
	
	public String getTwoA() {
		return Utilities.stringFormat(twoA);
	}
	
	public String getFirstX() {
		double x = (negativeB + discriminant.getExactValue()) / twoA;
		return Double.isNaN(x) ? NO_REAL_SOLUTION : Utilities.stringFormat(x);
	}
	
	public String getSecondX() {
		double x = (negativeB - discriminant.getExactValue()) / twoA;
		return Double.isNaN(x) ? NO_REAL_SOLUTION : Utilities.stringFormat(x);
	}
	
	public double getY(double x) {
		return a * x * x + b * x + c;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	private void clear() {
		a = 0;
		b = 0;
		c = 0;
		valid = false;
	}
	
	private void setAbc(String equation) {
		valid = true;
		if (!equation.contains(Term.X_SQUARED) || equation.split("=").length > 2 || equation.trim().startsWith("=") || equation.trim().endsWith("=")) {
			clear();
			return;
		}
		
		List<Term> terms = getTerms(equation);
		
		for (int i = 0; i < terms.size(); i++) {
			double number = terms.get(i).getNumber();
			String variable = terms.get(i).getVariable();
			if (variable.equals(Term.X_SQUARED)) {
				a += number;
			} else if (variable.equals(Term.X)) {
				b += number;
			} else {
				c += number;
			}
		}
	}
	
	private void divideByGcf() {
		int gcf = 1;
		double[] nums = new double[] {Math.abs(negativeB), Math.abs(discriminant.getLeftNumber()), Math.abs(twoA)};
		nums = sortWithoutZeros(nums);
		
		for (int i = (int) nums[0]; i > 0; i--) {
			int j;
			for (j = 0; j < nums.length; j++) {
				if (nums[j] % i != 0) {
					break;
				}
			}
			if (j == nums.length) {
				gcf = i;
				break;
			}
		}
		
		negativeB /= gcf;
		discriminant.divideByGcf(gcf);
		twoA /= gcf;
	}
	
	private double[] sortWithoutZeros(double[] nums) {
		Arrays.sort(nums);
		
		while (nums.length > 0) {
			if (nums[0] == 0) {
				nums = Arrays.copyOfRange(nums, 1, nums.length);
			} else {
				break;
			}
		}
		
		return nums;
	}
	
	private List<Term> getTerms(String equation) {
		String[] rawTerms = equation.replace(" ", "").replace("=", " ").replace("+", " +").replace("-", " -").split(" ");
		int leftTermCount = equation.split("=")[0].replace(" ", "").replace("+", " +").replace("-", " -").split(" ").length;
		List<Term> terms = new ArrayList<Term>();
		
		for (int i = 0; i < rawTerms.length; i++) {
			if (!rawTerms[i].isBlank()) {
				Term term = new Term(rawTerms[i]);
				if (term.isValid()) {
					if (i >= leftTermCount) {
						term.flipNumber();
					}
					terms.add(term);
				} else {
					clear();
					terms.clear();
					break;
				}
			}
		}
		
		return terms;
	}
	
}