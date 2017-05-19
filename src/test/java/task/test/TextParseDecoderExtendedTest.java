package task.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import lombok.val;

public class TextParseDecoderExtendedTest {

  private final TextParseDecoder obj = new TextParseDecoder();

  @Test
  public void test_case_1() {

    //given
    val input = "decode(fun(decode(test(), 1,2,  3,4,  def)), 42, X, 99, Y, 13, Z, DEF)";
    val output = "if(eq(fun(if(eq(test(),1),2,if(eq(test(),3),4,def))),42),X,if(eq(fun(if(eq(test(),1),2,if(eq(test(),3),4,def))),99),Y,if(eq(fun(if(eq(test(),1),2,if(eq(test(),3),4,def))),13),Z,DEF)))";

    //when
    val actual = this.obj.decode(input);

    //then
    assertEquals(output, actual);
  }

  @Test
  public void test_case_2() {

    //given
    val input = "decode(FOO(), 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, BAR())";
    val output = "if(eq(FOO(),1),2,if(eq(FOO(),3),4,if(eq(FOO(),5),6,if(eq(FOO(),7),8,if(eq(FOO(),9),10,BAR())))))";

    //when
    val actual = this.obj.decode(input);

    //then
    assertEquals(output, actual);
  }

  @Test
  public void test_case_3() {

    //given
    val input = "fun(1,2, test)";
    val output = "fun(1,2, test)";

    //when
    val actual = this.obj.decode(input);

    //then
    assertEquals(output, actual);
  }

  @Test
  public void test_case_4() {

    //given
    val input = "test_id";
    val output = "test_id";

    //when
    val actual = this.obj.decode(input);

    //then
    assertEquals(output, actual);
  }

  @Test
  public void test_case_5 () {

    //given
    val input = "001";
    val output = "001";

    //when
    val actual = this.obj.decode(input);

    //then
    assertEquals(output, actual);
  }
}
