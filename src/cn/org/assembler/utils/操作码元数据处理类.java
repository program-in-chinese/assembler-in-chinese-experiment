package cn.org.assembler.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class 操作码元数据处理类 {

  public static List<操作码信息类> 提取指令信息() {
    List<操作码信息类> 指令信息 = new ArrayList<>();
    try {
      Node 根 = DocumentBuilderFactory.newInstance().newDocumentBuilder()
          .parse(new File("资源/x86reference.xml")).getChildNodes().item(0);

      Node 单字节 = 根.getFirstChild().getNextSibling();

      // TODO: 添加双字节操作码
      // Node 双字节 = 单字节.getNextSibling().getNextSibling();

      NodeList 单字节指令 = 单字节.getChildNodes();

      // NodeList 双字节指令 = 双字节.getChildNodes();
      for (int 索引 = 0; 索引 < 单字节指令.getLength(); 索引++) {
        Node 元素 = 单字节指令.item(索引);
        if (元素.getNodeType() == Node.ELEMENT_NODE) {
          操作码信息类 某指令信息 = new 操作码信息类();
          某指令信息.值 = 元素.getAttributes().item(0).getNodeValue();
          指令信息.add(某指令信息);
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return 指令信息;
  }
}
