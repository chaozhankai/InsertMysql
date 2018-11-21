import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InsertMysql {
    private static String ReadTXT;  // 读取的txt文件
    private static String OutTXT;   // 写入的txt文件
    public static void main(String[] args) {
//        args = new String[2];
        // 解析参数
        if (args.length != 2) {
            System.out.println("参数不对，请依次输入需要读取的txt，输出SQL语句结构的.sql");
            return;
        }
        ReadTXT = args[0];
        OutTXT = args[1];
//        ReadTXT = "C:\\Users\\xiaozhankai\\Desktop\\1.txt";
//        OutTXT = "C:\\Users\\xiaozhankai\\Desktop\\2.sql";
        // 创建一个list用来存储读取到的数据
        List<String> list = new ArrayList<>();
        ReadFile(list);
        WriteFile(list);

    }

    /**
     * 读取txt文件
     */
    private static void ReadFile(List list) {
        // 防止文件建立或读取失败，用catch捕捉错误并打印
        // 带资源的try语句（try-with-resource）,自带关闭文件
        try (FileReader reader = new FileReader(ReadTXT);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            // 安全的线程,本来想全存到一个br里，但是发现写入的时候不会按行写入，遂改为ArrayList
            // StringBuffer bf = new StringBuffer();
            // 一次读取一行数据
            while ((line=br.readLine())!=null){
                list.add(line);
                // bf.append(line+System.lineSeparator()); // System.lineSeparator()换行符
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 写入txt文件
     */
    private static void WriteFile(List list){
        String before = "INSERT INTO `eleme`(`id`, `link`, `used`, `AddDate`, `LastDate`, `spare`) VALUES (NULL,'";
        String after = "', 0, CURDATE(), NULL, NULL);";
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateString = format.format(date);
        try {
            File file = new File(OutTXT);
//            file.createNewFile();
            try (FileWriter writer = new FileWriter(file,true); // 追加写入,一般可以自动创建文件
                 BufferedWriter out = new BufferedWriter(writer)){
                out.write(dateString+"     一共更新了"+list.size()+"条数据"+System.lineSeparator()); // \r\n为换行
                for (Object s:list){
                    System.out.println(s);
                    out.write(before+s.toString()+after+System.lineSeparator());
                }
                out.flush(); // 把缓存区内容写入到文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
