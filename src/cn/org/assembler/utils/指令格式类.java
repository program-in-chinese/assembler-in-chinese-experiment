package cn.org.assembler.utils;

import java.util.ArrayList;
import java.util.List;

public class 指令格式类 {

  public String 助记符;
  
  /**
   * 目标操作数在前, 源操作数在后, 排序与xml中相同
   */
  public List<操作数元数据类> 操作数 = new ArrayList<>();
}
