package cn.org.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.org.assembler.utils.指令元数据类;
import cn.org.assembler.utils.指令格式类;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据处理类;
import cn.org.assembler.utils.操作码元数据类;
import cn.org.assembler.模型.代码行类;

public class 分析器类 {

  // TODO: 优化 - 避免线性查找
  // TODO: 不仅返回操作码元数据, 还有操作数的信息(立即数位数, 寄存器值, 等等)
  public static List<操作码元数据类> 查找操作码(代码行类 代码行) {
    String 助记符名 = 代码行.助记符;
    List<操作码元数据类> 操作码元数据 = new ArrayList<>();
    List<操作码元数据类> 操作码元数据表 = 操作码元数据处理类.按助记符查找操作码(助记符名);
    if (操作码元数据表 == null) {
      return 操作码元数据;
    }
    for (操作码元数据类 某操作码元数据 : 操作码元数据表) {
      List<指令元数据类> 匹配指令元数据 = new ArrayList<>();
      for (指令元数据类 指令元数据 : 某操作码元数据.指令元数据) {
        for (指令格式类 格式 : 指令元数据.格式) {

          // TODO: 完全忽略大小写?
          // TODO: 提取匹配操作数类型到单独函数
          if (助记符名.toUpperCase().equals(格式.助记符) && 格式.操作数.size() == 2) {
            if (操作数类型匹配(代码行.操作数2, 代码行.操作数2类型, 格式.操作数.get(1))) {
              if (操作数类型匹配(代码行.操作数1, 代码行.操作数1类型, 格式.操作数.get(0))) {
                匹配指令元数据.add(指令元数据);
              }
            }
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

  public static boolean 操作数类型匹配(String 待匹配操作数, 操作数元数据类 待匹配操作数类型, 操作数元数据类 目标操作数类型) {
/*
 *  A.2.3 Register Codes
 *      When an opcode requires a specific register as an operand, the register is identified by name (for example, AX, CL,
        or ESI). The name indicates whether the register is 64, 32, 16, or 8 bits wide.
        A register identifier of the form eXX or rXX is used when register width depends on the operand-size attribute. eXX
        is used when 16 or 32-bit sizes are possible; rXX is used when 16, 32, or 64-bit sizes are possible.
 */
    // AX,EAX,RAX - rAX
    if (目标操作数类型.寻址方式.startsWith("r")) {
      String 寄存器名 = 目标操作数类型.寻址方式.substring(1);
      return 待匹配操作数.equalsIgnoreCase(寄存器名) || 待匹配操作数.substring(1).equalsIgnoreCase(寄存器名);
    } 
    // 匹配立即数
    else if (Objects.equals(目标操作数类型.寻址方式, 操作数元数据类.寻址方式_立即数)) {
      return Objects.equals(待匹配操作数类型, 目标操作数类型) 
          || (Objects.equals(待匹配操作数类型.寻址方式, 操作数元数据类.寻址方式_立即数)
                  // strict word 5 匹配 Ivds
              && ((操作数元数据类.类型16.equals(待匹配操作数类型.类型) && 操作数元数据类.类型16_32.equals(目标操作数类型.类型))
                  // 0 匹配 Ib
                  || (操作数元数据类.类型8_有符号.equals(待匹配操作数类型.类型) && 操作数元数据类.类型8.equals(目标操作数类型.类型))));
    } else {
      // TODO: 待改进
      boolean 类型匹配 = Objects.equals(待匹配操作数类型.类型, 目标操作数类型.类型)
          || ((操作数元数据类.类型16.equals(待匹配操作数类型.类型) || 操作数元数据类.类型16_32.equals(待匹配操作数类型.类型)) 
              && 操作数元数据类.类型16_32_64.equals(目标操作数类型.类型));
      boolean 寻址方式匹配 = Objects.equals(待匹配操作数类型.寻址方式, 目标操作数类型.寻址方式)
          || (待匹配操作数类型.寻址方式 != null && 待匹配操作数类型.寻址方式.equals(操作数元数据类.寻址方式_寄存器) && 目标操作数类型.寻址方式
              .equals(操作数元数据类.寻址方式_寄存器_ModRM));
      return 待匹配操作数类型.equals(目标操作数类型) || (寻址方式匹配 && 类型匹配);
    }
  }
}
