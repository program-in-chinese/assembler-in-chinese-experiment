package cn.org.assembler;

import java.util.ArrayList;
import java.util.List;

import cn.org.assembler.utils.指令元数据类;
import cn.org.assembler.utils.指令格式类;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据处理类;
import cn.org.assembler.utils.操作码元数据类;
import cn.org.assembler.模型.代码行类;

public class 分析器类 {

  private static final List<操作码元数据类> 操作码元数据表 = 操作码元数据处理类.提取操作码信息();
  
  // TODO: 优化 - 避免线性查找
  // TODO: 不仅返回操作码元数据, 还有操作数的信息(立即数位数, 寄存器值, 等等)
  public static List<操作码元数据类> 查找操作码(代码行类 代码行) {
    String 助记符名 = 代码行.助记符;
    操作数元数据类 操作数2类型 = 操作数元数据类.取操作数类型(代码行.操作数2);
    List<操作码元数据类> 操作码元数据 = new ArrayList<>();
    for (操作码元数据类 某操作码元数据 : 操作码元数据表) {
      List<指令元数据类> 匹配指令元数据 = new ArrayList<>();
      for (指令元数据类 指令元数据 : 某操作码元数据.指令元数据) {
        for (指令格式类 格式 : 指令元数据.格式) {

          // TODO: 完全忽略大小写?
          // TODO: 提取匹配操作数类型到单独函数
          if (助记符名.equalsIgnoreCase(格式.助记符) && 格式.操作数.size() == 2 
              && 格式.操作数.get(1).equals(操作数2类型) && 操作数类型匹配(代码行.操作数1, 格式.操作数.get(0))) {
            匹配指令元数据.add(指令元数据);
          }
        }
      }
      if (!匹配指令元数据.isEmpty()) {
        操作码元数据类 匹配操作码元数据 = new 操作码元数据类();
        匹配操作码元数据.值 = 某操作码元数据.值;
        匹配操作码元数据.操作码字节数 = 某操作码元数据.操作码字节数;
        匹配操作码元数据.指令元数据 = 匹配指令元数据;
        操作码元数据.add(匹配操作码元数据);
      }
    }
    return 操作码元数据;
  }

  public static boolean 操作数类型匹配(String 待匹配操作数, 操作数元数据类 目标操作数类型) {
/*
 *  A.2.3 Register Codes
 *      When an opcode requires a specific register as an operand, the register is identified by name (for example, AX, CL,
        or ESI). The name indicates whether the register is 64, 32, 16, or 8 bits wide.
        A register identifier of the form eXX or rXX is used when register width depends on the operand-size attribute. eXX
        is used when 16 or 32-bit sizes are possible; rXX is used when 16, 32, or 64-bit sizes are possible.
 */
    // EAX,RAX - rAX
    if (目标操作数类型.寻址方式.startsWith("r")) {
      return 待匹配操作数.substring(1).equalsIgnoreCase(目标操作数类型.寻址方式.substring(1));
    } else {
      操作数元数据类 操作数类型 = 操作数元数据类.取操作数类型(待匹配操作数);
      // TODO: 待改进
      return 操作数类型.equals(目标操作数类型)
          || (操作数类型.寻址方式 != null && 操作数类型.寻址方式.equals(操作数元数据类.寻址方式_寄存器) && 目标操作数类型.寻址方式
              .equals(操作数元数据类.寻址方式_寄存器_ModRM));
    }
  }
}
