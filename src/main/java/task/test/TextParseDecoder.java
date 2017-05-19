package task.test;

import java.util.LinkedList;

import lombok.val;

/**
 * Decoder that uses Strings and text parsing as a tool to replace all 'decode' functions.
 *
 * @author rroschin
 *
 */
public class TextParseDecoder implements Decoder {

  /** The key for 'decode' function */
  public static final String DECODE_KEY = "decode";

  /** The key for 'if' function */
  public static final String IF_KEY = "if";
  /** The key for 'eq' function */
  public static final String EQ_KEY = "eq";

  @Override
  public String decode(final String rawInput) {

    if (!isValidInput(rawInput)) { throw new IllegalArgumentException("Not a valid input."); }
    if (!rawInput.contains(DECODE_KEY)) { return rawInput; } //if no 'decode' function

    val input = rawInput.replaceAll("\\s+", ""); //ignore all whitespaces

    val output = new W<>(input);
    parseDecodeFunc(0, output);

    return output.t;
  }

  /**
   * Mock function to validate the given expression
   * @param rawInput the expression to validate
   * @return not implemented exceptiuon
   */
  private static boolean isValidInput(@SuppressWarnings("unused") final String rawInput) {

    return true; // TODO
  }

  /**
   * Recursive call to find all 'decode' functions and replace them with supported functions.
   * @param pos The position of current 'decode' substring
   * @param original The state of original and replaced string
   */
  private static void parseDecodeFunc(final int pos, final W<String> original) {

    val d = original.t.indexOf(DECODE_KEY, pos);
    if (d > -1) { //go next until the last one
      parseDecodeFunc(d + 1, original);
    }

    if (pos == 0) { return; } //all done
    val open = pos + DECODE_KEY.length();
    val close = indexOfMatchingParentheses(original.t, open);

    val cut = original.t.substring(open, close); //get the current 'decode' expression

    val replaced = replaceDecodeFunc(cut);
    original.t = original.t.replace(DECODE_KEY + '(' + cut + ')', replaced); //update the state
  }

  /**
   * Replaces the 'decode' function arguments with suppurted if() + eq() functions.
   * @param args The parameters of given 'decode' function.
   * @return Decoded function
   */
  private static String replaceDecodeFunc(final String args) { //1,4,func(),x

    val params = new LinkedList<>();
    final char[] arr = args.toCharArray();
    int pnum = 0;
    int prev = 0;
    for (int i = 0; i < arr.length; i++) {

      if (arr[i] == '(') { pnum++; continue; }
      if (arr[i] == ')') { pnum--; continue; }

      if (arr[i] == ',' && pnum == 0) {
        params.add(args.substring(prev, i));
        prev = i + 1;
      }
    }
    params.add(args.substring(prev));

    val numOfIfEq = (params.size() - 2) / 2;

    val replaced = new StringBuilder();
    int ip = 1;

    for (int i = 1; i <= numOfIfEq; i++) {

      replaced.append(IF_KEY).append("(");
      replaced.append(EQ_KEY).append("(");
      replaced.append(params.get(0)).append(",");

      replaced.append(params.get(ip)).append(")").append(",");
      replaced.append(params.get(ip + 1)).append(",");
      ip += 2;

      if (i == numOfIfEq) { //default argument
        replaced.append(params.get(params.size() - 1));
        for (int j = 1; j <= numOfIfEq; j++) {
          replaced.append(")");
        }
      }
    }

    return replaced.toString();
  }

  /**
   * Finds the matching parentheses in a given string (String should be valid).
   * @param string String to search for matching parentheses
   * @param open Index of opening parentheses
   * @return Index of closing parentheses
   */
  private static int indexOfMatchingParentheses(final String string, final int open) {

    final char[] text = string.toCharArray();

    int close = open;
    int i = 1;
    while (i > 0) {
      final char c = text[++close];
      if (c == '(') {
        i++;
      } else if (c == ')') {
        i--;
      }
    }
    return close;
  }

  /**
   * Wrapper class to hold the state for String in recursion calls.
   * @author rroschin
   */
  private static class W<T> {

    public T t;

    public W(final T t) {

      this.t = t;
    }

    @Override
    public String toString() {

      return this.t.toString();
    }

  }

}
