import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cn.org.assembler.分析器类;
import cn.org.assembler.指令类;
import cn.org.assembler.汇编器类;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据类;


public class 汇编器测试类 {

  @Test
  public void 指令汇编() {
    指令类 指令 = 分析器类.分析代码行("mov rax, 0x1122334455667788");
    List<String> 二进制码 = 汇编器类.指令汇编(指令);
    assertEquals(Arrays.asList("48", "b8", "88", "77", "66", "55", "44", "33", "22", "11"), 二进制码);
  }

  @Test
  public void 查找操作码() {
    List<操作码元数据类> 操作码元数据 = 汇编器类.查找操作码("MOV", 操作数元数据类.寄存器64, 操作数元数据类.立即数64);
    assertTrue(!操作码元数据.isEmpty());
  }
  
  @Test
  public void 生成二进制码() {
    assertEquals(Arrays.asList("88", "77", "66", "55", "44", "33", "22", "11"), 汇编器类.生成二进制码("0x1122334455667788"));
    assertEquals(Arrays.asList("21", "10"), 汇编器类.生成二进制码("0x112"));
    assertEquals(Arrays.asList("00", "01"), 汇编器类.生成二进制码("0x1000"));
  }
}
