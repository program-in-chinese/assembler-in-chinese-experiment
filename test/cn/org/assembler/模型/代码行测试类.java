package cn.org.assembler.模型;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.org.assembler.汇编器类;
import cn.org.assembler.模型.代码行类.操作数类型;
import cn.org.assembler.测试资源.测试资源处理类;

public class 代码行测试类 {

  @Test
  public void 取操作数信息() {
    assertEquals(new 操作数信息(操作数类型.寄存器, 8, "AL"), 代码行类.取操作数信息("al"));
    assertEquals(new 操作数信息(操作数类型.寄存器, 16, "AX"), 代码行类.取操作数信息("ax"));
    assertEquals(new 操作数信息(操作数类型.寄存器, 64, "RAX"), 代码行类.取操作数信息("rax"));
    assertEquals(new 操作数信息(操作数类型.寄存器, 8, "AL"), 代码行类.取操作数信息("byte al"));
    assertEquals(new 操作数信息(操作数类型.立即数, 8, "0"), 代码行类.取操作数信息("0"));
    assertEquals(new 操作数信息(操作数类型.立即数, 8, "127"), 代码行类.取操作数信息("strict byte 0x7f"));
    assertEquals(new 操作数信息(操作数类型.立即数, 8, "1"), 代码行类.取操作数信息("1h"));
    assertEquals(new 操作数信息(操作数类型.立即数, 32, "35"), 代码行类.取操作数信息("strict dword 35"));
    assertEquals(new 操作数信息(操作数类型.立即数, 16, "128"), 代码行类.取操作数信息("0x80"));
    assertEquals(new 操作数信息(操作数类型.立即数, 64, "1234605616436508552"), 代码行类.取操作数信息("0x1122334455667788"));
    assertEquals(new 操作数信息(操作数类型.内存, 0, "0"), 代码行类.取操作数信息("[0]"));
    assertEquals(new 操作数信息(操作数类型.内存, 8, "0"), 代码行类.取操作数信息("byte [0]"));
    assertEquals(new 操作数信息(操作数类型.内存, 32, "0"), 代码行类.取操作数信息("dword [0]"));
  }

  @Test
  public void 行分析() {
    assertEquals("mov", 代码行类.分析("mov rax, 0x1122334455667788").助记符);
    代码行类 代码行 = 代码行类.分析("mov al, 0");
    assertEquals("mov", 代码行.助记符);
    代码行 = 代码行类.分析("mov byte al, 0");
    assertEquals("mov", 代码行.助记符);
    代码行 = 代码行类.分析("xchg ax, ax");
    assertEquals("xchg", 代码行.助记符);
    代码行 = 代码行类.分析("lar ax, bx");
    assertEquals("lar", 代码行.助记符);
    代码行 = 代码行类.分析("mov byte [0], 0");
    assertEquals("mov", 代码行.助记符);
    assertEquals("mov", 代码行类.分析("mov [0], word 0").助记符);
    assertEquals("mov", 代码行类.分析("mov dword [0], dword 0").助记符);
    assertEquals("mov", 代码行类.分析("mov eax, 0").助记符);
    assertEquals("mov", 代码行类.分析("mov bx, 1h").助记符);
  }

  @Test
  public void 全部指令() {
    int 已识别代码行数 = 0;
    List<String> 源码文件名列表 = 测试资源处理类.取所有测试源码文件名();
    List<String> 编译错误代码行 = new ArrayList<>();
    List<String> 编译正确代码行 = new ArrayList<>();

    for (String 源码文件名 : 源码文件名列表) {
      System.out.println("文件:" + 源码文件名);
      List<String> 行 = 测试资源处理类.取源码代码行(源码文件名);
      assertTrue("测试数据不应为空", 行.size() > 0);

      boolean 已有匹配 = false;
      boolean 比较中止 = false;
      int 二进制文件行号 = 0;
      int 默认操作数长度 = 16;
      for (int 行号 = 0; 行号 < 行.size(); 行号++) {
        String 单行 = 行.get(行号);
        int 位数关键词索引 = 单行.indexOf("bits ");
        if (位数关键词索引 == 0 || 位数关键词索引 == 1) {
          默认操作数长度 = Integer.parseInt(单行.substring(位数关键词索引 + 5, 位数关键词索引 + 7));
        }
        代码行类 代码行 = 代码行类.分析(单行);
        if (代码行 != null) {
          // 跳过注释行/空行
          if (代码行.为空) {
            continue;
          }
          
          // TODO: 支持2个之外的操作数
          if (代码行.操作数信息.size() != 2) {
            已识别代码行数++;
            continue;
          }
          List<String> 二进制码 = 汇编器类.指令汇编(代码行, 默认操作数长度);

          // 按照源码行顺序,比较二进制码. 一旦发现不匹配,停止比较.
          if (!(二进制码.isEmpty() || (二进制码.size() == 1 && 二进制码.get(0).equals("66"))) && !比较中止) {
            String 编译结果 = 源码文件名 + ".asm:" + (行号 + 1) + " " + 单行 + " --> " + 二进制码;
            二进制文件行号 = 比较目标二进制码(二进制码, 源码文件名, 二进制文件行号);
            if (二进制文件行号 > 0) {
              编译正确代码行.add(编译结果);
              已有匹配 = true;
            } else {
              编译错误代码行.add(编译结果);
              比较中止 = true;
            }
          } else {
            //System.out.println(单行);
            比较中止 = true;
          }

          已识别代码行数++;
        }
        // 如果代码分析跳行了,直接中止比较
        else {
          if (已有匹配) {
            比较中止 = true;
          }
        }
      }
    }
    assertTrue(编译正确代码行.size() > 0);
    System.out.println("\n\n =====编译正确=======");
    for (String 编译正确结果 : 编译正确代码行) {
      System.out.println(编译正确结果);
    }
    System.out.println("\n\n =====编译错误=======");
    for (String 编译错误结果 : 编译错误代码行) {
      System.out.println(编译错误结果);
    }
    System.out.println("编译正确代码行数: " + 编译正确代码行.size() + " 已识别代码行数: " + 已识别代码行数);
  }

  /**
   * @param 二进制码 编译生成的二进制码
   * @param 源码文件名 按照此文件名查询对应的.hex文件
   * @param 二进制文件行号 从此行号开始比较二进制码
   * @return 如果完全匹配, 返回匹配的最后一行的行号; 否则返回-1
   */
  private int 比较目标二进制码(List<String> 二进制码, String 源码文件名, int 二进制文件行号) {
    List<String> 所有二进制码 = 测试资源处理类.读hex文件行(源码文件名);
    if (所有二进制码.isEmpty()) {
      System.out.println(源码文件名 + ".hex不存在或为空");
      return -1;
    }
    for (int 二进制码索引 = 0; 二进制码索引 < 二进制码.size(); 二进制码索引++) {
      int 二进制码行号 = 二进制文件行号 + 二进制码索引;
      if (!所有二进制码.get(二进制码行号).trim().equals(二进制码.get(二进制码索引))) {
        return -1;
      }
    }
    int 下一行号 = 二进制文件行号 + 二进制码.size();
    // 如果下一个二进制码是00,则必定没有结束,说明汇编错误
    if (所有二进制码.size() > 下一行号 && 所有二进制码.get(下一行号).startsWith("00")) {
      return -1;
    }
    return 下一行号;
  }

}
