package cn.org.assembler.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class 寄存器常量 {

  private static final String 四字寄存器前缀 = "R";
  private static final String 双字寄存器前缀 = "E";

  private static final String 单字累加寄存器名 = "AX";
  private static final String 单字计数寄存器名 = "CX";
  private static final String 单字数据寄存器名 = "DX";
  private static final String 单字BX寄存器名 = "BX";
  private static final String 单字SP寄存器名 = "SP";
  private static final String 单字BP寄存器名 = "BP";
  private static final String 单字SI寄存器名 = "SI";
  private static final String 单字目标地址寄存器名 = "DI";
  
  private static final String 单字节累加寄存器名 = "AL";
  private static final String 单字节计数寄存器名 = "CL";
  private static final String 单字节数据寄存器名 = "DL";
  private static final String 单字节BX寄存器名 = "BL";
  private static final String 单字节SP寄存器名 = "SPL";
  private static final String 单字节BP寄存器名 = "BPL";
  private static final String 单字节SI寄存器名 = "SIL";
  private static final String 单字节目标地址寄存器名 = "DIL";
  
  private static final HashSet<String> 单字寄存器 = new HashSet<>();
  private static final HashSet<String> 单字节寄存器 = new HashSet<>();
  
  private static final HashMap<String, Integer> 寄存器对应码 = new HashMap<>();
  
  // Table 3-1, Intel® 64 and IA-32 architectures software developer’s manual combined volumes Vol 2
  static {
    寄存器对应码.put(单字累加寄存器名, 0);
    寄存器对应码.put(单字计数寄存器名, 1);
    寄存器对应码.put(单字数据寄存器名, 2);
    寄存器对应码.put(单字BX寄存器名, 3);
    寄存器对应码.put(单字SP寄存器名, 4);
    寄存器对应码.put(单字BP寄存器名, 5);
    寄存器对应码.put(单字SI寄存器名, 6);
    寄存器对应码.put(单字目标地址寄存器名, 7);

    寄存器对应码.put(单字节累加寄存器名, 0);
    寄存器对应码.put(单字节计数寄存器名, 1);
    寄存器对应码.put(单字节数据寄存器名, 2);
    寄存器对应码.put(单字节BX寄存器名, 3);
    寄存器对应码.put(单字节SP寄存器名, 4);
    寄存器对应码.put(单字节BP寄存器名, 5);
    寄存器对应码.put(单字节SI寄存器名, 6);
    寄存器对应码.put(单字节目标地址寄存器名, 7);
    
    单字寄存器.addAll(Arrays.asList(单字累加寄存器名, 单字计数寄存器名, 单字数据寄存器名, 单字BX寄存器名, 单字SP寄存器名, 单字BP寄存器名,
        单字SI寄存器名, 单字目标地址寄存器名));
    单字节寄存器.addAll(Arrays.asList(单字节累加寄存器名, 单字节计数寄存器名, 单字节数据寄存器名, 单字节BX寄存器名, 单字节SP寄存器名, 单字节BP寄存器名,
        单字节SI寄存器名, 单字节目标地址寄存器名));
  }

  /**
   * @param 寄存器名 不区分大小写
   */
  public static boolean 为四字寄存器(String 寄存器名) {
    寄存器名 = 寄存器名.toUpperCase();
    return 寄存器名.startsWith(四字寄存器前缀) && 单字寄存器.contains(寄存器名.substring(1));
  }

  /**
   * @param 寄存器名 不区分大小写
   */
  public static boolean 为双字寄存器(String 寄存器名) {
    寄存器名 = 寄存器名.toUpperCase();
    return 寄存器名.startsWith(双字寄存器前缀) && 单字寄存器.contains(寄存器名.substring(1));
  }

  /**
   * @param 寄存器名 不区分大小写
   */
  public static boolean 为单字寄存器(String 寄存器名) {
    return 单字寄存器.contains(寄存器名.toUpperCase());
  }

  /**
   * @param 寄存器名 不区分大小写
   */
  public static boolean 为单字节寄存器(String 寄存器名) {
    return 单字节寄存器.contains(寄存器名.toUpperCase());
  }

  /**
   * @param 寄存器名 不区分大小写
   */
  public static Integer 取寄存器码(String 寄存器名) {
    寄存器名 = 寄存器名.toUpperCase();
    if (寄存器名.startsWith(四字寄存器前缀) || 寄存器名.startsWith(双字寄存器前缀)) {
      寄存器名 = 寄存器名.substring(1);
    }
    return 寄存器对应码.get(寄存器名);
  }
}
