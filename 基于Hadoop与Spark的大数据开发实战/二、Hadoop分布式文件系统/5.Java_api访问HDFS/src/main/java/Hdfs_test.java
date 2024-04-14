
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * TODO
 *   客户端代码常用套路
 *      1.获取客户端对象
 *      2.执行相关操作命令
 *      3.关闭资源
 * @Author Wayne
 * @Date 2023/12/19 10:05
 */
public class Hdfs_test {

    private FileSystem fs;

    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        //连接的集群nn地址
        URI uri = new URI("hdfs://192.168.224.132:8020/");
        //创建一个配置文件（即获取文件系统）
        Configuration configuration = new Configuration();
        configuration.set("dfs.replication", "2");//设置备份个数2
        //用户
        String user = "root";


        /**
         *     //第26-31行还可以写成：
         *     1.获取文件系统
         *         Configuration configuration = new Configuration();
         *         configuration.set("dfs.replication", "2");
         *         FileSystem fs = FileSystem.get(new URI("hdfs://192.168.224.132:8020"), configuration, "root");
         */
        System.out.println("创建连接成功");

        //获得客户端对象
        fs = FileSystem.get(uri, configuration, user);
    }

    @After
    public void close() throws IOException {
        System.out.println("释放连接");
        //关闭资源
        fs.close();
    }

    //创建目录
    @Test
    public void testMkdir() throws URISyntaxException, IOException, InterruptedException {
        fs.mkdirs(new Path("/lili"));
    }

    //上传文件
    @Test
    public void testCopyFromLocalFile() throws IOException, InterruptedException, URISyntaxException {

        /***
         *1 获取文件系统
         *        Configuration configuration = new Configuration();
         *        configuration.set("dfs.replication", "2");
         *        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.224.132:8020"), configuration, "root")
         */
        // 2 上传文件
        //参数一：是否产出原数据；参数二：是否允许覆盖；参数三：原数据路径；参数四：目的地路径
        fs.copyFromLocalFile(false, false, new Path("C:\\5.Java_api访问HDFS\\src\\main\\java\\yun.txt"), new Path("/lili/yun.txt"));

        // 3 关闭资源
        //fs.close();
    }


    //HDFS文件下载
    @Test
    public void testGet() throws IOException {
        //参数一：原文件是否删除，参数二：原文件路径HDFS，参数三：目标地址路径Windows,参数四：是否开启本地校验
        /**
         * 执行下载操作
         *         // boolean delSrc 指是否将原文件删除
         *         // Path src 指要下载的文件路径
         *         // Path dst 指将文件下载到的路径
         *         // boolean useRawLocalFileSystem 是否开启文件校验
         */
        fs.copyToLocalFile(false, new Path("/lili/yun.txt")
                , new Path("C:\\yun.txt"), true);
    }

    //HDFS文件删除
    @Test
    public void testRm() throws IOException {
        //删除文件
        //参数一：要删除的路径，参数二：是否递归删除
        fs.delete(new Path("/lili/yun.txt"), false);

        //删除非空目录
//        fs.delete(new Path("/cheng"),true);
    }

    //HDFS文件的更名和移动
    @Test
    public void testMv() throws IOException {
        //文件重命名
        //参数一：原文件路径；参数2：目标文件路径
        fs.rename(new Path("/lili/yun.txt"), new Path("/lili/yun1.txt"));

        //文件的移动和更名
        // fs.rename(new Path("/input/myGirl.jpg"),new Path("/myFirstGirl.jpg"));

        //目录的更名
//        fs.rename(new Path("/lili"),new Path("/moli"));
    }


    //HDFS文件详情查看
    @Test
    public void testListFiles() throws IOException, InterruptedException, URISyntaxException {

        // 获取文件详情
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
        //遍历文件
        while (listFiles.hasNext()) {
            LocatedFileStatus fileStatus = listFiles.next();
            System.out.println("========" + fileStatus.getPath() + "=========");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());

            // 获取块信息
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();
            System.out.println(Arrays.toString(blockLocations));
        }
    }


    //HDFS文件和文件夹判断
    @Test
    public void testListStatus() throws IOException, InterruptedException, URISyntaxException {

        //判断是文件还是文件夹
        FileStatus[] listStatus = fs.listStatus(new Path("/"));
        for (FileStatus fileStatus : listStatus) {
            // 如果是文件
            if (fileStatus.isFile()) {
                System.out.println("文件:" + fileStatus.getPath().getName());
            } else {
                System.out.println("目录:" + fileStatus.getPath().getName());
            }
        }
    }


}
