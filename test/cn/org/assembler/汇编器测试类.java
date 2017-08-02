package cn.org.assembler;
import static com.github.programinchinese.断言.相等;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class 汇编器测试类 {

  @Test
  public void 指令汇编() {
    验证二进制码("mov rax, 0x1122334455667788",
        Arrays.asList("48", "b8", "88", "77", "66", "55", "44", "33", "22", "11"));
    验证二进制码("mov rax, 0x1000", Arrays.asList("48", "c7", "c0", "00", "10", "00", "00"));
    
    验证二进制码("add rax, 0x1000", Arrays.asList("48", "05", "00", "10", "00", "00"));
    验证二进制码("add eax, 4", Arrays.asList("83", "c0", "04"));
    验证二进制码("add ebx, 4", Arrays.asList("83", "c3", "04"));
    验证二进制码("add ax,-128", Arrays.asList("83", "c0", "80"));
    验证二进制码("and eax, 35", Arrays.asList("83", "e0", "23"));
    验证二进制码("add ax,0x80", Arrays.asList("05", "80", "00"));
    验证二进制码("mov al, 0", Arrays.asList("b0", "00"));
    验证二进制码("mov byte al, 0", Arrays.asList("b0", "00"));
    验证二进制码("mov byte [0], 0", Arrays.asList("c6", "06", "00", "00", "00"));
    验证二进制码("mov [0], word 0", Arrays.asList("c7", "06", "00", "00", "00", "00"));
    验证二进制码("mov eax, 0", 16, Arrays.asList("66", "b8", "00", "00", "00", "00"));
    验证二进制码("mov bx, 1h", Arrays.asList("bb", "01", "00"));
    //验证二进制码("mov cr0, eax", Arrays.asList("0f", "22", "c0"));
    //验证二进制码("lar ax, bx", Arrays.asList("0f", "02", "c3"));
    
    // 强制操作数长度
    验证二进制码("xchg ax, ax", 64, Arrays.asList("66", "90"));
    //验证二进制码("add rax, 0x1122334455667788", Arrays.asList("48", "05", "88", "77", "66", "55")); 
  }

  @Test
  public void 指令汇编为字符串() {
    assertEquals("48b88877665544332211", 汇编器类.指令汇编为字符串(分析器类.分析代码("mov rax, 0x1122334455667788")));
  }

  private void 验证二进制码(String 代码行, List<String> 二进制码) {
    相等(二进制码, 汇编器类.指令汇编(分析器类.分析代码(代码行)));
  }

  private void 验证二进制码(String 代码行, int 有效操作数长度, List<String> 二进制码) {
    相等(二进制码, 汇编器类.指令汇编(分析器类.分析代码(代码行), 有效操作数长度));
  }
}
