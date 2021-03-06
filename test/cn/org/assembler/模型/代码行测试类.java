package cn.org.assembler.模型;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.org.assembler.分析器类;
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
        代码行类 代码行 = 分析器类.分析代码(单行);
        if (代码行 != null) {
          // 跳过注释行/空行
          if (代码行.为空) {
            continue;
          }
          
          // TODO: 适当log
          /*int 操作数个数 = 代码行.操作数信息.size();
          if ( 操作数个数 != 2) {
            if (操作数个数 == 3) {
              System.out.println("3操作数: " + 代码行);
            } else if (操作数个数 == 1) {
              System.out.println("单操作数: " + 代码行);
            } else if (操作数个数 == 0) {
              System.out.println("无操作数: " + 代码行);
            }
          }*/
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
          System.out.println("分析失败: " + 单行);
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
