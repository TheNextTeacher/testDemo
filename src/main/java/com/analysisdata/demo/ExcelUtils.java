package com.analysisdata.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author lijiaming
 * @title: ExcelUtils
 * @projectName demo
 * @description: TODO
 * @date 2019/6/2714:04
 */
public class ExcelUtils {

    public static void main(String[] args) throws Exception {
        //选用LinkedHashMap，保证迭代顺序的一致性
        Map<String, Integer> map = new LinkedHashMap<>();
        //加载配置文件（不用Properties读，在配置文件中的表头顺序即为创建Excel表格的顺序）
        InputStreamReader reader = new InputStreamReader(new FileInputStream("D:/temp/table.properties"), "UTF-8");
        BufferedReader br = new BufferedReader(reader);
        String readLine;
        while ((readLine = br.readLine()) != null) {
            String[] arr = readLine.split("=");
            map.put(arr[0], Integer.parseInt(arr[1]));
        }
        br.close();

        //迭代Map集合，并重构一套“根目录”
        Node root = new Node();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            root.addNodeByStringExpressionAndWidth(entry.getKey(), entry.getValue());
        }

        //遍历“根目录”
        List<Node> nodes = root.parseToSeqNodes();
        //获取树的深度
        Integer rootDeepLength = root.getDeepLength();


        File file = new File("D:/temp/test.xls");
        file.createNewFile();
        //1. 创建excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();

        //1.1 创建单元格样式
        HSSFCellStyle alignCenterStyle = workbook.createCellStyle();
        alignCenterStyle.setAlignment(HorizontalAlignment.CENTER);
        //表名样式：水平、垂直居中
        HSSFCellStyle font24Style = workbook.createCellStyle();
        font24Style.setAlignment(HorizontalAlignment.CENTER);
        font24Style.setVerticalAlignment(VerticalAlignment.CENTER);
        //24号字体
        HSSFFont font24 = workbook.createFont();
        font24.setFontHeightInPoints((short) 24);
        font24Style.setFont(font24);


        //2. 创建一个工作表Sheet
        HSSFSheet sheet = workbook.createSheet("新工作表");

        //3.1 创建一个行
        int row = 0;
        //3.2 在这个行中，创建一行单元格
        HSSFRow tableHeadRow = sheet.createRow(row);
        HSSFCell tableHeadCell = tableHeadRow.createCell(row);
        tableHeadRow.setRowStyle(font24Style);
        //3.3 设置该单元格的内容
        tableHeadCell.setCellValue("表名");
        tableHeadCell.setCellStyle(font24Style);
        row++;
        //表名的合并单元格无法在写表头之前计算（没有确定出有多少叶子结点）

        //row = 1

        //4. 写表头
        //4.1 创建多个行，并用数组存储
        HSSFRow[] rows = new HSSFRow[rootDeepLength];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = sheet.createRow(row + i);
        }
        int[] columnIndexArr = new int[rootDeepLength];

        //4.2 遍历所有结点
        for (Node node : nodes) {
            //获取该节点的深度
            int deep = node.getDeepLength();
            //深度为0，这是普通一级结点
            if (deep == 0) {
                //从下往上取行，向右创建
                int topRowIndex = node.getDeep();//获取这个结点的控制范围上限
                int bottomRowIndex = rows.length - deep - 1;//计算这个结点的控制范围下限
                for (int i = rows.length - 1; i >= 0; i--) {
                    rows[i].createCell(columnIndexArr[i]);
                }
                rows[topRowIndex].getCell(columnIndexArr[topRowIndex]).setCellValue(node.getText());
                //一列多行，但如果只有一行，就没有必要合并了
                if (topRowIndex != bottomRowIndex) {
                    sheet.addMergedRegion(new CellRangeAddress(row + topRowIndex, row + bottomRowIndex, columnIndexArr[topRowIndex], columnIndexArr[topRowIndex]));
                }
                //涉及到的列的下标数组统一往后推一格
                for (int i = topRowIndex; i <= bottomRowIndex; i++) {
                    columnIndexArr[i] += 1;
                }
                //最后一行一定全是叶子结点，要控制列宽
                sheet.setColumnWidth(columnIndexArr[columnIndexArr.length - 1], node.getWidth() * 2 * 256);
            } else {
                //深度不为0，复合结点，需要复杂构建
                //从下往上取行，向右创建
                int topRowIndex = node.getDeep();//获取这个结点的控制范围上限
                int bottomRowIndex = rows.length - deep - 1;//计算这个结点的控制范围下限
                int childrenCount = node.getChildrenCount();
                //并行创建，能控制到的每一行都要创建足够的容量使得下面的叶子结点能放得下
                for (int i = bottomRowIndex; i >= topRowIndex; i--) {
                    for (int j = 0; j < childrenCount; j++) {
                        rows[i].createCell(columnIndexArr[i] + j);
                    }
                    columnIndexArr[i] += childrenCount;
                }
                //填充值，合并单元格（不需要判定是否为一个单元格）
                rows[bottomRowIndex].getCell(columnIndexArr[bottomRowIndex] - childrenCount).setCellValue(node.getText());
                sheet.addMergedRegion(new CellRangeAddress(row + topRowIndex, row + bottomRowIndex, columnIndexArr[topRowIndex] - childrenCount, columnIndexArr[topRowIndex] - 1));
            }
        }
        row += rows.length;

        //表头的数据应该是很多单元格的合并、居中
        //四个参数：开始行，结束行，开始列，结束列
        //因为上面加了1，这里还要抵消掉
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnIndexArr[columnIndexArr.length - 1] - 1));

        //Workbook写入file中
        workbook.write(file);
        workbook.close();
    }
}