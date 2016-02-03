package com.sf.hash.util;

import java.io.*;

/**
 * Created by 862911 on 2016/1/6.
 */
public class codeCount {
    static long  normalLine=0;
    static long commentLine=0;
    static long whiteLine=0;

    private static void p(Object obj){
        System.out.println(obj);
    }
    private static void countcode(File f) throws Exception {
        BufferedReader br=null;
        boolean bln=false;
        try{
            br=new BufferedReader(new FileReader(f));
            String  line="";
            try {
                while((line = br.readLine()) != null) {
                    line=line.trim();
                    line = line.toLowerCase();
                    if(line.indexOf("create table")!=-1||line.indexOf("drop table")!=-1||line.indexOf("alter table")!=-1||line.indexOf("truncate table")!=-1
                            ||line.indexOf("drop index")!=-1||line.indexOf("create index")!=-1||line.indexOf("create database")!=-1||line.indexOf("drop database")!=-1){
                        throw new Exception("Please don't use DDL!");
                    }
                    if(line.matches("^[\\s&&[^\\n]]*$")){
                        whiteLine+=1;
                    }else if(line.startsWith("/*")&&!line.equals("*/")){
                        commentLine+=1;
                        bln=true;
                    }else if (bln==true){
                        commentLine+=1;
                        if(line.endsWith("*/")){
                            bln=false;
                        }
                    }else if(line.startsWith("/*")&&line.endsWith("*/")){
                        commentLine+=1;
                    }else if(line.startsWith("//")){
                        commentLine+=1;
                    }else {
                        normalLine+=1;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if(br!=null){
                try {
                    br.close();
                    br=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private static void recursivelyFileAccess(String path){
        File f = new File(path);

        File[] fs = f.listFiles();

        if (fs == null) {
            return;
        }

        for (File file : fs) {
            if (file.isFile()) {
                if(file.getName().matches("codeCount.java")){
                    continue;
                }
                if(file.getName().matches(".*\\.java$")||file.getName().matches(".*\\.xml$")){
                    try {
                        countcode(file);
                    } catch (Exception e) {
                        System.err.println(file.getName()+" has DDL!");
                        e.printStackTrace();
                    }
                }
            } else {
                recursivelyFileAccess(file.getPath());
            }
        }
    }

    public static void main(String args[]) throws Exception {
        recursivelyFileAccess(System.getProperty("user.dir"));
        p("注释的代码行数:"+commentLine);
        p("空白的代码行数:"+whiteLine);
        p("有效的代码行数:"+normalLine);
    }

}
