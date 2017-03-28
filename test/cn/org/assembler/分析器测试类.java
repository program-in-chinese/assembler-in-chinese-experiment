package cn.org.assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据类;
import cn.org.assembler.模型.代码行类;
import cn.org.assembler.模型.指令类;


public class 分析器测试类 {

  @Test
  public void 查找操作码() {
    验证操作码("b8", 代码行类.分析("mov rax, 0x1122334455667788"));
    验证操作码("c7", 代码行类.分析("mov rax, 0x1000"));

    验证操作码("05", 代码行类.分析("add rax, 0x1000"));
    验证操作码("83", 代码行类.分析("add eax, 4"));
    验证操作码("25", 代码行类.分析("and eax, 3584"));
    验证操作码("05", 代码行类.分析("add ax,strict word 5"));
    验证操作码("b0", 代码行类.分析("mov al, 0"));
    验证操作码("b0", 代码行类.分析("mov byte al, 0"));
    验证操作码("90", 代码行类.分析("xchg ax, ax"));
    // TODO: 需要强制汇编?
    // 验证操作码("05", 代码行类.分析("add rax, 0x1122334455667788"));
  }

  @Test
  public void 匹配操作数类型() {
    assertTrue(分析器类.操作数类型匹配("rax", 操作数元数据类.寄存器64, new 操作数元数据类(false, "vqs", "rAX", null)));
    assertTrue(分析器类.操作数类型匹配("ax", 操作数元数据类.单字寄存器, new 操作数元数据类(false, "vqp", "rAX", null)));
  }

  private void 验证操作码(String 操作码值, 代码行类 代码行) {
    操作码元数据类 操作码元数据 = 代码行.查找操作码();
    assertNotNull(操作码元数据);
    assertEquals(操作码值, 指令类.生成操作码(操作码元数据.值));
  }
}
