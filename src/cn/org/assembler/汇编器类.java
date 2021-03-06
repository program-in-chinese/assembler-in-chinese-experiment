package cn.org.assembler;

import java.util.ArrayList;
import java.util.List;

import cn.org.assembler.constants.寄存器常量;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据类;
import cn.org.assembler.模型.ModRM;
import cn.org.assembler.模型.SIB;
import cn.org.assembler.模型.代码行类;
import cn.org.assembler.模型.代码行类.操作数类型;
import cn.org.assembler.模型.指令类;
import cn.org.assembler.模型.操作数信息类;

public class 汇编器类 {

  // TODO: 添加intel指令文档(包含版本号)中的对应章节号,方便查询
  /**
   * vol2, 2.1.1 group 3. Operand-size override prefix is encoded using 66H (66H is also used as a
   * mandatory prefix for some instructions).
   */
  public static List<String> 指令汇编(代码行类 代码行, int 默认操作数长度) {
    List<String> 二进制码 = 汇编器类.指令汇编(代码行);
    int 有效操作码长度 = 代码行.取有效操作码长度();
    if (默认操作数长度 != 有效操作码长度 && 有效操作码长度 != 8) {
      二进制码.add(0, "66");
    }
    return 二进制码;
  }

  public static List<String> 指令汇编(代码行类 代码行) {

    操作码元数据类 操作码 = 代码行.操作码;
    
    // TODO: 支持两个以外操作数
    if(操作码 == null || 代码行.操作数信息.size() != 2) {
      return new ArrayList<>();
    }
    操作数元数据类 操作数1类型 = 操作码.指令元数据.get(0).格式.get(0).操作数.get(0);
    操作数元数据类 操作数2类型 = 操作码.指令元数据.get(0).格式.get(0).操作数.get(1);

    操作数信息类 操作数1信息 = 代码行.操作数信息.get(0);
    操作数信息类 操作数2信息 = 代码行.操作数信息.get(1);
    指令类 指令 = new 指令类();
    指令.模式 = 操作数1信息.位数;
    
    if (操作数1类型.类型.equals(操作数元数据类.类型16_32_64)
        && 操作数1信息.类型 == 操作数类型.寄存器 && 操作数1信息.位数 == 64) {
        指令.rex前缀 = "48";
    }

    switch (操作数1类型.寻址方式) {
      case 操作数元数据类.寻址方式_寄存器:
        指令.set操作码(操作码.值 + 寄存器常量.取寄存器码(操作数1信息.值));
        break;
      case 操作数元数据类.寻址方式_寄存器_ModRM:
        指令.set操作码(操作码.值);

        指令.modRM = new ModRM();
        if (操作数1信息.类型 == 操作数类型.内存) {
          指令.modRM.mod = 0b00;
          // TODO: 仅支持直接寻址
          指令.modRM.rm = 0b110;
          指令.displacement = 操作数1信息.值;
          // TODO: 确认displacement是否允许超过单字节
          指令.displacement位数 = 8;
          指令.sib = new SIB();
        } else {
          指令.modRM.mod = 0b11;
          // 有扩展码时,赋值给modRM的reg部分
          if (!操作码.指令元数据.get(0).无扩展码()) {
            指令.modRM.reg = 操作码.指令元数据.get(0).扩展码;
          }
          指令.modRM.rm = 寄存器常量.取寄存器码(操作数1信息.值);
        }
        break;
      default:
        // 包括类型: rAX
        指令.set操作码(操作码.值);
        break;
    }
    
    if (操作数2类型.寻址方式.equals(操作数元数据类.寻址方式_立即数) && 操作数2类型.显式名称 == null) {
      指令.立即数 = 操作数2信息.值;
      指令.立即数类型 = 操作数2类型;
    }
    
    return 指令.生成二进制码();
  }
  
  public static String 指令汇编为字符串(代码行类 代码行) {
    return String.join("", 指令汇编(代码行));
  }
}
