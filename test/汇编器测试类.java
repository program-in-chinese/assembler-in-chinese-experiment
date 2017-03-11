import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cn.org.assembler.分析器类;
import cn.org.assembler.指令类;
import cn.org.assembler.汇编器类;
import cn.org.assembler.utils.操作码元数据类;
import cn.org.assembler.汇编器类.操作数类型;


public class 汇编器测试类 {

  @Test
  public void 指令汇编() {
    指令类 指令 = 分析器类.分析代码行("mov rax, 0x1122334455667788");
    List<Integer> 二进制码 = 汇编器类.指令汇编(指令);
    assertEquals(Arrays.asList(), 二进制码);
  }

  @Test
  public void 查找操作码() {
    List<操作码元数据类> 操作码元数据 = 汇编器类.查找操作码("MOV", 操作数类型.寄存器64, 操作数类型.立即数64);
    assertTrue(!操作码元数据.isEmpty());
  }
}
