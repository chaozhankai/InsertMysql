import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InsertMysql {
    private static String ReadTXT;  // 读取的txt文件
    private static String OutLINK;   // 写入带头像的垫包
    private static String OutSQL;   // 写入SQL语句
    public static void main(String[] args) {
        // 预写入输出文件名
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");   // 格式化日期不能带\，否则会炸
        String dateString = format.format(date);
//        args = new String[3];
        // 解析参数
        if (args.length == 1){
            // 不需要带路径了System.getProperty("user.dir")+"\\"+
            OutLINK = dateString+" elemeLink.txt";
            OutSQL = dateString+" elemeSQL.sql";
            System.out.println("可选择输出带垫包头像和SQL语句的文件名");
            System.out.println("默认输出&_from=xzk的文件名为："+OutLINK);
            System.out.println("默认输出SQL语句的文件名为："+OutLINK);
        }else if (args.length == 2){
            OutLINK = args[1];
            OutSQL = dateString+" elemeSQL.sql";
        }else if (args.length == 3){
            OutLINK = args[1];
            OutSQL = args[2];
        }else {
            System.out.println("参数不对，请必须输入需要读取的txt，可选输出带头像垫包的链接的文件名及输出SQL语句结构的.sql名");
            return;
        }
        ReadTXT = args[0];
//        ReadTXT = "C:\\Users\\xiaozhankai\\Desktop\\1.txt";
//        OutTXT = "C:\\Users\\xiaozhankai\\Desktop\\2.sql";
        // 创建一个list用来存储读取到的数据
        List<String> list = new ArrayList<>();
        ReadFile(list);
        WriteLink(list);
        WriteSQL(list);

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
     * 写入txt文件，把原本的链接修改为后面带&_from=xzk的
     */
    private static void WriteLink(List list){
        String after = "&_from=xzk";
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateString = format.format(date);
        try {
            File file = new File(OutLINK);
//            file.createNewFile();
            try (FileWriter writer = new FileWriter(file,true); // 追加写入,一般可以自动创建文件
                 BufferedWriter out = new BufferedWriter(writer)){
                out.write(dateString+"     一共转换了"+list.size()+"条数据"+System.lineSeparator()); // \r\n为换行
                for (Object s:list){
                    System.out.println(s);
                    out.write(s.toString()+after+System.lineSeparator());
                }
                out.flush(); // 把缓存区内容写入到文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入SQL文件，方便插入到数据库的
     */
    private static void WriteSQL(List list){
        // 加入对应的SQL语句，方便插入数据库
        String before = "INSERT INTO `eleme`(`id`, `link`, `used`, `AddDate`, `LastDate`, `spare`) VALUES (NULL,'";
        String after = "', 0, CURDATE(), NULL, NULL);";
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String dateString = format.format(date);
        try {
            File file = new File(OutSQL);
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
