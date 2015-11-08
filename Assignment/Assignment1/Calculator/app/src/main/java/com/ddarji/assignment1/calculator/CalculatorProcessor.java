package com.ddarji.assignment1.calculator;

public class CalculatorProcessor {

    // a + b = c
    // a & b are called the operand.
    // The + is called the operator.
    // c is the result of the operation.
    private double mOperand;
    private double mWaitingOperand;
    private String mWaitingOperator;
    private double mCalculatorMemory;

    // operator types
    public static final String ADD = "+";
    public static final String SUBTRACT = "−";
    public static final String MULTIPLY = "×";
    public static final String DIVIDE = "÷";

    public static final String CLEAR = "C" ;
    public static final String CLEARMEMORY = "MC";
    public static final String ADDTOMEMORY = "M+";
    public static final String SUBTRACTFROMMEMORY = "M-";
    public static final String RECALLMEMORY = "MR";
    public static final String SQUAREROOT = "√";
    public static final String SQUARED = "x²";
    public static final String INVERT = "1/x";
    public static final String TOGGLESIGN = "±";
    public static final String PERCENT = "%";
    public static final String SINE = "sin";
    public static final String COSINE = "cos";
    public static final String TANGENT = "tan";
    public static final String PI = "π";

    public static final String EQUALS = "=";

    // constructor
    public CalculatorProcessor() {
        // initialize variables upon start
        mOperand = 0;
        mWaitingOperand = 0;
        mWaitingOperator = "";
        mCalculatorMemory = 0;
    }

    /**
     * Set Operand
     * @param operand : Quantity on which an operation is to be done.
     */
    public void setOperand(double operand) {
        mOperand = operand;
    }

    /**
     * Get Result
     * @return mOperand
     */
    public double getResult() {
        return mOperand;
    }

    /**
     * Set Memory
     * @param calculatorMemory : The Calculator Memory
     */
    public void setMemory(double calculatorMemory) {
        mCalculatorMemory = calculatorMemory;
    }

    /**
     * Get Memory
     * @return mCalculatorMemory
     */
    public double getMemory() {
        return mCalculatorMemory;
    }

    /**
     * mOperand To String
     * @return String mOperand
     */
    public String toString() {
        return Double.toString(mOperand);
    }

    /**
     * Perform Operation
     * @param operator : The operation to be done.
     * @return mOperand
     */
    protected double performOperation(String operator) {

        switch (operator) {
            case CLEAR:
                mOperand = 0;
                mWaitingOperator = "";
                mWaitingOperand = 0;
                // mCalculatorMemory = 0;
                break;
            case CLEARMEMORY:
                mCalculatorMemory = 0;
                break;
            case ADDTOMEMORY:
                mCalculatorMemory = mCalculatorMemory + mOperand;
                break;
            case SUBTRACTFROMMEMORY:
                mCalculatorMemory = mCalculatorMemory - mOperand;
                break;
            case RECALLMEMORY:
                mOperand = mCalculatorMemory;
                break;
            case SQUAREROOT:
                mOperand = Math.sqrt(mOperand);
                break;
            case SQUARED:
                mOperand = mOperand * mOperand;
                break;
            case INVERT:
                if (mOperand != 0) {
                    mOperand = 1 / mOperand;
                }
                break;
            case TOGGLESIGN:
                mOperand = -mOperand;
                break;
            case PERCENT:
                mOperand = mOperand/100;
                break;
            case SINE:
                mOperand = Math.sin(Math.toRadians(mOperand)); // Math.toRadians(mOperand) converts result to degrees
                break;
            case COSINE:
                mOperand = Math.cos(Math.toRadians(mOperand)); // Math.toRadians(mOperand) converts result to degrees
                break;
            case TANGENT:
                mOperand = Math.tan(Math.toRadians(mOperand)); // Math.toRadians(mOperand) converts result to degrees
                break;
            case PI:
                mOperand = Math.PI;
                break;
            default:
                performWaitingOperation();
                mWaitingOperator = operator;
                mWaitingOperand = mOperand;
                break;
        }

//        if (operator.equals(CLEAR)) {
//            mOperand = 0;
//            mWaitingOperator = "";
//            mWaitingOperand = 0;
//            // mCalculatorMemory = 0;
//        } else if (operator.equals(CLEARMEMORY)) {
//            mCalculatorMemory = 0;
//        } else if (operator.equals(ADDTOMEMORY)) {
//            mCalculatorMemory = mCalculatorMemory + mOperand;
//        } else if (operator.equals(SUBTRACTFROMMEMORY)) {
//            mCalculatorMemory = mCalculatorMemory - mOperand;
//        } else if (operator.equals(RECALLMEMORY)) {
//            mOperand = mCalculatorMemory;
//        } else if (operator.equals(SQUAREROOT)) {
//            mOperand = Math.sqrt(mOperand);
//        } else if (operator.equals(SQUARED)) {
//            mOperand = mOperand * mOperand;
//        } else if (operator.equals(INVERT)) {
//            if (mOperand != 0) {
//                mOperand = 1 / mOperand;
//            }
//        } else if (operator.equals(TOGGLESIGN)) {
//            mOperand = -mOperand;
//        } else if (operator.equals(PERCENT)) {
//            mOperand = mOperand/100;
//        } else if (operator.equals(SINE)) {
//            mOperand = Math.sin(Math.toRadians(mOperand)); // Math.toRadians(mOperand) converts result to degrees
//        } else if (operator.equals(COSINE)) {
//            mOperand = Math.cos(Math.toRadians(mOperand)); // Math.toRadians(mOperand) converts result to degrees
//        } else if (operator.equals(TANGENT)) {
//            mOperand = Math.tan(Math.toRadians(mOperand)); // Math.toRadians(mOperand) converts result to degrees
//        } else if (operator.equals(PI)) {
//            mOperand = Math.PI;
//        } else {
//            performWaitingOperation();
//            mWaitingOperator = operator;
//            mWaitingOperand = mOperand;
//        }

        return mOperand;
    }

    /**
     * Perform Chained Waiting Operation
     */
    protected void performWaitingOperation() {

        switch (mWaitingOperator) {
            case ADD:
                mOperand = mWaitingOperand + mOperand;
                break;
            case SUBTRACT:
                mOperand = mWaitingOperand - mOperand;
                break;
            case MULTIPLY:
                mOperand = mWaitingOperand * mOperand;
                break;
            case DIVIDE:
                if (mOperand != 0) {
                    mOperand = mWaitingOperand / mOperand;
                }
                break;
        }

//        if (mWaitingOperator.equals(ADD)) {
//            mOperand = mWaitingOperand + mOperand;
//        } else if (mWaitingOperator.equals(SUBTRACT)) {
//            mOperand = mWaitingOperand - mOperand;
//        } else if (mWaitingOperator.equals(MULTIPLY)) {
//            mOperand = mWaitingOperand * mOperand;
//        } else if (mWaitingOperator.equals(DIVIDE)) {
//            if (mOperand != 0) {
//                mOperand = mWaitingOperand / mOperand;
//            }
//        }

    }
}
