package cn.org.assembler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 分析器 {

  public static 指令类 分析代码行(String 行) {
    行 = 删除注释(行).trim();
    String 操作符格式 = "[A-Za-z]+";
    String displacement格式 = "[A-Za-z]+";
    String immediate格式 = "[A-Za-z0-9]+";
    Pattern 格式 =
        Pattern.compile("^(" + 操作符格式 + ")\\s+(" + displacement格式 + "),\\s*(" + immediate格式 + ")$");
    Matcher 匹配器 = 格式.matcher(行);
    if (匹配器.find()) {
      指令类 指令 = new 指令类();
      指令.操作符 = 匹配器.group(1);
      指令.displacement = 匹配器.group(2);
      指令.immediate = 匹配器.group(3);
      return 指令;
    }
    return null;
  }

  private static String 删除注释(String 行) {
    int 注释位置 = 行.indexOf(";");
    return 注释位置 == -1 ? 行 : 行.substring(0, 注释位置);
  }
}
