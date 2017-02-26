package cn.org.assembler;

public class 指令 {


  // Legacy Prefix
  // REX prefix

  String 操作符;

  //ModRM
  //SIB

  String displacement;
  String immediate;

  @Override
  public String toString() {
    return "操作符: " + 操作符 + " 操作数1: " + displacement + " 操作数2: " + immediate;
  }

}
