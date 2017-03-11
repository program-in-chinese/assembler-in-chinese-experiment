package cn.org.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.org.assembler.utils.指令元数据类;
import cn.org.assembler.utils.指令格式类;
import cn.org.assembler.utils.操作码元数据处理类;
import cn.org.assembler.utils.操作码元数据类;

public class 汇编器类 {
  public enum 操作数类型 {
    寄存器32, 寄存器64, 立即数32, 立即数64
  }

  private static final List<操作码元数据类> 操作码元数据表 = 操作码元数据处理类.提取操作码信息();

  public static List<Integer> 指令汇编(指令类 指令) {
    List<Integer> 二进制码 = new ArrayList<>();
    String 助记符 = 指令.助记符;
    String 操作数1 = 指令.displacement;
    String 操作数2 = 指令.immediate;
    操作数类型 操作数1类型 = 取操作数类型(操作数1);
    操作数类型 操作数2类型 = 取操作数类型(操作数2);
    List<操作码元数据类> 操作码元数据 = 查找操作码(助记符, 操作数1类型, 操作数2类型);

    // TODO: 改善反馈信息
    if (操作码元数据.size() != 1) {
      System.out.println("无法确定操作码. 可能选项: "
          + 操作码元数据.stream().map(操作码元数据类::toString).collect(Collectors.joining(", ")));
      return 二进制码;
    }
    二进制码.add(操作码元数据.get(0).值);
    return 二进制码;
  }

  // TODO: 优化 - 避免线性查找
  public static List<操作码元数据类> 查找操作码(String 助记符名, 操作数类型 操作数1类型, 操作数类型 操作数2类型) {
    List<操作码元数据类> 操作码元数据 = new ArrayList<>();
    for (操作码元数据类 某操作码元数据 : 操作码元数据表) {
      for (指令元数据类 指令元数据 : 某操作码元数据.指令元数据) {
        for (指令格式类 格式 : 指令元数据.格式) {
          
          // TODO: 完全忽略大小写?
          if (助记符名.equalsIgnoreCase(格式.助记符)) {
            操作码元数据.add(某操作码元数据);
          }
        }
      }
    }
    return 操作码元数据;
  }

  // TODO: 仅作演示用
  public static 操作数类型 取操作数类型(String 操作数) {
    if (操作数.startsWith("0x")) {
      return 操作数类型.立即数64;
    } 
    return 操作数类型.寄存器64;
  }

}
