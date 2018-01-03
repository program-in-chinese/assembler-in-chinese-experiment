package cn.org.assembler;

import static com.github.program_in_chinese.junit4_in_chinese.断言.相等;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import cn.org.assembler.utils.操作码元数据类;
import cn.org.assembler.模型.代码行类;
import cn.org.assembler.模型.指令类;


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
    验证助记符("idiv", "idiv al");
    验证助记符("idiv", "idiv al      ; F6 F8");
    验证助记符("shrd", "shrd cx, dx, cl");
  }

  private void 验证助记符(String 助记符, String 行) {
    相等(助记符, 分析器类.分析(行).助记符);
  }
  
  private void 验证操作码(String 操作码值, String 行) {
    代码行类 代码行 = 分析器类.分析代码(行);
    操作码元数据类 操作码元数据 = 代码行.操作码;
    assertNotNull(操作码元数据);
    相等(操作码值, 指令类.生成操作码(操作码元数据.值));
  }
}
