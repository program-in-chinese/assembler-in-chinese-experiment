package cn.org.assembler.utils;

import java.util.ArrayList;
import java.util.List;

import cn.org.assembler.指令类;

public class 操作码信息类 {
  int 值;
  
  // TODO: 应该为指令指令信息/元数据类
  List<指令类> 指令 = new ArrayList<>();
  int 操作码字节数;
}
