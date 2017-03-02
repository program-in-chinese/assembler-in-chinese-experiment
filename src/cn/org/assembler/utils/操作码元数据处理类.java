package cn.org.assembler.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class 操作码元数据处理类 {

  public static List<操作码信息类> 提取操作码信息() {
    List<操作码信息类> 操作码信息 = new ArrayList<>();
    try {
      Node 根 = DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(new File("资源/x86reference.xml")).getChildNodes().item(0);

      Node 单字节 = 根.getFirstChild().getNextSibling();
      Node 双字节 = 单字节.getNextSibling().getNextSibling();

      操作码信息.addAll(提取操作码信息(单字节.getChildNodes(), 1));
      操作码信息.addAll(提取操作码信息(双字节.getChildNodes(), 2));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return 操作码信息;
  }
  
  private static List<操作码信息类> 提取操作码信息(NodeList 操作码元数据, int 操作码字节数) {
    List<操作码信息类> 操作码信息 = new ArrayList<>();
    
    for (int 索引 = 0; 索引 < 操作码元数据.getLength(); 索引++) {
      Node 元素 = 操作码元数据.item(索引);
      if (元素.getNodeType() == Node.ELEMENT_NODE) {
        操作码信息类 某操作码信息 = new 操作码信息类();
        String 操作码值 = 元素.getAttributes().item(0).getNodeValue();
        某操作码信息.值 = Integer.parseInt(操作码值, 16);
        某操作码信息.操作码字节数 = 操作码字节数;
        操作码信息.add(某操作码信息);
      }
    }
    return 操作码信息;
  }
}
