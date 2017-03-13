package cn.org.assembler;

import java.util.ArrayList;
import java.util.List;

import cn.org.assembler.constants.寄存器常量;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据类;

public class 汇编器类 {

  public static List<String> 指令汇编(代码行类 代码行) {
    return 指令汇编(代码行.查找操作码(), 代码行.操作数1, 代码行.操作数2);
  }

  // TODO: 添加intel指令文档(包含版本号)中的对应章节号,方便查询
  private static List<String> 指令汇编(操作码元数据类 操作码, String 操作数1, String 操作数2) {
    List<String> 二进制码 = new ArrayList<>();
    操作数元数据类 操作数1类型 = 操作码.指令元数据.get(0).格式.get(0).操作数.get(0);
    操作数元数据类 操作数2类型 = 操作码.指令元数据.get(0).格式.get(0).操作数.get(1);
    String rex前缀 = "4";
    if (操作数1类型.类型.equals(操作数元数据类.类型16_32_64)) {
      rex前缀 += "8";
    }
    二进制码.add(rex前缀);
    
    switch (操作数1类型.寻址方式) {
      case 操作数元数据类.寻址方式_寄存器:
        二进制码.add(Integer.toHexString(操作码.值 + 寄存器常量.取寄存器码(操作数1)));
        break;
      case 操作数元数据类.寻址方式_寄存器_ModRM:
        二进制码.add(Integer.toHexString(操作码.值));
        // TODO: 生成ModR/M码
        二进制码.add("c0");
        break;
      default:
        ;
    }
    
    if (操作数2类型.寻址方式.equals(操作数元数据类.寻址方式_立即数)) {
      二进制码.addAll(生成二进制码(操作数2, 操作数2类型.取立即数位数()));
    }
    return 二进制码;
  }

  /**
   * @param 十六进制立即数 0x开头的十六进制数
   * @return
   */
  public static List<String> 生成二进制码(String 十六进制立即数, int 立即数位数) {
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
    
    // 按照立即数位数补全0
    for (int 位数 = 立即数.length() * 4; 位数 < 立即数位数; 位数 += 8) {
      二进制码.add("00");
    }

    return 二进制码;
  }
}
