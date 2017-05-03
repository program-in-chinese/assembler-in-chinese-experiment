package cn.org.assembler.模型;

import java.util.ArrayList;
import java.util.List;

import cn.org.assembler.utils.操作码元数据类;

public class 代码行类 {

  public enum 操作数类型 {
    立即数,
    寄存器,
    内存
  }

  // 只有注释
  public boolean 为空 = false;
  
  // TODO: 待清理-操作码已包括助记符.
  public String 助记符;

  public 操作码元数据类 操作码;
  
  public List<操作数信息类> 操作数信息 = new ArrayList<>();

  public int 取有效操作码长度() {
    int 最大位数 = 0;
    for (操作数信息类 某操作数信息 : 操作数信息) {
      if (某操作数信息.位数 > 最大位数) {
        最大位数 = 某操作数信息.位数;
      }
    }
    return 最大位数;
  }

  @Override
  public String toString() {
    String 文本 = "操作符: " + 助记符;
    for (int i = 0; i < 操作数信息.size(); i++) {
      文本 += "操作数" + i + ":" + 操作数信息.get(i);
    }
    return 文本;
  }
}
