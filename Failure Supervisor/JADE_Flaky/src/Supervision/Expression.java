package Supervision;

import java.io.*;

// Represents an arithmetic expression involving integer numbers
public interface Expression  {

    public Expression getLeft();

    public Expression getRight();



    public static Expression fromStringToExp (String task) {
      if(task.charAt(0) != '(')
        {
            return new Const(Integer.parseInt(task));
        }
          char op = task.charAt(1);

          int space = task.indexOf(' ');

          String length = task.substring(3, space);
          int L = Integer.parseInt(length.split(",")[0]);
          int R = Integer.parseInt(length.split(",")[1]);

          String LL = task.substring(space + 1, space + L + 1);
          String RR = task.substring(space + L + 2, space + L + 2 + R);

        //System.out.println("<" + LL + ">");
        //System.out.println("<" + RR + ">");

          switch (op) {

              case '/':
                  return new Divide(fromStringToExp(LL), fromStringToExp(RR));
              case '+':
                  return new Add(fromStringToExp(LL), fromStringToExp(RR));
              case '*':
                  return new Multiply(fromStringToExp(LL), fromStringToExp(RR));
              //case '-': return new Divide(fromStringToExp(LL),fromStringToExp(RR));
          }
      return null;
    }

    // Basic arithmetic operations that are supported by the ArithmeticService. Every
    // operation except the constant value has a left and right side. For example
    // the addition in (3 * 2) + (6 * 6) has the left side (3 * 2) and the right
    // side (6 * 6).
    public static abstract class AbstractExpression implements Expression {
        private final Expression left;
        private final Expression right;
        private final String operator;

        protected AbstractExpression(Expression left, Expression right, String operator) {
            this.left = left;
            this.right = right;
            this.operator = operator;
        }


        public Expression getLeft() {
            return left;
        }

        public Expression getRight() {
            return right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AbstractExpression)) return false;

            AbstractExpression that = (AbstractExpression) o;

            if (!left.equals(that.left)) return false;
            if (!operator.equals(that.operator)) return false;
            if (!right.equals(that.right)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = left.hashCode();
            result = 31 * result + right.hashCode();
            result = 31 * result + operator.hashCode();
            return result;
        }

        @Override
        public String toString() {
            String L = getLeft().toString();
            String R = getRight().toString();
            return "("+operator+","+L.length()+","+R.length()+" "+L+ " "+R+" )";
        }
    }

    public static final class Add extends AbstractExpression {

        public Add(Expression left, Expression right) {
            super(left, right, "+");
        }
    }

    public static final class Multiply extends AbstractExpression {
        public Multiply(Expression left, Expression right) {
            super(left, right, "*");
        }
    }

    public static final class Divide extends AbstractExpression {
        public Divide(Expression left, Expression right) {
            super(left, right, "/");
        }
    }


    public static final class Const implements Expression{
        private final int value;

        public Const(int value) {
            this.value = value;
        }

        @Override
        public Expression getLeft() {
            return this;
        }

        @Override
        public Expression getRight() {
            return this;
        }

        public int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Const)) return false;

            Const aConst = (Const) o;

            if (value != aConst.value) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}