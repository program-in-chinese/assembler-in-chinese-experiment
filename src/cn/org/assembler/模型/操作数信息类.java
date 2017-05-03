package cn.org.assembler.模型;

import java.util.Objects;

import cn.org.assembler.constants.寄存器常量;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.模型.代码行类.操作数类型;

public class 操作数信息类 {
  public 操作数类型 类型;
  public int 位数;
  public String 值;

  public 操作数信息类() {}

  public 操作数信息类(操作数类型 类型, int 位数, String 值) {
    this.类型 = 类型;
    this.位数 = 位数;
    this.值 = 值;
  }

  public static 操作数信息类 取操作数信息(String 操作数) {
    // TODO: 支持空格之外的间隔符
    if (操作数.indexOf(" ") > 0) {
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
        return null;
      }
      
      int 位数 = 0;
      if (强制类型.equals("dword")) {
        位数 = 32;
      } else if (强制类型.equals("word")) {
        位数 = 16;
      } else if (强制类型.equals("byte")) {
        位数 = 8;
      }
      操作数信息类 操作对象信息 = 取操作数信息(操作对象);
      if (操作对象信息 == null) {
        return null;
      }
      操作对象信息.位数 = 位数;
      return 操作对象信息;
    } else if (操作数元数据类.为数值(操作数)) {
      return 取操作数类型(Long.parseLong(操作数));
    } else if (操作数.startsWith("0x") && 操作数元数据类.为十六进制数值(操作数.substring(2))) {
      return 取操作数类型(Long.parseLong(操作数.substring(2), 16));
    } else if (操作数.startsWith("-0x") && 操作数元数据类.为十六进制数值(操作数.substring(3))) {
      return 取操作数类型(-Long.parseLong(操作数.substring(3), 16));
    } else if (操作数.endsWith("h") && 操作数元数据类.为数值(操作数.substring(0, 操作数.length() - 1))) {
      return 取操作数类型(Long.parseLong(操作数.substring(0, 操作数.length() - 1), 16));
    } else if (操作数.startsWith("[") && 操作数.endsWith("]")) {
        操作数信息类 信息 = new 操作数信息类();
        信息.类型 = 操作数类型.内存;
        信息.值 = 操作数.substring(1, 操作数.length() - 1);
        return 信息;
    } else {
      return 寄存器常量.取寄存器信息(操作数);
    }
  }

  @Override
  public boolean equals(Object 对象) {
    return 对象 instanceof 操作数信息类 && Objects.equals(类型, ((操作数信息类) 对象).类型)
        && Objects.equals(位数, ((操作数信息类) 对象).位数) && Objects.equals(值, ((操作数信息类) 对象).值);
  }
  
  @Override
  public String toString() {
    return 类型 + " " + 位数 + " " + 值;
  }

  private static 操作数信息类 取操作数类型(long 数值) {
    操作数信息类 信息 = new 操作数信息类();
    信息.类型 = 操作数类型.立即数;
    信息.值 = Long.toString(数值);
    if (数值 > 4294967295L) {
      信息.位数 = 64;
    } else if (数值 > 32767 || 数值 < -32768) {
      信息.位数 = 32;
    } else if (数值 > 127 || 数值 < -128) {
      信息.位数 = 16;
    } else {
      信息.位数 = 8;
    }
    return 信息;
  }
}
