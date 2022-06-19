package com.quadratic;

public class Term {
	
	public static final String X_SQUARED = "x^2";
	public static final String X = "x";
	public static final String NO_VARIABLE = "";
	private String variable;
	private double number;
	private boolean valid;
	
	public Term(String term) {
		
		if (term.endsWith(X_SQUARED)) {
			variable = X_SQUARED;
		} else if (term.endsWith(X)) {
			variable = X;
		} else {
			variable = NO_VARIABLE;
		}
		
		String removeVariable = term.replace(variable, "");
		
		try {
			valid = true;
			if (variable.equals(X_SQUARED) || variable.equals(X)) {
				if (duplicateVariable(term)) {
					throw new IllegalArgumentException();
				} else if (removeVariable.equals("+") || removeVariable.isBlank()) {
					removeVariable = String.valueOf(1);
				} else if (removeVariable.equals("-")) {
					removeVariable = String.valueOf(-1);
				}
			}
			number = Double.parseDouble(removeVariable);
		} catch (Exception e) {
			number = 0;
			valid = false;
		}
		
	}
	
	public String getVariable() {
		return variable;
	}
	
	public double getNumber() {
		return number;
	}
	
	public void flipNumber() {
		number *= -1;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	private boolean duplicateVariable(String term) {
		return term.replace(variable, " " + variable).split(" ").length > 2;
	}
	
}