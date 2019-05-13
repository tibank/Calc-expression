package hillel.calc.utils;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessFiles {

    /*=================================================================================
    функция возвращает ArrayList строк из текстового файла для дальнейшей обработки
    ===================================================================================*/
    public static List<String> loadStrExpressions(String str) throws IOException {
        List<String> list = new ArrayList<>();

        File file = new File(str);
        if (!file.exists()) {
            // файла нет, нечего загружать
        } else {
            // получаем массив строк (одна строка - одна операция)
            list = Files.lines(Paths.get(str)).collect(Collectors.toList());
        }

        return list;
    }

    public static boolean writeListToFile(List<String> strResult, String fileResult) {

        try (FileWriter writer = new FileWriter(fileResult, false)) {
            // запись всей строки
            for (String str : strResult) {
                writer.write(str + "\n");
            }
            writer.flush();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    /*=================================================================================
    parsing одного узла
    ===================================================================================*/
    private static void parseNode(Node node, Map<String, String> hmRes, String prefix) {
        String key;
        Node childNode;
        if (node.getNodeType() == Node.ELEMENT_NODE) {  //
            if (node.hasAttributes()) {
                NamedNodeMap attrNode = node.getAttributes();
                // node with attributes, read them and write to Hashmap
                for (int j = 0; j < attrNode.getLength(); j++) {
                    // key write in a lower case (field of class usually called in lower case)
                    hmRes.put((prefix + attrNode.item(j).getNodeName()).toLowerCase(),
                            attrNode.item(j).getNodeValue());
                }
            } else {
                NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    childNode = childNodes.item(i);
                    if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                        // if node is without attributes we add node name to preffix and make reverse call procedure
                        // after we get key like this SMTPserver, SMTPpassword etc.
                        key = prefix + childNode.getNodeName();
                        parseNode(childNode, hmRes, key);
                    }
                }
            }
        }
    }

    /*=================================================================================
    обработка XML файла с конфигурацией калькулятора
    ===================================================================================*/
    public static Map<String, String> LoadFromXML(String fileName) {
        Map<String, String> hmResult = new HashMap<>();
        Node node;
        String key;

        boolean isReadXML = true;
        File file = new File(System.getProperty("user.dir") +
                File.separator + "src" +
                File.separator + "main" +
                File.separator + "resources" +
                File.separator + fileName);
        if (file.exists()) {

            // create document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;

            // wrapper for exit from block 'if' when catch exception
            // return default configuration
            while (true) {
                try {
                    db = dbf.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    //e.printStackTrace();
                    isReadXML = false;
                    break;
                }

                // get document DOM
                Document doc = null;
                try {
                    doc = db.parse(file.getAbsoluteFile());
                } catch (SAXException | IOException e) {
                    //e.printStackTrace();
                    isReadXML = false;
                    break;
                }
                // get root node
                NodeList listConfig = doc.getDocumentElement().getElementsByTagName("config");
                // get config
                if (!(listConfig.getLength() == 0)) {
                    // there are configs in the file
                    Node nodeConfig = listConfig.item(0);  // get only first node with config
                    NodeList nodeList = nodeConfig.getChildNodes();

                    for (int i = 0; i < nodeList.getLength(); i++) {
                        key = "";
                        node = nodeList.item(i);
                        parseNode(node, hmResult, key);
                    }
                }
                break;
            }
        }

        if (!isReadXML) {
            // either absent file of conf or have got errors of reading xml-file
            // return default conf of calculator
            hmResult.put("parser", "Simple");
            hmResult.put("validator", "Simple");
            hmResult.put("evaluator", "ReversePoland");
            hmResult.put("direxpression", "Expression");
        }

        return hmResult;
    }
}
