package cn.org.assembler.测试资源;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.org.assembler.utils.文件功能类;

public class 测试资源处理类 {

  private static final String 测试文件路径 = "测试/指令/";
  private static final String 汇编文件扩展名 = ".asm";
  private static final String 二进制文件扩展名 = ".hex";

  public static List<String> 取源码代码行(String 源码文件名) {
    return 文件功能类.读取行(测试文件路径 + 源码文件名 + 汇编文件扩展名);
  }
  
  /**
   * hex文件每行末有一空格
   */
  public static List<String> 读hex文件行(String 文件名) {
    return 文件功能类.读取行(测试文件路径 + 文件名 + 二进制文件扩展名);
  }

  public static List<String> 取所有测试源码文件名() {
    List<String> 文件名列表 = new ArrayList<>();
    File 测试资源目录 = new File(测试文件路径);
    if (测试资源目录.isDirectory()) {
      for (File 文件 : 测试资源目录.listFiles()) {
        String 文件名 = 文件.getName();
        if (文件.isFile() && 文件名.endsWith(汇编文件扩展名)) {
          文件名列表.add(文件名.substring(0, 文件名.length() - 4));
        }
      }
    }
    return 文件名列表;
  }
}
