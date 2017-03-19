package cn.org.assembler.测试资源;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class 测试资源处理类 {

  private static final String 测试文件路径 = "测试/指令/";

  public static List<String> 取所有测试源码文件名() {
    List<String> 文件名列表 = new ArrayList<>();
    File 测试资源目录 = new File(测试文件路径);
    if (测试资源目录.isDirectory()) {
      for (File 文件 : 测试资源目录.listFiles()) {
        String 文件名 = 文件.getName();
        if (文件.isFile() && 文件名.endsWith(".asm")) {
          文件名列表.add(文件名.substring(0, 文件名.length() - 4));
        }
      }
    }
    return 文件名列表;
  }
}
