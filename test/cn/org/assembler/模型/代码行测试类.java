package cn.org.assembler.模型;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import cn.org.assembler.测试资源.测试资源处理类;

public class 代码行测试类 {

  @Test
  public void test() {
    int 已识别代码行数 = 0;
    List<String> 源码文件名列表 = 测试资源处理类.取所有测试源码文件名();
    for (String 源码文件名 : 源码文件名列表) {
      List<String> 行 = 测试资源处理类.取代码行(源码文件名);
      assertTrue("测试数据不应为空", 行.size() > 0);

      for (String 单行 : 行) {
        代码行类 代码行 = 代码行类.分析(单行);
        if (代码行 != null) {
          已识别代码行数++;
        }
      }
    }
    assertTrue(已识别代码行数 > 0);
    System.out.println("已识别代码行数: " + 已识别代码行数);
  }

}
