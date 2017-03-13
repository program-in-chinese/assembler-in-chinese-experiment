package cn.org.assembler;

import java.util.ArrayList;
import java.util.List;

import cn.org.assembler.utils.指令元数据类;
import cn.org.assembler.utils.指令格式类;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据处理类;
import cn.org.assembler.utils.操作码元数据类;

public class 分析器类 {

  private static final List<操作码元数据类> 操作码元数据表 = 操作码元数据处理类.提取操作码信息();
  
  // TODO: 优化 - 避免线性查找
  // TODO: 不仅返回操作码元数据, 还有操作数的信息(立即数位数, 寄存器值, 等等)
  public static List<操作码元数据类> 查找操作码(代码行类 代码行) {
    String 助记符名 = 代码行.助记符;
    操作数元数据类 操作数2类型 = 操作数元数据类.取操作数类型(代码行.操作数2);
    List<操作码元数据类> 操作码元数据 = new ArrayList<>();
    for (操作码元数据类 某操作码元数据 : 操作码元数据表) {
      for (指令元数据类 指令元数据 : 某操作码元数据.指令元数据) {
        for (指令格式类 格式 : 指令元数据.格式) {

          // TODO: 完全忽略大小写?
          // TODO: 必须检查第一个操作数类型
          // TODO: 提取匹配操作数类型到单独函数
          if (助记符名.equalsIgnoreCase(格式.助记符) && 格式.操作数.size() == 2 
              && 格式.操作数.get(1).equals(操作数2类型)) {
            操作码元数据.add(某操作码元数据);
          }
        }
      }
    }
    return 操作码元数据;
  }
}
