package cn.org.assembler.模型;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.org.assembler.汇编器类;
import cn.org.assembler.测试资源.测试资源处理类;

public class 代码行测试类 {

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

      boolean 比较中止 = false;
      int 二进制文件行号 = 0;
      for (int 行号 = 0; 行号 < 行.size(); 行号++) {
        String 单行 = 行.get(行号);
        代码行类 代码行 = 代码行类.分析(单行);
        if (代码行 != null) {
          List<String> 二进制码 = 汇编器类.指令汇编(代码行);

          // 按照源码行顺序,比较二进制码. 一旦发现不匹配,停止比较.
          if (!二进制码.isEmpty() && !比较中止) {
            String 编译结果 = 源码文件名 + ".asm:" + (行号 + 1) + " " + 单行 + " --> " + 二进制码;
            二进制文件行号 = 比较目标二进制码(二进制码, 源码文件名, 二进制文件行号);
            if (二进制文件行号 > 0) {
              编译正确代码行.add(编译结果);
            } else {
              编译错误代码行.add(编译结果);
              比较中止 = true;
            }
          } else {
            System.out.println(单行);
            比较中止 = true;
          }

          已识别代码行数++;
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
    for (int 二进制码索引 = 0; 二进制码索引 < 二进制码.size(); 二进制码索引++) {
      int 二进制码行号 = 二进制文件行号 + 二进制码索引;
      if (!所有二进制码.get(二进制码行号).trim().equals(二进制码.get(二进制码索引))) {
        return -1;
      }
    }
    return 二进制文件行号 + 二进制码.size();
  }

}
