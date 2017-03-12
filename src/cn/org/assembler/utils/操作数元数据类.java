package cn.org.assembler.utils;

import java.util.Objects;

public class 操作数元数据类 {

  public static final 操作数元数据类 寄存器64 = new 操作数元数据类();
  public static final 操作数元数据类 立即数64 = new 操作数元数据类();
  
  public static final String 类型16_32_64 = "vqp";
  
  public static final String 寻址方式_寄存器 = "Z";
  public static final String 寻址方式_立即数 = "I";
  
  static {
    立即数64.寻址方式 = 寻址方式_立即数;
    立即数64.类型 = 类型16_32_64;

    寄存器64.寻址方式 = 寻址方式_寄存器;
    寄存器64.类型 = 类型16_32_64;
  }

  public boolean 为源;

  // TODO: 改为ENUM
  public String 类型; // 16/32/64
  public String 寻址方式;

  public String 显式名称;

  @Override
  public boolean equals(Object 对象) {
    return 对象 instanceof 操作数元数据类 && Objects.equals(类型, ((操作数元数据类) 对象).类型)
        && Objects.equals(寻址方式, ((操作数元数据类) 对象).寻址方式);
  }

}
