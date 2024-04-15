import sys
import hdfs


class HdfsClient:

    def __init__(self):
        # 创建Hdfs连接
        self.Client = hdfs.InsecureClient(url="http://192.168.224.132:9870", user='root')

    def mkdirs(self):
        # 在根目录下创建一个文件夹，名字叫做lili
        self.Client.makedirs('/lili')
        # 列出/目录的文件
        print(self.Client.list('/'))

    def updata(self):
        # 增加文件/写文件
        self.Client.write(hdfs_path="/list.txt", overwrite=True,
                          data="世界你好！".encode("utf-8"), )
        print(self.Client.list("/"))

    # def load(self):
    #     # 上传文件
    #     self.Client.upload(hdfs_path='/', local_path='list.txt')
    #     print(self.Client.list('/'))

    def check(self):
        # 查看文件是否存在
        # # strict如果设置为True时，文件不存在就会抛出异常，如果为False文件不存在就会返回None。
        # 如果文件存在，不管设置了什么都会返回改文件的block信息
        print(self.Client.status(hdfs_path='/datatest', strict=False))

    def readfile(self):
        # 读文件
        with self.Client.read('/list.txt') as f:
            print(f.data.decode('utf-8'))

    # def delete(self):
    #     self.Client.delete(hdfs_path='/list.txt')
    #     print(self.Client.list('/'))
    #

if __name__ == "__main__":
    client = HdfsClient()
    client.mkdirs()
    client.updata()
    # client.load()
    client.check()
    client.readfile()
    # client.delete()
