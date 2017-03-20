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
    List<String> 编译正确代码行 = new ArrayList<>();
    for (String 源码文件名 : 源码文件名列表) {
      System.out.println("文件:" + 源码文件名);
      List<String> 行 = 测试资源处理类.取源码代码行(源码文件名);
      assertTrue("测试数据不应为空", 行.size() > 0);

      for (String 单行 : 行) {
        代码行类 代码行 = 代码行类.分析(单行);
        if (代码行 != null) {
          List<String> 二进制码 = 汇编器类.指令汇编(代码行);
          if (!二进制码.isEmpty()) {
            if (比较目标二进制码(二进制码, 源码文件名)){
              编译正确代码行.add(单行 + " --> " + 二进制码);
            }
          } else {
            System.out.println(单行);
          }
          
          已识别代码行数++;
        }
      }
    }
    assertTrue(编译正确代码行.size() > 0);
    for (String 编译正确结果: 编译正确代码行) {
      System.out.println(编译正确结果);
    }
    System.out.println("编译正确代码行数: " + 编译正确代码行.size() + " 已识别代码行数: " + 已识别代码行数);
  }

  /**
   * 假设没有代码行生成重复的二进制码, 只要.hex文件中包含该二进制码序列, 就认为编译结果正确
   */
  private boolean 比较目标二进制码(List<String> 二进制码, String 源码文件名) {
    return String.join("", 测试资源处理类.读hex文件行(源码文件名)).indexOf(String.join(" ", 二进制码)) >= 0;
  }

}
