package cn.org.assembler.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class 操作码元数据处理类 {

  public static List<操作码元数据类> 提取操作码信息() {
    return 提取操作码信息("资源/x86reference.xml");
  }

  public static List<操作码元数据类> 提取操作码信息(String 文件名) {
    List<操作码元数据类> 操作码信息 = new ArrayList<>();
    try {
      Node 根 = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(文件名))
          .getChildNodes().item(0);

      Node 单字节 = 根.getFirstChild().getNextSibling();
      Node 双字节 = 单字节.getNextSibling().getNextSibling();

      操作码信息.addAll(提取操作码信息(单字节, 1));
      操作码信息.addAll(提取操作码信息(双字节, 2));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 操作码信息;
  }

  private static List<操作码元数据类> 提取操作码信息(Node 操作码元数据父节点, int 操作码字节数) {
    List<操作码元数据类> 操作码信息 = new ArrayList<>();

    for (Node 操作码节点 : 取子节点(操作码元数据父节点, "pri_opcd")) {
      操作码元数据类 操作码元数据 = new 操作码元数据类();
      String 操作码值 = 操作码节点.getAttributes().item(0).getNodeValue();
      操作码元数据.值 = Integer.parseInt(操作码值, 16);
      操作码元数据.操作码字节数 = 操作码字节数;

      // 取所有子entry节点
      for (Node entry节点 : 取子节点(操作码节点, "entry")) {
        指令元数据类 指令元数据 = new 指令元数据类();
        指令格式类 格式 = new 指令格式类();

        List<Node> 助记符节点 = 取子节点(取子节点(entry节点, "syntax").get(0), "mnem");

        // TODO: 部分entry没有syntax节点(如06的第二个). 暂时忽略
        if (助记符节点.isEmpty()) {
          System.out
              .println("无助记符, 暂时忽略: " + Integer.toHexString(操作码元数据.值) + " " + 操作码元数据.操作码字节数 + "字节");
          continue;
        }
        格式.助记符 = 助记符节点.get(0).getTextContent();
        指令元数据.格式.add(格式);
        操作码元数据.指令元数据.add(指令元数据);
      }
      操作码信息.add(操作码元数据);
    }
    return 操作码信息;
  }

  private static List<Node> 取子节点(Node 父节点, String 节点名) {
    List<Node> 节点 = new ArrayList<>();
    NodeList 子节点 = 父节点.getChildNodes();
    for (int 索引 = 0; 索引 < 子节点.getLength(); 索引++) {
      Node 某子节点 = 子节点.item(索引);
      if (某子节点.getNodeType() == Node.ELEMENT_NODE && 某子节点.getNodeName().equals(节点名)) {
        节点.add(某子节点);
      }
    }
    return 节点;
  }
}
