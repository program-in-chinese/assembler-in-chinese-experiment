package cn.org.assembler.模型;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cn.org.assembler.分析器类;
import cn.org.assembler.utils.指令元数据类;
import cn.org.assembler.utils.指令格式类;
import cn.org.assembler.utils.操作数元数据类;
import cn.org.assembler.utils.操作码元数据类;

public class 代码行类 {

  // 只有注释
  public boolean 为空 = false;
  public String 助记符;

  public String 操作数1;
  public String 操作数2;
  public 操作数元数据类 操作数1类型;
  public 操作数元数据类 操作数2类型;

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
    Pattern 格式 =
        Pattern.compile("^(" + 操作符格式 + ")\\s+(" + 操作数1格式 + "),\\s*(" + 操作数2格式 + ")$");
    Matcher 匹配器 = 格式.matcher(行);
    if (匹配器.find()) {
      String 助记符 = 匹配器.group(1);
      String 操作数1 = 匹配器.group(2);
      String 操作数2 = 匹配器.group(3);

      // 语法检查

      // TODO: 查找现有助记符,如无匹配,报错
      操作数元数据类 操作数1类型 = 操作数元数据类.取操作数类型(操作数1);
      if (!操作数1类型.equals(操作数元数据类.不确定)) {
        操作数元数据类 操作数2类型 = 操作数元数据类.取操作数类型(操作数2);
        if (!操作数2类型.equals(操作数元数据类.不确定)) {
          代码行类 代码行 = new 代码行类();
          代码行.助记符 = 助记符;
          代码行.操作数1 = 操作数1.lastIndexOf(" ") > 0 ? 操作数1.substring(操作数1.lastIndexOf(" ") + 1) : 操作数1;
          代码行.操作数2 = 操作数2.lastIndexOf(" ") > 0 ? 操作数2.substring(操作数2.lastIndexOf(" ") + 1) : 操作数2;
          代码行.操作数1类型 = 操作数1类型;
          代码行.操作数2类型 = 操作数2类型;
          return 代码行;
        }
      }
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

  /**
   * 优先选择有显式名称(如立即数1), 与不需modr/m字节的
   * TODO: 在了解alias前避免使用,如C1_6.
   */
  private 操作码元数据类 选取最佳操作码(List<操作码元数据类> 操作码元数据) {
    for (操作码元数据类 某操作码元数据 : 操作码元数据) {
      for (指令元数据类 指令元数据 : 某操作码元数据.指令元数据) {
        for (指令格式类 格式 : 指令元数据.格式) {
          // TODO: 假设有2个操作数
          // 暂时: 由于可以省去立即数部分,生成指令一般较短,因此优先
          if (格式.操作数.get(1).显式名称 != null) {
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
    int 操作数1长度 = 操作数1类型.取位数();
    int 操作数2长度 = 操作数2类型.取位数();
    return 操作数1长度 > 操作数2长度 ? 操作数1长度 : 操作数2长度;
  }

  @Override
  public String toString() {
    return "操作符: " + 助记符 + " 操作数1: " + 操作数1 + " 操作数2: " + 操作数2;
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
