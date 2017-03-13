import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cn.org.assembler.代码行类;
import cn.org.assembler.汇编器类;


public class 汇编器测试类 {

  @Test
  public void 指令汇编() {
    验证二进制码("mov rax, 0x1122334455667788",
        Arrays.asList("48", "b8", "88", "77", "66", "55", "44", "33", "22", "11"));
    验证二进制码("mov rax, 0x1000", Arrays.asList("48", "c7", "c0", "00", "10", "00", "00"));
  }

  private void 验证二进制码(String 代码行, List<String> 二进制码) {
    assertEquals(二进制码, 汇编器类.指令汇编(代码行类.分析(代码行)));
  }

}
