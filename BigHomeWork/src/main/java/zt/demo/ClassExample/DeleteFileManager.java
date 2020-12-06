package zt.demo.ClassExample;

import java.io.File;

public class DeleteFileManager {
    //删除一个文件
    public boolean DeleteFile(File file){
        if(file.exists()){
            boolean ans=file.delete();
            return ans;
        }
        else return false;
    }
    //删除一个文件夹
    public boolean DeleteDir(File dir){
        if(!dir.exists())return false;
        File[] files=dir.listFiles();
        assert files != null;
        for(int i = 0; i<files.length; i++){
            if(files[i].isFile()){
                boolean ans=DeleteFile(files[i]);
                return ans;
            }
            if(files[i].isDirectory()){
                boolean ans=DeleteDir(files[i]);
                return ans;
            }
        }
        return true;
    }
}
