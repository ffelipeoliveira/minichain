package other;

import io.Simple;

public class Calculator {
	private String numbers = "";
	private String operators = "";
	private char[] operatorsNotUsed;

	// Iterates through an array searching for the desired index

	private int index(char characters[], char desiredChar) {
		int counter = 0;
		for (char character : characters) {
			if (desiredChar == character) {
				return counter;
			}
			counter++;
		}
		return -1;
	}

	// Expressions are added to a String and divided by whitespaces on
	// operators. Afterwards the split method make an array a with this String

	private void reset() {
		numbers = "";
		operators = "";
		operatorsNotUsed = null;
	}

	private String[] buildCalc(String expression) {
		for (int i = 0; i < expression.length(); i++) {
			char character = expression.charAt(i);
			if (character == ',') {
				numbers += '.';
			} else if (index("+-*/^".toCharArray(), character) != -1) {
				numbers += ' ';
				operators += character;
			}
			// The only operator which does not divide number is the SquareRoot
			// because it works with just one number
			else if (character == 'V') {
				operators += character;
			} else if (index("0123456789".toCharArray(), character) != -1) {
				numbers += character;
			}
		}
		return numbers.split(" ");
	}

	// The number begins after the index to check if there's more valid
	// operands. If there is one, it will be put in the place of the current one

	private void dequeueOperand(String[] operands, int index) {
		int counter = index;
		for (int i = index; i < operands.length - 1; i++) {
			if (operands[i + 1] != null) {
				operands[i] = operands[i + 1];
				counter++;
			}
		}
		operands[counter] = null;
	}

	private void dequeueOperator(int index) {
		int counter = index;
		for (int i = index; i < operatorsNotUsed.length - 1; i++) {
			if (operatorsNotUsed[i + 1] != ' ') {
				operatorsNotUsed[i] = operatorsNotUsed[i + 1];
				counter++;
			}
		}
		operatorsNotUsed[counter] = ' ';
	}

	// The method will build the expression and and operator array.
	// Afterwards it will search for the index of each operator to see which one
	// will come first.

	private double calc(String expression) throws Exception {
		reset();
		String operands[] = buildCalc(expression);
		operatorsNotUsed = operators.toCharArray();
		int queuedOperations = operators.length();

		while (queuedOperations > 0) {
			int indexExponentiation = index(operatorsNotUsed, '^');
			int indexSquareRoot = index(operatorsNotUsed, 'V');
			int indexMultiplication = index(operatorsNotUsed, '*');
			int indexDivision = index(operatorsNotUsed, '/');
			int indexAddition = index(operatorsNotUsed, '+');
			int indexSubtraction = index(operatorsNotUsed, '-');

			if (indexExponentiation != -1 && (indexExponentiation < indexSquareRoot || indexSquareRoot == -1)) {
				operands[indexExponentiation] = "" + (Math.pow(Double.parseDouble(operands[indexExponentiation]),
						Double.parseDouble(operands[indexExponentiation + 1])));
				dequeueOperand(operands, indexExponentiation + 1);
				queuedOperations--;
				dequeueOperator(indexExponentiation);

			} else if (indexSquareRoot != -1) {
				operands[indexSquareRoot] = "" + (Math.sqrt(Double.parseDouble(operands[indexSquareRoot])));
				queuedOperations--;
				dequeueOperator(indexSquareRoot);

			} else if (indexMultiplication != -1 && (indexMultiplication < indexDivision || indexDivision == -1)) {
				operands[indexMultiplication] = "" + (Double.parseDouble(operands[indexMultiplication])
						* Double.parseDouble(operands[indexMultiplication + 1]));
				dequeueOperand(operands, indexMultiplication + 1);
				queuedOperations--;
				dequeueOperator(indexMultiplication);
			} else if (indexDivision != -1) {
				if (Double.parseDouble(operands[indexDivision + 1]) == 0) {
					throw new ArithmeticException("Cannot divide by zero");
				} else {
					operands[indexDivision] = "" + (Double.parseDouble(operands[indexDivision])
							/ Double.parseDouble(operands[indexDivision + 1]));
				}
				dequeueOperand(operands, indexDivision + 1);
				queuedOperations--;
				dequeueOperator(indexDivision);

			} else if (indexAddition != -1 && (indexAddition < indexSubtraction || indexSubtraction == -1)) {
				operands[indexAddition] = ""
						+ (Double.parseDouble(operands[indexAddition])
								+ Double.parseDouble(operands[indexAddition + 1]));
				dequeueOperand(operands, indexAddition + 1);
				queuedOperations--;
				dequeueOperator(indexAddition);
			} else if (indexSubtraction != -1) {
				operands[indexSubtraction] = "" + (Double.parseDouble(operands[indexSubtraction])
						- Double.parseDouble(operands[indexSubtraction + 1]));
				dequeueOperand(operands, indexSubtraction + 1);
				queuedOperations--;
				dequeueOperator(indexSubtraction);
			}
		}

		return Double.parseDouble(operands[0]);
	}

	// The replaceAll will run through the String and replace ( to (f
	// f flags which operands had parenthesis
	// The Split method divide the String where there's (), removing all
	// parenthesis in the process

	public double calcExpression(String expression) throws Exception {
		String operands[] = expression.replaceAll("\\(", "\\(f").split("([()])");
		expression = "";

		for (int i = 0; i < operands.length; i++) {

			if (!operands[i].isEmpty()) {
				if (operands[i].charAt(0) == 'f') {
					operands[i] = "" + calc(operands[i]) / 10;
				}

				expression += operands[i];
			}
		}
		return calc(expression);
	}

	public double calcUsingPreviousResult(String expression, double result) {
		try {
			return calcExpression(expression.replaceAll("r", "" + result / 10));
		} catch (Exception e) {
			System.err.println("[X] " + e.getMessage());
			Simple.pause();
			return result;
		}
	}
}
