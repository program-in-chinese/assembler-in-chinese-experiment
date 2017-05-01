package cn.org.assembler.utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import cn.org.assembler.模型.代码行类.操作数类型;
import cn.org.assembler.模型.操作数信息类;

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
  public static final 操作数元数据类 单字节内存 = new 操作数元数据类();
  public static final 操作数元数据类 单字内存 = new 操作数元数据类();
  public static final 操作数元数据类 双字内存 = new 操作数元数据类();

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
    
    单字节内存.寻址方式 = 寻址方式_寄存器_ModRM;
    单字节内存.类型 = 类型8;
    单字内存.寻址方式 = 寻址方式_寄存器_ModRM;
    单字内存.类型 = 类型16;
    双字内存.寻址方式 = 寻址方式_寄存器_ModRM;
    双字内存.类型 = 类型16_32_可扩展到64;
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
    return 类型.equals(类型8) || 类型.equals(类型8_有符号) ? 8 : 类型.equals(类型16) ? 16 : 类型.equals(类型16_32_可扩展到64) ? 32 : 64;
  }

  /**
   * 正负十进制数,不包含小数点
   */
  public static boolean 为数值(String 字符串) {
    return 字符串.matches("[-+]?\\d+");
  }
  public static boolean 为十六进制数值(String 字符串) {
    return 字符串.matches("[\\da-fA-F]+");
  }

  @Override
  public boolean equals(Object 对象) {
    return 对象 instanceof 操作数元数据类 && Objects.equals(类型, ((操作数元数据类) 对象).类型)
        && Objects.equals(寻址方式, ((操作数元数据类) 对象).寻址方式);
  }

  /**
   * 将分析得到的操作数信息与指令元数据中的操作数类型进行匹配
   */
  public boolean 匹配(操作数信息类 待操作数信息) {
    int 位数 = 待操作数信息.位数;
    操作数类型 待操作数类型 = 待操作数信息.类型;
    // 匹配立即数
    if (Objects.equals(寻址方式, 操作数元数据类.寻址方式_立即数) && 类型 != null) {
      return (Objects.equals(待操作数类型, 操作数类型.立即数)
      // strict word 5 匹配 Ivds
      && (位数 == 取位数()
      || (位数 == 16 && 操作数元数据类.类型16_32_可扩展到64.equals(类型))
      || (// 0 匹配 Ivqp
          位数 == 8 && 操作数元数据类.类型16_32_64.equals(类型))
      || (位数 == 32 && 操作数元数据类.类型16_32_64.equals(类型))));
    } else if ((操作数类型.内存.equals(待操作数类型) && 寻址方式_寄存器_ModRM.equals(寻址方式)) 
        || (操作数类型.寄存器.equals(待操作数类型) && 寄存器寻址方式.contains(寻址方式))){
      return 位数 <= 取位数();
    } else {
      return Objects.equals(待操作数信息.值, 显式名称);
    }
  }
}
