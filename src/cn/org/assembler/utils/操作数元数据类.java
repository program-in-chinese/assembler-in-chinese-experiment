package cn.org.assembler.utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import cn.org.assembler.constants.寄存器常量;

public class 操作数元数据类 {

  public static final 操作数元数据类 寄存器 = new 操作数元数据类();
  public static final 操作数元数据类 寄存器64 = new 操作数元数据类();
  public static final 操作数元数据类 双字寄存器 = new 操作数元数据类();
  public static final 操作数元数据类 单字寄存器 = new 操作数元数据类();
  public static final 操作数元数据类 单字节寄存器 = new 操作数元数据类();
  public static final 操作数元数据类 立即数64 = new 操作数元数据类();
  public static final 操作数元数据类 立即数32 = new 操作数元数据类();
  public static final 操作数元数据类 单字立即数 = new 操作数元数据类();
  public static final 操作数元数据类 立即数8_有符号 = new 操作数元数据类();
  public static final 操作数元数据类 不确定 = new 操作数元数据类();

  public static final String 类型8 = "b";
  public static final String 类型8_有符号 = "bs";
  public static final String 类型16 = "w";
  public static final String 类型16_32 = "v";
  public static final String 类型16_32_可扩展到64 = "vds";
  public static final String 类型16_32_64 = "vqp";

  public static final String 寻址方式_寄存器_ModRM = "E";
  public static final String 寻址方式_寄存器_ModRM_reg_通用寄存器 = "G";
  public static final String 寻址方式_寄存器_ModRM_mod_通用寄存器 = "R";
  public static final String 寻址方式_寄存器 = "Z";
  public static final String 寻址方式_立即数 = "I";
  
  private static final Set<String> 寄存器寻址方式 = new HashSet<>();

  static {
    寄存器寻址方式.add(寻址方式_寄存器);
    寄存器寻址方式.add(寻址方式_寄存器_ModRM);
    寄存器寻址方式.add(寻址方式_寄存器_ModRM_reg_通用寄存器);
    寄存器寻址方式.add(寻址方式_寄存器_ModRM_mod_通用寄存器);
    
    寄存器64.寻址方式 = 寻址方式_寄存器;
    双字寄存器.寻址方式 = 寻址方式_寄存器;
    单字寄存器.寻址方式 = 寻址方式_寄存器;
    寄存器.寻址方式 = 寻址方式_寄存器;
    单字节寄存器.寻址方式 = 寻址方式_寄存器;

    单字立即数.寻址方式 = 寻址方式_立即数;
    单字立即数.类型 = 类型16;

    立即数32.寻址方式 = 寻址方式_立即数;
    立即数32.类型 = 类型16_32_可扩展到64;

    立即数64.寻址方式 = 寻址方式_立即数;
    立即数64.类型 = 类型16_32_64;

    立即数8_有符号.寻址方式 = 寻址方式_立即数;
    立即数8_有符号.类型 = 类型8_有符号;

    寄存器64.类型 = 类型16_32_64;
    双字寄存器.类型 = 类型16_32_可扩展到64;
    单字寄存器.类型 = 类型16;
    单字节寄存器.类型 = 类型8;
  }

  public boolean 为源;

  // TODO: 改为ENUM
  public String 类型; // 16/32/64
  public String 寻址方式; // 寄存器/立即数/内存

  public String 显式名称; // 如: <src address="I">1</src>

  public 操作数元数据类() {}

  public 操作数元数据类(boolean 为源, String 类型, String 寻址方式, String 显式名称) {
    this.为源 = 为源;
    this.类型 = 类型;
    this.寻址方式 = 寻址方式;
    this.显式名称 = 显式名称;
  }

  public int 取位数() {
    return 类型.equals(类型8) || 类型.equals(类型8) ? 8 : 类型.equals(类型16) ? 16 : 类型.equals(类型16_32_可扩展到64) ? 32 : 64;
  }

  // TODO: 仅作演示用. 需更精细的模式匹配
  public static 操作数元数据类 取操作数类型(String 操作数) {
    if (操作数.startsWith("0x")) {
      long 数值 = Long.parseLong(操作数.substring(2), 16);
      return 数值 > 4294967295L ? 立即数64 : (数值 > 32767 || 数值 < -32768) ? 立即数32
          : (数值 > 127 || 数值 < -128) ? 单字立即数 : 立即数8_有符号;
    }
    // TODO: 支持空格之外的间隔符
    else if (操作数.indexOf(" ") > 0) {
      String[] 三段 = 操作数.split(" ");
      String 强制类型 = "";
      String 操作对象 = "";
      // TODO: 暂不支持隐藏类型,如add rax, strict 4
      if (三段.length == 2) {
        强制类型 = 三段[0];
        操作对象 = 三段[1];
      } else if (三段.length == 3 && 三段[0].equals("strict")) {
        强制类型 = 三段[1];
        操作对象 = 三段[2];
      } else {
        return 不确定;
      }
      boolean 为寄存器 = 寄存器常量.取寄存器码(操作对象) != null;
      if (强制类型.equals("dword")) {
        return 为寄存器 ? 双字寄存器 : 立即数32;
      } else if (强制类型.equals("word")) {
        return 为寄存器 ? 单字寄存器 : 单字立即数;
      } else if (强制类型.equals("byte")) {
        return 为寄存器 ? 单字节寄存器 : 立即数8_有符号;
      }
    } else if (为数值(操作数)) {
      long 数值 = Long.parseLong(操作数);
      // TODO: 数值有误
      return 数值 > 4294967295L ? 立即数64 : 数值 > 255 ? 立即数32 : 立即数8_有符号;
    } else {
      return 寄存器常量.取寄存器类型(操作数);
    }
    return 不确定;
  }

  /**
   * 正负十进制数,不包含小数点
   */
  public static boolean 为数值(String 字符串) {
    return 字符串.matches("[-+]?\\d+");
  }

  /**
   * 将分析得到的操作数类型与指令元数据中的操作数类型进行匹配
   */
  public boolean 匹配(操作数元数据类 目标操作数类型) {
    // 匹配立即数
    if (Objects.equals(目标操作数类型.寻址方式, 操作数元数据类.寻址方式_立即数)) {
      return (Objects.equals(寻址方式, 操作数元数据类.寻址方式_立即数)
      // strict word 5 匹配 Ivds
      && ((操作数元数据类.类型16.equals(类型) && 操作数元数据类.类型16_32_可扩展到64.equals(目标操作数类型.类型))
      // 0 匹配 Ib
      || (操作数元数据类.类型8_有符号.equals(类型) && 操作数元数据类.类型8.equals(目标操作数类型.类型))));
    } else {
      boolean 类型匹配 =
          Objects.equals(类型, 目标操作数类型.类型)
              || ((操作数元数据类.类型16.equals(类型) || 操作数元数据类.类型16_32_可扩展到64.equals(类型)) 
                  && 操作数元数据类.类型16_32_64.equals(目标操作数类型.类型))
              || (操作数元数据类.类型16.equals(类型) && 操作数元数据类.类型16_32.equals(目标操作数类型.类型));
      boolean 寻址方式匹配 =
          Objects.equals(寻址方式, 目标操作数类型.寻址方式)
              || (操作数元数据类.寻址方式_寄存器.equals(寻址方式) && 寄存器寻址方式.contains(目标操作数类型.寻址方式));
      return 寻址方式匹配 && 类型匹配;
    }
  }

  @Override
  public boolean equals(Object 对象) {
    return 对象 instanceof 操作数元数据类 && Objects.equals(类型, ((操作数元数据类) 对象).类型)
        && Objects.equals(寻址方式, ((操作数元数据类) 对象).寻址方式);
  }

}
