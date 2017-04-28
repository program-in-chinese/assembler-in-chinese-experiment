package cn.org.assembler.模型;

import java.util.Objects;

import cn.org.assembler.模型.代码行类.操作数类型;

public class 操作数信息 {
  public 操作数类型 类型;
  public int 位数;
  public String 值;

  public 操作数信息() {}

  public 操作数信息(操作数类型 类型, int 位数, String 值) {
    this.类型 = 类型;
    this.位数 = 位数;
    this.值 = 值;
  }

  @Override
  public boolean equals(Object 对象) {
    return 对象 instanceof 操作数信息 && Objects.equals(类型, ((操作数信息) 对象).类型)
        && Objects.equals(位数, ((操作数信息) 对象).位数) && Objects.equals(值, ((操作数信息) 对象).值);
  }
  
  @Override
  public String toString() {
    return 类型 + " " + 位数 + " " + 值;
  }
}
