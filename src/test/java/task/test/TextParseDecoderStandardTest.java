package task.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import lombok.val;

public class TextParseDecoderStandardTest {

  private final TextParseDecoder obj = new TextParseDecoder();

  @Test
  public void test_case_1() {

    //given
    val input = "fun(decode(test(), 1,2,  3,4,  def))";
    val output = "fun(if(eq(test(),1),2,if(eq(test(),3),4,def)))";

    //when
    val actual = this.obj.decode(input);

    //then
    assertEquals(output, actual);
  }

  @Test
  public void test_case_2() {

    //given
    val input = "decode(x, 1, decode(y, 1, 2, 0), 0)";
    val output = "if(eq(x,1),if(eq(y,1),2,0),0)";

    //when
    val actual = this.obj.decode(input);

    //then
    assertEquals(output, actual);
  }

}
