package cn.org.assembler.模型;

import java.util.ArrayList;
import java.util.List;

import cn.org.assembler.utils.操作数元数据类;

public class 指令类 {

  // Legacy Prefix
  public String rex前缀;

  public String 操作码;

  public String modRM;
  // SIB

  public String displacement;
  public String 立即数;

  public List<String> 生成二进制码() {
    List<String> 二进制码 = new ArrayList<>();
    if (rex前缀 != null) {
      二进制码.add(rex前缀);
    }
    if (操作码 != null) {
      二进制码.add(操作码);
    }
    if (modRM != null) {
      二进制码.add(modRM);
    }
    二进制码.addAll(生成二进制码(立即数, 操作数元数据类.取操作数类型(立即数).equals(操作数元数据类.立即数32) ? 32 : 64));
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
    for (int 索引 = 0; 索引 < 立即数.length() / 2; 索引++) {
      二进制码.add(0, 立即数.substring(索引 * 2, 索引 * 2 + 2));
    }

    // 按照立即数位数补全0
    for (int 位数 = 立即数.length() * 4; 位数 < 立即数位数; 位数 += 8) {
      二进制码.add("00");
    }

    return 二进制码;
  }
}
