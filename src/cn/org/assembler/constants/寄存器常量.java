package cn.org.assembler.constants;

import java.util.HashMap;
import java.util.HashSet;

public class 寄存器常量 {
  public static final String 四字节累加寄存器名 = "RAX";
  public static final HashSet<String> 四字节寄存器 = new HashSet<>();
  
  private static final HashMap<String, Integer> 寄存器对应码 = new HashMap<>();
  
  // Table 3-1, Intel® 64 and IA-32 architectures software developer’s manual combined volumes
  static {
    寄存器对应码.put(四字节累加寄存器名, 0);
    
    四字节寄存器.add(四字节累加寄存器名);
  }

  /**
   * @param 寄存器名 不区分大小写
   */
  public static boolean 为四字节寄存器(String 寄存器名) {
    return 四字节寄存器.contains(寄存器名.toUpperCase());
  }
  
  /**
   * @param 寄存器名 不区分大小写
   */
  public static Integer 取寄存器码(String 寄存器名) {
    return 寄存器对应码.get(寄存器名.toUpperCase());
  }
}
