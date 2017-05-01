package cn.org.assembler.模型;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cn.org.assembler.分析器类;
import cn.org.assembler.constants.寄存器常量;
import cn.org.assembler.utils.指令元数据类;
import cn.org.assembler.utils.指令格式类;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据类;

public class 代码行类 {

  public enum 操作数类型 {
    立即数,
    寄存器,
    内存
  }

  // 只有注释
  public boolean 为空 = false;
  public String 助记符;

  public List<操作数信息> 操作数信息 = new ArrayList<>();

  public static 代码行类 分析(String 行) {
    行 = 删除注释(行).trim();
    if (行.isEmpty()) {
      代码行类 代码行 = new 代码行类();
      代码行.为空 = true;
      return 代码行;
    }
    String 操作符格式 = "[A-Za-z]+";
    String 操作数1格式 = "[A-Za-z0-9\\s\\[\\]]+";
    String 操作数2格式 = "[\\-A-Za-z0-9\\s\\[\\]]+";
    Pattern 无操作数格式 = Pattern.compile("^(" + 操作符格式 + ")");
    Pattern 单操作数格式 = Pattern.compile("^(" + 操作符格式 + ")\\s+(" + 操作数2格式 + ")");
    Pattern 双操作数格式 =
        Pattern.compile("^(" + 操作符格式 + ")\\s+(" + 操作数1格式 + "),\\s*(" + 操作数2格式 + ")$");
    Matcher 匹配器 = 双操作数格式.matcher(行);
    if (匹配器.find()) {
      String 助记符 = 匹配器.group(1);
      String 操作数1 = 匹配器.group(2);
      String 操作数2 = 匹配器.group(3);

      // 语法检查

      // TODO: 查找现有助记符,如无匹配,报错
      操作数信息 操作数2信息 = 取操作数信息(操作数2);
      if (操作数2信息 != null) {
        操作数信息 操作数1信息 = 取操作数信息(操作数1);
        if (操作数1信息 != null) {
          // 如果无法判断位数,如[0], 则采用操作数2的位数
          // TODO: 必需吗?
          if(操作数1信息.位数 == 0) {
            操作数1信息.位数 = 操作数2信息.位数;
          }
          代码行类 代码行 = new 代码行类();
          代码行.助记符 = 助记符;
          
          代码行.操作数信息.add(操作数1信息);
          代码行.操作数信息.add(操作数2信息);
          return 代码行;
        }
      }
    }
    匹配器 = 单操作数格式.matcher(行);
    if (匹配器.find()) {
      String 助记符 = 匹配器.group(1);
      String 操作数1 = 匹配器.group(2);
      操作数信息 操作数1信息 = 取操作数信息(操作数1);
      if (操作数1信息 != null) {
        代码行类 代码行 = new 代码行类();
        代码行.助记符 = 助记符;
        
        //代码行.操作数1信息 = 操作数1信息;
        代码行.操作数信息.add(操作数1信息);
        return 代码行;
      }
    }
    匹配器 = 无操作数格式.matcher(行);
    if (匹配器.find()) {
      代码行类 代码行 = new 代码行类();
      代码行.助记符 = 匹配器.group(1);
      return 代码行;
    }
    
    return null;
  }

  public 操作码元数据类 查找操作码() {
    List<操作码元数据类> 操作码元数据 = 分析器类.查找操作码(this);

    // TODO: 改善反馈信息
    if (操作码元数据.isEmpty()) {
      System.out.println("无匹配操作码: " + this);
      return null;
    } else if (操作码元数据.size() > 1) {
      List<操作码元数据类> 专用操作码 = 取专用操作码(操作码元数据);
      if (专用操作码.size() > 1) {
        System.out.println(操作码元数据.size() + "个匹配的操作码: "
            + 操作码元数据.stream().map(操作码元数据类::toString).collect(Collectors.joining(", ")));
        return 选取最佳操作码(专用操作码);
      } else if (专用操作码.size() == 1) {
        return 专用操作码.get(0);
      } else {
        return 选取最佳操作码(操作码元数据);
      }
    } else {
      // TODO: shl rax, 5 有c1中两种格式,扩展码为4/6. 在确定区别之前,暂时取第一个
      return 操作码元数据.get(0);
    }
  }

  public static 操作数信息 取操作数信息(String 操作数) {
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
      操作数信息 操作对象信息 = 取操作数信息(操作对象);
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
        操作数信息 信息 = new 操作数信息();
        信息.类型 = 操作数类型.内存;
        信息.值 = 操作数.substring(1, 操作数.length() - 1);
        return 信息;
    } else {
      return 寄存器常量.取寄存器信息(操作数);
    }
  }

  private static 操作数信息 取操作数类型(long 数值) {
    操作数信息 信息 = new 操作数信息();
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

  /**
   * 优先选择有显式名称(如立即数1), 与不需modr/m字节的
   * TODO: 在了解alias前避免使用,如C1_6.
   */
  private 操作码元数据类 选取最佳操作码(List<操作码元数据类> 操作码元数据) {
    for (操作码元数据类 某操作码元数据 : 操作码元数据) {
      for (指令元数据类 指令元数据 : 某操作码元数据.指令元数据) {
        for (指令格式类 格式 : 指令元数据.格式) {
          // TODO: 支持2个以外操作数
          // 暂时: 由于可以省去立即数部分,生成指令一般较短,因此优先
          if (格式.操作数.size() == 2 && 格式.操作数.get(1).显式名称 != null) {
            return 某操作码元数据;
          } else {
            操作数元数据类 操作数类型 = 格式.操作数.get(0);
            // 返回第一个不需mod位的
            if (!(操作数类型.寻址方式.equals(操作数元数据类.寻址方式_寄存器_ModRM)
                || 操作数类型.寻址方式.equals(操作数元数据类.寻址方式_寄存器_ModRM_mod_通用寄存器) 
                || 操作数类型.寻址方式.equals(操作数元数据类.寻址方式_寄存器_ModRM_reg_通用寄存器))) {
              return 某操作码元数据;
            }
          }
        }
      }
    }
    return 操作码元数据.get(0);
  }

  public int 取有效操作码长度() {
    int 最大位数 = 0;
    for (操作数信息 某操作数信息 : 操作数信息) {
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

  // 选取无扩展码的指令
  private List<操作码元数据类> 取专用操作码(List<操作码元数据类> 操作码元数据) {
    List<操作码元数据类> 专用操作码 = new ArrayList<>();
    for (操作码元数据类 某操作码元数据 : 操作码元数据) {
      for (指令元数据类 指令元数据 : 某操作码元数据.指令元数据) {
        if (指令元数据.无扩展码()) {
          专用操作码.add(某操作码元数据);
          break;
        }
      }
    }
    return 专用操作码;
  }

  private static String 删除注释(String 行) {
    int 注释位置 = 行.indexOf(";");
    return 注释位置 == -1 ? 行 : 行.substring(0, 注释位置);
  }
}
