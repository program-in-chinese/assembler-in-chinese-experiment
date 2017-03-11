package cn.org.assembler;

public class 指令类 {


  // Legacy Prefix
  // REX prefix

  public String 助记符;

  //ModRM
  //SIB

  // TODO: 重命名
  public String displacement;
  public String immediate;

  @Override
  public String toString() {
    return "操作符: " + 助记符 + " 操作数1: " + displacement + " 操作数2: " + immediate;
  }

}
