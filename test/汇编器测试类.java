import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cn.org.assembler.分析器类;
import cn.org.assembler.汇编器类;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据类;


public class 汇编器测试类 {

  @Test
  public void 指令汇编() {
    验证二进制码("mov rax, 0x1122334455667788",
        Arrays.asList("48", "b8", "88", "77", "66", "55", "44", "33", "22", "11"));
    验证二进制码("mov rax, 0x1000", Arrays.asList("48", "c7", "c0", "00", "10", "00", "00"));
  }

  @Test
  public void 查找操作码() {
    验证操作码("b8", "MOV", 操作数元数据类.寄存器64, 操作数元数据类.立即数64);
    验证操作码("c7", "MOV", 操作数元数据类.寄存器64, 操作数元数据类.立即数32);
  }

  @Test
  public void 生成二进制码() {
    assertEquals(Arrays.asList("88", "77", "66", "55", "44", "33", "22", "11"),
        汇编器类.生成二进制码("0x1122334455667788", 64));
    assertEquals(Arrays.asList("12", "01", "00", "00"), 汇编器类.生成二进制码("0x112", 32));
    assertEquals(Arrays.asList("00", "10", "00", "00"), 汇编器类.生成二进制码("0x1000", 32));
  }

  private void 验证二进制码(String 代码行, List<String> 二进制码) {
    assertEquals(二进制码, 汇编器类.指令汇编(分析器类.分析代码行(代码行)));
  }

  private void 验证操作码(String 操作码值, String 助记符名, 操作数元数据类 操作数1元数据, 操作数元数据类 操作数2元数据) {
    List<操作码元数据类> 操作码元数据 = 汇编器类.查找操作码(助记符名, 操作数1元数据, 操作数2元数据);
    assertEquals(1, 操作码元数据.size());
    assertEquals(操作码值, Integer.toHexString(操作码元数据.get(0).值));
  }
}
