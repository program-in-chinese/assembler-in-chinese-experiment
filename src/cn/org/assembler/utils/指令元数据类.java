package cn.org.assembler.utils;

import java.util.ArrayList;
import java.util.List;

public class 指令元数据类 {

  /**
   * 默认无扩展码 (-1)
   */
  public int 扩展码 = -1;
  public List<指令格式类> 格式 = new ArrayList<>();
  
  public boolean 无扩展码() {
    return 扩展码 == -1;
  }
}
