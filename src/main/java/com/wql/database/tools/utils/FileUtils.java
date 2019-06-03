package com.wql.database.tools.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;

/**
 * 文件操作工具类
 * Created by wendrewshay on 2019/06/03 16:06
 */
public class FileUtils {

    /**
     * 将工作簿文件流写入响应对象
     * @param response 响应对象
     * @param workbook 工作簿对象
     */
    public static void createFile(HttpServletResponse response, HSSFWorkbook workbook) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            byte[] bytes = out.toByteArray();

            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String((LocalDateTime.now().toString() + ".xls").getBytes(), "UTF-8"));
            response.setHeader("Content-Length", "" + bytes.length);

            BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(bytes));
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[8192];
            int bytesRead;
            while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
            bos.close();
            bis.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
