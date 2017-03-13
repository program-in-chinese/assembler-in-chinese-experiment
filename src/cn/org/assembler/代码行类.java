package cn.org.assembler;

import java.util.List;
import java.util.stream.Collectors;

import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据类;

public class 代码行类 {

  public String 助记符;

  public String 操作数1;
  public String 操作数2;
  
  public 操作码元数据类 查找操作码() {
    操作数元数据类 操作数1类型 = 操作数元数据类.取操作数类型(操作数1);
    操作数元数据类 操作数2类型 = 操作数元数据类.取操作数类型(操作数2);
    List<操作码元数据类> 操作码元数据 = 分析器类.查找操作码(助记符, 操作数1类型, 操作数2类型);

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

}
