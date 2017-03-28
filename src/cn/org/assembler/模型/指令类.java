package cn.org.assembler.模型;

import java.util.ArrayList;
import java.util.List;

import cn.org.assembler.utils.操作数元数据类;

public class 指令类 {

  // Legacy Prefix
  public String rex前缀;

  public String 操作码;

  public ModRM modRM;
  // SIB

  public String displacement;
  public String 立即数;
  public 操作数元数据类 立即数类型;
  
  // 16/32/64
  public int 模式 = 32;

  public List<String> 生成二进制码() {
    List<String> 二进制码 = new ArrayList<>();
    if (rex前缀 != null) {
      二进制码.add(rex前缀);
    }
    if (操作码 != null) {
      二进制码.add(操作码);
    }
    if (modRM != null) {
      二进制码.add(modRM.生成二进制码());
    }
    if (立即数 != null && 立即数类型 != null) {
      int 立即数位数 = 立即数类型.equals(操作数元数据类.立即数8_有符号) ? 8 : 立即数类型.equals(操作数元数据类.单字立即数) ? 16
          : 立即数类型.equals(操作数元数据类.立即数32) ? 32 : 64;
      // TODO: 待考证 - 64模式下, 只支持32/64两种立即数位数?
      if (立即数位数 == 16 && 模式 == 64) {
        立即数位数 = 32;
      }
      二进制码.addAll(生成二进制码(立即数, 立即数位数));
      
    }
    return 二进制码;
  }

  public void set操作码(int 操作码值) {
    this.操作码 = 生成操作码(操作码值);
  }

  /**
   * @param 立即数字符 0x开头的十六进制数 或 十进制数
   * @return
   */
  public static List<String> 生成二进制码(String 立即数字符, int 立即数位数) {
    List<String> 二进制码 = new ArrayList<>();
    String 立即数 = 立即数字符;
    // TODO: 与 取操作数类型 重复
    if (立即数.indexOf(" ") > 0) {
      String[] 三段 = 立即数.split(" ");
      if (三段.length == 3 && 三段[0].equals("strict")) {
        立即数 = 三段[2];
      } else if (三段.length == 2) {
        立即数 = 三段[1];
      }
    }
    // TODO: 暂不支持-0x80000000
    if (立即数.startsWith("-0x")) {
      立即数 = 立即数.substring(1);
    }
    if (立即数.startsWith("0x")) {
      立即数 = 立即数.substring(2);
    } else {
      // TODO: 不支持变量名
      if (操作数元数据类.isNumeric(立即数)) {
        立即数 = Long.toHexString(Long.parseLong(立即数));
      } else {
        return 二进制码;
      }
    }
    if (立即数.length() % 2 == 1) {
      立即数 = "0" + 立即数;
    }

    // 去除高位
    // TODO: 需要检查符号位吗? -128 => 80或FF 80
    if (立即数.length() > 立即数位数 / 4) {
      立即数 = 立即数.substring(立即数.length() - 立即数位数 / 4, 立即数.length());
    }
    // 反序
    for (int 索引 = 0; 索引 < 立即数.length() / 2; 索引++) {
      二进制码.add(0, 立即数.substring(索引 * 2, 索引 * 2 + 2));
    }

    // 按照立即数位数补全0
    // 手册 2.1 INSTRUCTION FORMAT FOR PROTECTED MODE, REAL-ADDRESS MODE, AND VIRTUAL-8086 MODE
    // Immediate data of 1, 2, or 4 bytes or none
    for (int 位数 = 立即数.length() * 4; 位数 < 立即数位数; 位数 += 8) {
      二进制码.add("00");
    }

    return 二进制码;
  }

  public static String 生成操作码(int 操作码值) {
    String 操作码 = Integer.toHexString(操作码值);
    return 操作码值 > 15 ? 操作码 : "0" + 操作码;
  }
}
