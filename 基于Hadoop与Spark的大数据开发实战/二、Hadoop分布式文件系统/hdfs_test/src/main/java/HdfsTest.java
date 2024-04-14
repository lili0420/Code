import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

/**
 * HDFS Java API操作
 */
public class HdfsTest {
    public static final String HDFS_PATH="hdfs://192.168.224.132:8020";
    Configuration configuration=null;
    static FileSystem fileSystem=null;

    @Before
    public void setUp() throws Exception{
        System.out.println("HdfsTest开始");
        configuration =new Configuration();
        fileSystem=FileSystem.get(new URI(HDFS_PATH),configuration,"root");
    }

    @After
    public void tearDown() throws Exception{
         fileSystem=null;
         configuration=null;
         System.out.println("HdfsTest结束");
    }

    /**
     * 使用JAVA API操作HDFS的常用操作
     * @param args
     */

    /**
     * 创建目录
     * @throws Exception
     */
    @Test
    public void mkdir() throws Exception{
        fileSystem.mkdirs(new Path("/hdfsapi/test"));

 }
    /**
     * 创建文件
     * @param
     */
    @Test
     public void create() throws Exception{
        FSDataOutputStream output=fileSystem.create(new Path("/hdfsapi/test/a.txt"));
        output.write("hello.world".getBytes());
        output.flush(); /*写入*/
        output.close(); /*关闭连接*/
    }

    /**
     *重命名
     */
    @Test
    public void rename() throws Exception{
        Path oldPath=new Path("/hdfsapi/test/a.txt");
        Path newPath=new Path("/hdfsapi/test/b.txt");
        System.out.println(fileSystem.rename(oldPath,newPath));
    }

    /**
     * 上传本地文件到HDFS
     */
    @Test
    public void copyFromLocalFile() throws Exception{
        Path src=new Path("C:\\hdfs_test\\src\\main\\java\\hello.txt");
        Path dist=new Path("/hdfsapi/test");
        fileSystem.copyFromLocalFile(src,dist);
    }

    /**
     * 查看某个目录下的所有文件
     */
    @Test
    public void listFiles() throws Exception{
        FileStatus[] listStatus=fileSystem.listStatus(new Path("/hdfsapi/test"));
        for(FileStatus fileStatus:listStatus){
            String isDir=fileStatus.isDirectory()?"文件夹":"文件";//文件/文件夹
            String permission=fileStatus.getPermission().toString();//权限
            short replication=fileStatus.getReplication();//副本系数
            long len=fileStatus.getLen();//长度
            String path=fileStatus.getPath().toString();//路径
            System.out.println(isDir+"\t"+permission+"\t"+replication+"\t"+len+"\t"+path);
        }
    }

    /**
     * 查看文件块信息
     */
    @Test
    public void getFileBlockLocations() throws Exception {
        FileStatus fileStatus = fileSystem.getFileStatus(new Path("/hdfsapi/test/b.txt"));
        BlockLocation[] blocks = fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        for (BlockLocation block : blocks) {
            for (String host : block.getHosts()) {
                System.out.println(host);
            }
        }
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
        fileSystem.copyToLocalFile(false, new Path("/hdfsapi/test/b.txt")
                , new Path("C:\\yun.txt"), true);
    }



    //HDFS文件删除
    @Test
    public void testRm() throws IOException {
        //删除文件
        //参数一：要删除的路径，参数二：是否递归删除
        fileSystem.delete(new Path("/hdfsapi/test/b.txt"), false);

        //删除非空目录
//       fileSystem.delete(new Path("/cheng"),true);
    }


}
