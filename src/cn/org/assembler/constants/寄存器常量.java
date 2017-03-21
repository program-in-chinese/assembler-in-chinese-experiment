package cn.org.assembler.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class 寄存器常量 {
  public static final String 四字累加寄存器名 = "RAX";
  public static final String 四字计数寄存器名 = "RCX";
  public static final String 四字数据寄存器名 = "RDX";
  public static final String 四字BX寄存器名 = "RBX";
  public static final String 四字SP寄存器名 = "RSP";
  public static final String 四字BP寄存器名 = "RBP";
  public static final String 四字SI寄存器名 = "RSI";
  public static final String 四字目标地址寄存器名 = "RDI";
  
  public static final String 双字累加寄存器名 = "EAX";
  public static final String 双字计数寄存器名 = "ECX";
  public static final String 双字数据寄存器名 = "EDX";
  public static final String 双字BX寄存器名 = "EBX";
  public static final String 双字SP寄存器名 = "ESP";
  public static final String 双字BP寄存器名 = "EBP";
  public static final String 双字SI寄存器名 = "ESI";
  public static final String 双字目标地址寄存器名 = "EDI";

  public static final String 单字累加寄存器名 = "AX";
  public static final String 单字计数寄存器名 = "CX";
  public static final String 单字数据寄存器名 = "DX";
  public static final String 单字BX寄存器名 = "BX";
  public static final String 单字SP寄存器名 = "SP";
  public static final String 单字BP寄存器名 = "BP";
  public static final String 单字SI寄存器名 = "SI";
  public static final String 单字目标地址寄存器名 = "DI";
  
  public static final HashSet<String> 四字寄存器 = new HashSet<>();
  public static final HashSet<String> 双字寄存器 = new HashSet<>();
  public static final HashSet<String> 单字寄存器 = new HashSet<>();
  
  private static final HashMap<String, Integer> 寄存器对应码 = new HashMap<>();
  
  // Table 3-1, Intel® 64 and IA-32 architectures software developer’s manual combined volumes Vol 2
  static {
    寄存器对应码.put(四字累加寄存器名, 0);
    寄存器对应码.put(四字计数寄存器名, 1);
    寄存器对应码.put(四字数据寄存器名, 2);
    寄存器对应码.put(四字BX寄存器名, 3);
    寄存器对应码.put(四字SP寄存器名, 4);
    寄存器对应码.put(四字BP寄存器名, 5);
    寄存器对应码.put(四字SI寄存器名, 6);
    寄存器对应码.put(四字目标地址寄存器名, 7);
    
    寄存器对应码.put(双字累加寄存器名, 0);
    寄存器对应码.put(双字计数寄存器名, 1);
    寄存器对应码.put(双字数据寄存器名, 2);
    寄存器对应码.put(双字BX寄存器名, 3);
    寄存器对应码.put(双字SP寄存器名, 4);
    寄存器对应码.put(双字BP寄存器名, 5);
    寄存器对应码.put(双字SI寄存器名, 6);
    寄存器对应码.put(双字目标地址寄存器名, 7);

    寄存器对应码.put(单字累加寄存器名, 0);
    寄存器对应码.put(单字计数寄存器名, 1);
    寄存器对应码.put(单字数据寄存器名, 2);
    寄存器对应码.put(单字BX寄存器名, 3);
    寄存器对应码.put(单字SP寄存器名, 4);
    寄存器对应码.put(单字BP寄存器名, 5);
    寄存器对应码.put(单字SI寄存器名, 6);
    寄存器对应码.put(单字目标地址寄存器名, 7);
    
    四字寄存器.addAll(Arrays.asList(四字累加寄存器名, 四字计数寄存器名, 四字数据寄存器名, 四字BX寄存器名, 四字SP寄存器名, 四字BP寄存器名,
        四字SI寄存器名, 四字目标地址寄存器名));
    双字寄存器.addAll(Arrays.asList(双字累加寄存器名, 双字计数寄存器名, 双字数据寄存器名, 双字BX寄存器名, 双字SP寄存器名, 双字BP寄存器名,
        双字SI寄存器名, 双字目标地址寄存器名));
    单字寄存器.addAll(Arrays.asList(单字累加寄存器名, 单字计数寄存器名, 单字数据寄存器名, 单字BX寄存器名, 单字SP寄存器名, 单字BP寄存器名,
        单字SI寄存器名, 单字目标地址寄存器名));
  }

  /**
   * @param 寄存器名 不区分大小写
   */
  public static boolean 为四字寄存器(String 寄存器名) {
    return 四字寄存器.contains(寄存器名.toUpperCase());
  }

  /**
   * @param 寄存器名 不区分大小写
   */
  public static boolean 为双字寄存器(String 寄存器名) {
    return 双字寄存器.contains(寄存器名.toUpperCase());
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
  public static Integer 取寄存器码(String 寄存器名) {
    return 寄存器对应码.get(寄存器名.toUpperCase());
  }
}
