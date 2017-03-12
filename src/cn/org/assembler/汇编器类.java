package cn.org.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.org.assembler.constants.寄存器常量;
import cn.org.assembler.utils.指令元数据类;
import cn.org.assembler.utils.指令格式类;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据处理类;
import cn.org.assembler.utils.操作码元数据类;

public class 汇编器类 {

  private static final List<操作码元数据类> 操作码元数据表 = 操作码元数据处理类.提取操作码信息();

  public static List<String> 指令汇编(指令类 指令) {
    List<String> 二进制码 = new ArrayList<>();
    String 助记符 = 指令.助记符;
    String 操作数1 = 指令.displacement;
    String 操作数2 = 指令.immediate;
    操作数元数据类 操作数1类型 = 取操作数类型(操作数1);
    操作数元数据类 操作数2类型 = 取操作数类型(操作数2);
    List<操作码元数据类> 操作码元数据 = 查找操作码(助记符, 操作数1类型, 操作数2类型);

    // TODO: 改善反馈信息
    if (操作码元数据.size() != 1) {
      System.out.println("无法确定操作码. " + 操作码元数据.size() + "选项: "
          + 操作码元数据.stream().map(操作码元数据类::toString).collect(Collectors.joining(", ")));
      return 二进制码;
    }
    
    // TODO: 添加intel指令文档(包含版本号)中的对应章节号,方便查询
    String rex前缀 = "4";
    if (操作数1类型.类型.equals(操作数元数据类.类型16_32_64)) {
      rex前缀 += "8";
    }
    二进制码.add(rex前缀);
    
    if (操作数1类型.寻址方式.equals(操作数元数据类.寻址方式_寄存器)) {
      二进制码.add(Integer.toHexString(操作码元数据.get(0).值 + 寄存器常量.取寄存器码(操作数1)));
    }
    
    if (操作数2类型.寻址方式.equals(操作数元数据类.寻址方式_立即数)) {
      二进制码.addAll(生成二进制码(操作数2));
    }
    return 二进制码;
  }

  // TODO: 优化 - 避免线性查找
  // TODO: 提取单独函数匹配操作数类型
  public static List<操作码元数据类> 查找操作码(String 助记符名, 操作数元数据类 操作数1类型, 操作数元数据类 操作数2类型) {
    List<操作码元数据类> 操作码元数据 = new ArrayList<>();
    for (操作码元数据类 某操作码元数据 : 操作码元数据表) {
      for (指令元数据类 指令元数据 : 某操作码元数据.指令元数据) {
        for (指令格式类 格式 : 指令元数据.格式) {

          // TODO: 完全忽略大小写?
          if (助记符名.equalsIgnoreCase(格式.助记符) && 格式.操作数.size() == 2 && 格式.操作数.get(0).equals(操作数1类型)
              && 格式.操作数.get(1).equals(操作数2类型)) {
            操作码元数据.add(某操作码元数据);
          }
        }
      }
    }
    return 操作码元数据;
  }

  // TODO: 仅作演示用. 需更精细的模式匹配
  public static 操作数元数据类 取操作数类型(String 操作数) {
    if (操作数.startsWith("0x")) {
      return 操作数元数据类.立即数64;
    } else {
      return 操作数元数据类.寄存器64;
    }
  }

  /**
   * @param 十六进制立即数 0x开头的十六进制数
   * @return
   */
  public static List<String> 生成二进制码(String 十六进制立即数) {
    List<String> 二进制码 = new ArrayList<>();
    // 删除开头的"0x"
    String 立即数 = 十六进制立即数.substring(2);
    if (立即数.length() % 2 == 1) {
      立即数 = "0" + 立即数;
    }
    
    // 反序
    for (int 索引 = 0; 索引<立即数.length()/2; 索引++) {
      二进制码.add(0, 立即数.substring(索引*2, 索引*2+2));
    }
    
    // TODO: 需要补几位0?
    return 二进制码;
  }
}
