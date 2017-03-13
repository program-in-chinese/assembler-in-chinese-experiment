package cn.org.assembler;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cn.org.assembler.utils.操作码元数据类;

public class 代码行类 {

  public String 助记符;

  public String 操作数1;
  public String 操作数2;

  public static 代码行类 分析(String 行) {
    行 = 删除注释(行).trim();
    String 操作符格式 = "[A-Za-z]+";
    String 操作数1格式 = "[A-Za-z]+";
    String 操作数2格式 = "[A-Za-z0-9]+";
    Pattern 格式 =
        Pattern.compile("^(" + 操作符格式 + ")\\s+(" + 操作数1格式 + "),\\s*(" + 操作数2格式 + ")$");
    Matcher 匹配器 = 格式.matcher(行);
    if (匹配器.find()) {
      代码行类 代码行 = new 代码行类();
      代码行.助记符 = 匹配器.group(1);
      代码行.操作数1 = 匹配器.group(2);
      代码行.操作数2 = 匹配器.group(3);
      return 代码行;
    }
    return null;
  }

  public 操作码元数据类 查找操作码() {
    List<操作码元数据类> 操作码元数据 = 分析器类.查找操作码(this);

    // TODO: 改善反馈信息
    if (操作码元数据.size() != 1) {
      System.out.println("无法确定操作码. " + 操作码元数据.size() + "选项: "
          + 操作码元数据.stream().map(操作码元数据类::toString).collect(Collectors.joining(", ")));
      return null;
    }
    return 操作码元数据.get(0);
  }

  @Override
  public String toString() {
    return "操作符: " + 助记符 + " 操作数1: " + 操作数1 + " 操作数2: " + 操作数2;
  }

  private static String 删除注释(String 行) {
    int 注释位置 = 行.indexOf(";");
    return 注释位置 == -1 ? 行 : 行.substring(0, 注释位置);
  }
}
