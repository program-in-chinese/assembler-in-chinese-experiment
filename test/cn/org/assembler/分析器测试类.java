package cn.org.assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据类;
import cn.org.assembler.模型.代码行类;
import cn.org.assembler.模型.代码行类.操作数类型;
import cn.org.assembler.模型.指令类;
import cn.org.assembler.模型.操作数信息类;


public class 分析器测试类 {

  @Test
  public void 查找操作码() {
    验证操作码("b8", "mov rax, 0x1122334455667788");
    验证操作码("c7", "mov rax, 0x1000");

    验证操作码("05", "add rax, 0x1000");
    验证操作码("83", "add eax, 4");
    验证操作码("25", "and eax, 3584");
    验证操作码("05", "add ax,strict word 5");
    验证操作码("b0", "mov al, 0");
    验证操作码("b0", "mov byte al, 0");
    验证操作码("90", "xchg ax, ax");
    验证操作码("c1", "shl rax, 5");
    验证操作码("02", "lar ax, bx");
    验证操作码("d0", "shl al, 1");
    验证操作码("c6", "mov byte [0], 0");
    验证操作码("c7", "mov [0], word 0");
    验证操作码("c7", "mov dword [0], dword 0");
    验证操作码("b8", "mov eax, 0");
    验证操作码("b8", "mov eax, dword 0");
    
    
    
    // TODO: 需要强制汇编?
    // 验证操作码("05", 代码行类.分析("add rax, 0x1122334455667788"));
  }

  @Test
  public void 行分析() {
    验证助记符("mov", "mov rax, 0x1122334455667788");
    验证助记符("mov", "mov al, 0");
    验证助记符("mov", "mov byte al, 0");
    验证助记符("xchg", "xchg ax, ax");
    验证助记符("lar", "lar ax, bx");
    验证助记符("mov", "mov byte [0], 0");
    验证助记符("mov", "mov [0], word 0");
    验证助记符("mov", "mov dword [0], dword 0");
    验证助记符("mov", "mov eax, 0");
    验证助记符("mov", "mov bx, 1h");
  }

  @Test
  public void 匹配操作数信息() {
    assertTrue(分析器类.操作数类型匹配(new 操作数信息类(操作数类型.寄存器, 64, "RAX"), new 操作数元数据类(false, "vqs", "rAX", null)));
    assertTrue(分析器类.操作数类型匹配(new 操作数信息类(操作数类型.寄存器, 16, "AX"), new 操作数元数据类(false, "vqp", "rAX", null)));
  }

  private void 验证助记符(String 助记符, String 行) {
    assertEquals(助记符, 分析器类.分析(行).助记符);
  }
  
  private void 验证操作码(String 操作码值, String 行) {
    代码行类 代码行 = 分析器类.分析代码(行);
    操作码元数据类 操作码元数据 = 代码行.操作码;
    assertNotNull(操作码元数据);
    assertEquals(操作码值, 指令类.生成操作码(操作码元数据.值));
  }
}
